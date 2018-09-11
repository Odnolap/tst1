package com.odnolap.tst1.repository;

import com.odnolap.tst1.model.NewTransactionRequest;
import com.odnolap.tst1.model.db.MoneyTransferTransaction;

import java.io.Closeable;
import java.util.List;

public interface MoneyTransferRepository {

    MoneyTransferTransaction getTransaction(Long transactionId);

    List<MoneyTransferTransaction> getAccountTransactions(Long accountId, int startFrom, int offset);

    List<MoneyTransferTransaction> getCustomerTransactions(Long customerId, int startFrom, int offset);

    MoneyTransferTransaction createNewTransaction(NewTransactionRequest request);

}
