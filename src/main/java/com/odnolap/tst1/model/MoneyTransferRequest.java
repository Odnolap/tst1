package com.odnolap.tst1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.odnolap.tst1.model.db.Currency;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MoneyTransferRequest {
    private Long accountFromId;
    private Long customerToId;
    private Currency currencyFrom;
    private Float amountFrom;
    private Currency currencyTo;
}
