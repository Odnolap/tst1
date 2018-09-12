package com.odnolap.tst1.model;

import com.odnolap.tst1.model.db.ExchangeRate;
import lombok.Data;

import java.util.List;

@Data
public class GetExchangeRatesResponse {
    private List<ExchangeRate> exchangeRates;
}
