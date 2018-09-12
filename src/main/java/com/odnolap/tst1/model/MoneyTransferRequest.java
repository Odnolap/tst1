package com.odnolap.tst1.model;

import com.odnolap.tst1.model.db.Currency;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoneyTransferRequest {
    private Long accountFromId;
    private Long customerToId;
    private Float amountFrom;
    private Currency currencyTo;
}
