package com.odnolap.tst1.service;

import com.odnolap.tst1.model.GetTransactionsRequest;
import com.odnolap.tst1.model.GetTransactionsResponse;
import com.odnolap.tst1.model.NewTransactionRequest;
import com.odnolap.tst1.model.db.MoneyTransferTransaction;

public interface MoneyTransferService {

    GetTransactionsResponse getTransactions(GetTransactionsRequest request);

    MoneyTransferTransaction createMoneyTransferTransaction(NewTransactionRequest request);
}
