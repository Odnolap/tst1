package com.odnolap.tst1.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeRate {
    private Long id;
    private Currency currencyFrom;
    private Currency currencyTo;
    private Float rate;
    private Long validFromTimestamp;
    private Long validToTimestamp;
}
