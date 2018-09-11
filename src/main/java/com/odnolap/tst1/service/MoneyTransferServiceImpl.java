package com.odnolap.tst1.service;

import com.odnolap.tst1.model.GetTransactionsRequest;
import com.odnolap.tst1.model.GetTransactionsResponse;
import com.odnolap.tst1.model.NewTransactionRequest;
import com.odnolap.tst1.model.db.MoneyTransferTransaction;
import com.odnolap.tst1.model.db.MoneyTransferTransactionStatus;
import com.odnolap.tst1.model.dto.MoneyTransferTransactionDto;
import com.odnolap.tst1.repository.MoneyTransferRepository;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Slf4j
public class MoneyTransferServiceImpl implements MoneyTransferService {

    @Inject
    private MoneyTransferRepository repository;

    public GetTransactionsResponse getTransactions(GetTransactionsRequest request) {
        GetTransactionsResponse result = new GetTransactionsResponse();
        if (request.getTransactionId() != null) {
            MoneyTransferTransaction transaction = repository.getTransaction(request.getTransactionId());
            result.setTransactions(Collections.singletonList(new MoneyTransferTransactionDto(transaction)));
        } else if (request.getAccountId() != null) {
            List<MoneyTransferTransactionDto> resultTransactions =
                repository.getAccountTransactions(request.getAccountId(), request.getStartFrom(), request.getOffset()).stream()
                    .map(MoneyTransferTransactionDto::new)
                    .collect(Collectors.toList());
            result.setTransactions(resultTransactions);
        } else if (request.getCustomerId() != null) {
            List<MoneyTransferTransactionDto> resultTransactions =
                repository.getCustomerTransactions(request.getCustomerId(), request.getStartFrom(), request.getOffset()).stream()
                    .map(MoneyTransferTransactionDto::new)
                    .collect(Collectors.toList());
            result.setTransactions(resultTransactions);
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
