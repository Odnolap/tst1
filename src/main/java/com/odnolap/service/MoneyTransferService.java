package com.odnolap.service;

import com.odnolap.model.GetTransactionsRequest;
import com.odnolap.model.GetTransactionsResponse;
import com.odnolap.model.NewTransactionRequest;
import com.odnolap.model.db.MoneyTransferTransaction;

public interface MoneyTransferService {

    GetTransactionsResponse getTransactions(GetTransactionsRequest request);

    MoneyTransferTransaction createMoneyTransferTransaction(NewTransactionRequest request);
}
