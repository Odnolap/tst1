package com.odnolap.service;

import com.odnolap.model.db.MoneyTransferTransaction;

import java.util.List;

public interface MoneyTransferService {
    List<MoneyTransferTransaction> getAccountTransactions(Long accountId);
}
