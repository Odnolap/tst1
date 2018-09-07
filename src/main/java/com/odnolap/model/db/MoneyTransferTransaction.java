package com.odnolap.model.db;

import lombok.Data;

@Data
public class MoneyTransferTransaction {
    private Long id;
    private Long accountFromId;
    private Long accountToId;
    private Currency currencyFrom;
    private Float amountFrom;
    private Currency currencyTo;
    private Float amountTo;
    private ExchangeRate exchangeRate;
    private Long creationTimestamp;
    private Long finalizationTimestamp;
    private MoneyTransferTransactionStatus status;
    private String description;
}
