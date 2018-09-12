package com.odnolap.tst1.model;

import com.odnolap.tst1.model.db.ExchangeRate;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetExchangeRatesResponse {
    private List<ExchangeRate> exchangeRates;
}
