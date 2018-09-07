package com.odnolap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.odnolap.model.db.Currency;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MoneyTransferRequest {
    private Long accountFromId;
    private Long accountToId;
    private Currency currencyFrom;
    private Float amountFrom;
    private Currency currencyTo;
}
