package com.odnolap.tst1.repository;

import com.odnolap.tst1.model.NewTransactionRequest;
import com.odnolap.tst1.model.db.MoneyTransferTransaction;

import java.util.List;

public interface MoneyTransferRepository {

    List<MoneyTransferTransaction> getTransaction(Long transactionId);

    List<MoneyTransferTransaction> getAccountTransactions(Long accountId);

    List<MoneyTransferTransaction> getCustomerTransactions(Long customerId);

    MoneyTransferTransaction createNewTransaction(NewTransactionRequest request);

}
