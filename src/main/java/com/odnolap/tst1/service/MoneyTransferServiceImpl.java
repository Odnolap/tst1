package com.odnolap.tst1.service;

import com.odnolap.tst1.model.GetTransactionsRequest;
import com.odnolap.tst1.model.GetTransactionsResponse;
import com.odnolap.tst1.model.MoneyTransferRequest;
import com.odnolap.tst1.model.db.Account;
import com.odnolap.tst1.model.db.Currency;
import com.odnolap.tst1.model.db.Customer;
import com.odnolap.tst1.model.db.ExchangeRate;
import com.odnolap.tst1.model.db.MoneyTransferTransaction;
import com.odnolap.tst1.model.db.MoneyTransferTransactionStatus;
import com.odnolap.tst1.model.dto.MoneyTransferTransactionDto;
import com.odnolap.tst1.model.exceptions.MoneyTransferProcessingException;
import com.odnolap.tst1.repository.AccountRepository;
import com.odnolap.tst1.repository.CustomerRepository;
import com.odnolap.tst1.repository.ExchangeRateRepository;
import com.odnolap.tst1.repository.MoneyTransferRepository;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.odnolap.tst1.model.db.MoneyTransferTransactionStatus.REJECTED;
import static com.odnolap.tst1.model.db.MoneyTransferTransactionStatus.SUCCESSFUL;

@Singleton
@Slf4j
public class MoneyTransferServiceImpl implements MoneyTransferService {

    @Inject
    private MoneyTransferRepository moneyTransferRepository;
    @Inject
    private CustomerRepository customerRepository;
    @Inject
    private AccountRepository accountRepository;
    @Inject
    private ExchangeRateRepository exchangeRateRepository;

    public GetTransactionsResponse getTransactions(GetTransactionsRequest request) {
        GetTransactionsResponse result = new GetTransactionsResponse();
        List<MoneyTransferTransaction> transactions;
        if (request.getTransactionId() != null) {
            transactions = Collections.singletonList(moneyTransferRepository.getTransaction(request.getTransactionId()));
        } else if (request.getAccountId() != null) {
            Long accountId = request.getAccountId();
            Account account = accountRepository.getAccount(accountId);
            if (account == null) {
                throw new IllegalArgumentException("Account with id " + accountId + " doesn't exists. Transaction not created");
            } else {
                transactions = moneyTransferRepository.getAccountTransactions(accountId, request.getStartFrom(), request.getOffset());
            }
        } else if (request.getCustomerId() != null) {
            Long customerId = request.getCustomerId();
            Customer customer = customerRepository.getCustomer(customerId);
            if (customer == null) {
                throw new IllegalArgumentException("Customer with id " + customerId + " doesn't exists. Transaction not created");
            } else {
                transactions = moneyTransferRepository.getCustomerTransactions(customerId, request.getStartFrom(), request.getOffset());
            }
        } else {
            transactions = moneyTransferRepository.getAllTransactions(request.getStartFrom(), request.getOffset());
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
            Account accountFrom = accountRepository.getAccount(accountFromId);
            if (accountFrom == null) {
                errMsg = "Account with id " + accountFromId + " doesn't exists. Transaction not created";
                log.error(errMsg + ":\n{}", request);
            } else {
                Long customerToId = request.getCustomerToId();
                Customer customerTo = customerRepository.getCustomerWithAccounts(customerToId);
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
                        Currency currencyFrom = accountFrom.getCurrency();
                        ExchangeRate exchangeRate = exchangeRateRepository.getAppropriateRate(transactionRegistered, currencyFrom, currencyTo);
                        if (exchangeRate == null) {
                            errMsg = "There is no appropriate exchange rate for " + currencyFrom + " -> " + currencyTo +
                                " that is valid for " + transactionRegistered + ". Transaction not created";
                            log.error(errMsg + ":\n{}", request);
                        } else {
                            float amountFrom = request.getAmountFrom();
                            float amountTo = amountFrom * exchangeRate.getRate();
                            String description;
                            MoneyTransferTransactionStatus status;
                            try {
                                boolean transactionSuccessful =
                                    accountRepository.moveMoneyBetweenAccounts(accountFromId, amountFrom, accountTo.getId(), amountTo);
                                if (transactionSuccessful) {
                                    description = accountFrom.getCustomer().getId().equals(customerToId)
                                        ? "transfer " + currencyFrom + " -> " + currencyTo + " between customer's accounts"
                                        : "transfer " + currencyFrom + " -> " + currencyTo + " to other client ("
                                        + customerTo.getShortName() + ")";
                                    status = SUCCESSFUL;
                                } else {
                                    description = accountFrom.getBalance() < amountFrom
                                        ? "Insufficient funds."
                                        : "Error during moving money between accounts";
                                    status = REJECTED;
                                }
                            } catch (Exception ex) {
                                description = "Exception during creating transaction";
                                status = REJECTED;
                                log.error(description + ":\n" + request, ex);
                            }
                            newTransaction =
                                new MoneyTransferTransaction(null, accountFrom, accountTo, currencyFrom, amountFrom,
                                    currencyTo, amountTo, exchangeRate, transactionRegistered, new Date(), status,
                                    description);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            errMsg = "Error during creating transaction: " + ex.getMessage();
            throw new MoneyTransferProcessingException(errMsg, ex);
        }

        MoneyTransferTransaction savedTransaction = null;
        if (newTransaction != null) {
            newTransaction.setFinalizationTimestamp(new Date());
            savedTransaction = moneyTransferRepository.saveTransaction(newTransaction);
        } else {
            MoneyTransferTransactionDto notSavedErrorResult = new MoneyTransferTransactionDto();
            notSavedErrorResult.setDescription(errMsg);
            return notSavedErrorResult;
        }

        return new MoneyTransferTransactionDto(savedTransaction);
    }
}
