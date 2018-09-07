package com.odnolap.service;

import com.odnolap.model.GetTransactionRequest;
import com.odnolap.model.MoneyTransferRequest;
import com.odnolap.model.NewTransactionRequest;
import com.odnolap.model.db.MoneyTransferTransaction;
import com.odnolap.model.db.MoneyTransferTransactionStatus;

import java.util.List;

public interface MoneyTransferService {

    List<MoneyTransferTransaction> getTransactions(GetTransactionRequest request);

    MoneyTransferTransaction createMoneyTransferTransaction(NewTransactionRequest request);
}
