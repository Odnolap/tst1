package com.odnolap.service;

import com.odnolap.model.GetTransactionRequest;
import com.odnolap.model.NewTransactionRequest;
import com.odnolap.model.db.MoneyTransferTransaction;
import com.odnolap.model.db.MoneyTransferTransactionStatus;
import com.odnolap.repository.MoneyTransferRepository;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;

@Singleton
@Slf4j
public class MoneyTransferServiceImpl implements MoneyTransferService {

    @Inject
    private MoneyTransferRepository repository;

    public List<MoneyTransferTransaction> getTransactions(GetTransactionRequest request) {
        if (request.getAccountId() != null) {
            return repository.getAccountTransactions(request.getAccountId());
        } else if (request.getCustomerId() != null) {
            return repository.getCustomerTransactions(request.getCustomerId());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public MoneyTransferTransaction createMoneyTransferTransaction(NewTransactionRequest request) {
        log.info("Creating a new transaction.\n {}", request); // TODO
        MoneyTransferTransaction result = new MoneyTransferTransaction();
        result.setStatus(MoneyTransferTransactionStatus.SUCCESFUL);
        return result;
    }
}
