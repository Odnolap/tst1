package com.odnolap.tst1.model;

import com.odnolap.tst1.model.db.Currency;
import lombok.Data;

@Data
public class NewTransactionRequest {
    private Long accountFromId;
    private Long customerToId;
    private Currency currencyFrom;
    private Float amountFrom;
    private Currency currencyTo;
    private Long creationTimestamp;

    public NewTransactionRequest(MoneyTransferRequest moneyTransferRequest) {
        accountFromId = moneyTransferRequest.getAccountFromId();
        customerToId = moneyTransferRequest.getCustomerToId();
        currencyFrom = moneyTransferRequest.getCurrencyFrom();
        amountFrom = moneyTransferRequest.getAmountFrom();
        currencyTo = moneyTransferRequest.getCurrencyTo();
        creationTimestamp = System.currentTimeMillis();
    }
}
