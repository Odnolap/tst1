package com.odnolap.tst1.model;

import com.odnolap.tst1.model.db.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransferRequest {
    private Long accountFromId;
    private Long customerToId;
    private BigDecimal amountFrom;
    private Currency currencyTo;
}
