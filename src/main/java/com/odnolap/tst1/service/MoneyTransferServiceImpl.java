package com.odnolap.tst1.service;

import com.odnolap.tst1.model.GetTransactionsRequest;
import com.odnolap.tst1.model.GetTransactionsResponse;
import com.odnolap.tst1.model.MoneyTransferRequest;
import com.odnolap.tst1.model.db.Account;
import com.odnolap.tst1.model.db.Currency;
import com.odnolap.tst1.model.db.Customer;
import com.odnolap.tst1.model.db.ExchangeRate;
import com.odnolap.tst1.model.db.MoneyTransferTransaction;
import com.odnolap.tst1.model.dto.MoneyTransferTransactionDto;
import com.odnolap.tst1.repository.MoneyTransferRepository;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Singleton
@Slf4j
public class MoneyTransferServiceImpl implements MoneyTransferService {

    @Inject
    private MoneyTransferRepository repository;

    public GetTransactionsResponse getTransactions(GetTransactionsRequest request) {
        GetTransactionsResponse result = new GetTransactionsResponse();
        List<MoneyTransferTransaction> transactions;
        if (request.getTransactionId() != null) {
            transactions = Collections.singletonList(repository.getTransaction(request.getTransactionId()));
        } else if (request.getAccountId() != null) {
            Long accountId = request.getAccountId();
            Account account = getAccount(accountId);
            if (account == null) {
                throw new IllegalArgumentException("Account with id " + accountId + " doesn't exists. Transaction not created");
            } else {
                transactions = repository.getAccountTransactions(accountId, request.getStartFrom(), request.getOffset());
            }
        } else if (request.getCustomerId() != null) {
            Long customerId = request.getCustomerId();
            Customer customer = getCustomer(customerId);
            if (customer == null) {
                throw new IllegalArgumentException("Customer with id " + customerId + " doesn't exists. Transaction not created");
            } else {
                transactions = repository.getCustomerTransactions(customerId, request.getStartFrom(), request.getOffset());
            }
        } else {
            transactions = repository.getAllTransactions(request.getStartFrom(), request.getOffset());
        }

        List<MoneyTransferTransactionDto> resultTransactions = transactions.stream()
            .filter(Objects::nonNull)
            .map(MoneyTransferTransactionDto::new)
            .collect(Collectors.toList());
        if (resultTransactions.isEmpty()) {
            log.warn("Transaction list for get transactions request is empty:\n{}", request);
        }
        result.setTransactions(resultTransactions);

        return result;
    }

    @Override
    public MoneyTransferTransactionDto createMoneyTransferTransaction(MoneyTransferRequest request) {
        MoneyTransferTransaction newTransaction = null;
        Date transactionRegistered = new Date();
        String errMsg = null;

        try {
            Long accountFromId = request.getAccountFromId();
            Account accountFrom = getAccount(accountFromId);
            if (accountFrom == null) {
                errMsg = "Account with id " + accountFromId + " doesn't exists. Transaction not created";
                log.error(errMsg + ":\n{}", request);
            } else {
                Long customerToId = request.getCustomerToId();
                Customer customerTo = getCustomerWithAccounts(customerToId);
                if (customerTo == null) {
                    errMsg = "Customer with id " + customerToId + " doesn't exists. Transaction not created";
                    log.error(errMsg + ":\n{}", request);
                } else {
                    Currency currencyTo = request.getCurrencyTo();
                    Account accountTo = customerTo.getAccounts().stream()
                        .filter(acct -> acct.getCurrency() == currencyTo)
                        .findFirst()
                        .orElse(null);
                    if (accountTo == null) {
                        errMsg = "Account with currency " + currencyTo + " for customer with id "
                            + customerToId + " doesn't exists. Transaction not created";
                        log.error(errMsg + ":\n{}", request);
                    } else {
                        // Only here we start changing info in DB (block amount,
                        Float amountFrom = request.getAmountFrom();
                        boolean isAmountEnough = blockAmount(accountFrom, amountFrom);
                        try {
                            if (!isAmountEnough) {
                                newTransaction = returnBlockedAmount(accountFromId, amountFrom, "");
                            } else {
                                Currency currencyFrom = accountFrom.getCurrency();
                                ExchangeRate exchangeRate = getApropriateRate(transactionRegistered, currencyFrom, currencyTo);
                                if (exchangeRate == null) {
                                    newTransaction = returnBlockedAmount(accountFromId, amountFrom, "");
                                } else {
                                    newTransaction = moveBlockedAmount(accountFromId, amountFrom,
                                        accountTo, exchangeRate, transactionRegistered);
                                }
                            }
                        } catch (Exception ex) {
                            log.error("Error during creating transaction:\n" + request, ex);
                            newTransaction = returnBlockedAmount(accountFromId, amountFrom);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            errMsg = "Error during creating transaction: " + ex.getMessage();
            log.error(errMsg + ":\n" + request, ex);
        }

        MoneyTransferTransaction savedTransaction = null;
        if (newTransaction != null) {
            newTransaction.setFinalizationTimestamp(new Date());
            savedTransaction = repository.saveTransaction(newTransaction);
        } else {
            MoneyTransferTransactionDto notSavedErrorResult = new MoneyTransferTransactionDto();
            notSavedErrorResult.setDescription(errMsg);
            return notSavedErrorResult;
        }

        return new MoneyTransferTransactionDto(savedTransaction);
    }
}
