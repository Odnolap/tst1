package com.odnolap.tst1.service;

import com.odnolap.tst1.model.GetExchangeRatesRequest;
import com.odnolap.tst1.model.GetExchangeRatesResponse;
import com.odnolap.tst1.model.NewExchangeRateRequest;
import com.odnolap.tst1.model.db.ExchangeRate;

public interface ExchangeRateService {

    GetExchangeRatesResponse getExchangeRates(GetExchangeRatesRequest request);

    ExchangeRate createExchangeRate(NewExchangeRateRequest request);
}
