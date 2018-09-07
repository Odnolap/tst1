package com.odnolap.repository;

import com.odnolap.model.db.MoneyTransferTransaction;

import java.util.List;

public interface MoneyTransferRepository {

    List<MoneyTransferTransaction> getTransaction(Long transactionId);

    List<MoneyTransferTransaction> getAccountTransactions(Long accountId);

    List<MoneyTransferTransaction> getCustomerTransactions(Long customerId);

}
