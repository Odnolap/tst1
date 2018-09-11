package com.odnolap.tst1.model.dto;

import com.odnolap.tst1.model.db.Currency;
import com.odnolap.tst1.model.db.ExchangeRate;
import com.odnolap.tst1.model.db.MoneyTransferTransaction;
import com.odnolap.tst1.model.db.MoneyTransferTransactionStatus;
import lombok.Data;

import java.util.Date;

@Data
public class MoneyTransferTransactionDto {
    private Long id;
    private Long customerFromId;
    private Long accountFromId;
    private Long customerToId;
    private Long accountToId;
    private Currency currencyFrom;
    private Float amountFrom;
    private Currency currencyTo;
    private Float amountTo;
    private Float rate;
    private Date creationTimestamp = new Date();
    private Date finalizationTimestamp;
    private MoneyTransferTransactionStatus status = MoneyTransferTransactionStatus.CREATED;
    private String description;

    public MoneyTransferTransactionDto(MoneyTransferTransaction transaction) {
        this.id = transaction.getId();
        this.customerFromId = transaction.getAccountFrom().getCustomer().getId();
        this.accountFromId = transaction.getAccountFrom().getId();
        this.customerToId = transaction.getAccountTo().getCustomer().getId();
        this.accountToId = transaction.getAccountTo().getId();
        this.currencyFrom = transaction.getCurrencyFrom();
        this.amountFrom = transaction.getAmountFrom();
        this.currencyTo = transaction.getCurrencyTo();
        this.amountTo = transaction.getAmountTo();
        this.rate = transaction.getExchangeRate().getRate();
        this.creationTimestamp = transaction.getCreationTimestamp();
        this.finalizationTimestamp = transaction.getFinalizationTimestamp();
        this.status = transaction.getStatus();
        this.description = transaction.getDescription();
    }
}
