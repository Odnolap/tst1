package com.odnolap.tst1.service;

import com.odnolap.tst1.model.GetTransactionsRequest;
import com.odnolap.tst1.model.GetTransactionsResponse;
import com.odnolap.tst1.model.NewTransactionRequest;
import com.odnolap.tst1.model.db.MoneyTransferTransaction;
import com.odnolap.tst1.model.db.MoneyTransferTransactionStatus;
import com.odnolap.tst1.repository.MoneyTransferRepository;
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
        MoneyTransferTransaction result = repository.createNewTransaction(request);
        // TODO: checks, getting accountId
        return result;
    }
}
