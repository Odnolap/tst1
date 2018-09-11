package com.odnolap.tst1.service;

import com.odnolap.tst1.model.GetTransactionsRequest;
import com.odnolap.tst1.model.GetTransactionsResponse;
import com.odnolap.tst1.model.MoneyTransferRequest;
import com.odnolap.tst1.model.dto.MoneyTransferTransactionDto;

public interface MoneyTransferService {

    GetTransactionsResponse getTransactions(GetTransactionsRequest request);

    MoneyTransferTransactionDto createMoneyTransferTransaction(MoneyTransferRequest request);
}
