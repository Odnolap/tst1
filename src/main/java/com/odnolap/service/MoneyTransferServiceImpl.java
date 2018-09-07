package com.odnolap.service;

import com.odnolap.model.GetTransactionsRequest;
import com.odnolap.model.GetTransactionsResponse;
import com.odnolap.model.NewTransactionRequest;
import com.odnolap.model.db.MoneyTransferTransaction;
import com.odnolap.model.db.MoneyTransferTransactionStatus;
import com.odnolap.repository.MoneyTransferRepository;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;

@Singleton
@Slf4j
public class MoneyTransferServiceImpl implements MoneyTransferService {

    @Inject
    private MoneyTransferRepository repository;

    public GetTransactionsResponse getTransactions(GetTransactionsRequest request) {
        GetTransactionsResponse result = new GetTransactionsResponse();
        if (request.getTransactionId() != null) {
            result.setTransactions(repository.getTransaction(request.getTransactionId()));
        } else if (request.getAccountId() != null) {
            result.setTransactions(repository.getAccountTransactions(request.getAccountId()));
        } else if (request.getCustomerId() != null) {
            result.setTransactions(repository.getCustomerTransactions(request.getCustomerId()));
        } else {
            result.setTransactions(Collections.emptyList());
        }
        return result;
    }

    @Override
    public MoneyTransferTransaction createMoneyTransferTransaction(NewTransactionRequest request) {
        log.info("Creating a new transaction.\n {}", request); // TODO
        MoneyTransferTransaction result = new MoneyTransferTransaction();
        result.setStatus(MoneyTransferTransactionStatus.SUCCESFUL);
        return result;
    }
}
