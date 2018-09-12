package com.odnolap.tst1.service;

import com.odnolap.tst1.model.GetExchangeRatesRequest;
import com.odnolap.tst1.model.GetExchangeRatesResponse;

public interface ExchangeRateService {

    GetExchangeRatesResponse getExchangeRates(GetExchangeRatesRequest request);
}
