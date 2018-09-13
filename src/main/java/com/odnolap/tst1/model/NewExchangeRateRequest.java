package com.odnolap.tst1.model;

import com.odnolap.tst1.model.db.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewExchangeRateRequest {
    private Currency currencyFrom;
    private Currency currencyTo;
    private BigDecimal rate;
    private Date validFrom;
    private Date validTo;
}
