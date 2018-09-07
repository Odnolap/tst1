package com.odnolap.repository;

import com.odnolap.model.db.MoneyTransferTransaction;

import java.util.List;

public interface MoneyTransferRepository {
    List<MoneyTransferTransaction> getAccountTransactions(Long accountId);

}
