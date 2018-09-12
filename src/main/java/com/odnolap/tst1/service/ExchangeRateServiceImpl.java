package com.odnolap.tst1.service;

import com.odnolap.tst1.model.GetExchangeRatesResponse;
import com.odnolap.tst1.model.GetExchangeRatesRequest;
import com.odnolap.tst1.model.db.ExchangeRate;
import com.odnolap.tst1.repository.ExchangeRateRepository;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Singleton
@Slf4j
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Inject
    private ExchangeRateRepository exchangeRateRepository;
    
    @Override
    public GetExchangeRatesResponse getExchangeRates(GetExchangeRatesRequest request) {
        GetExchangeRatesResponse result = new GetExchangeRatesResponse();
        List<ExchangeRate> exchangeRates;
        if (request.getExchangeRateId() != null) {
            exchangeRates = Collections.singletonList(exchangeRateRepository.getExchangeRate(request.getExchangeRateId()));
        } else {
            exchangeRates = exchangeRateRepository.getAllExchangeRates(request.getStartFrom(), request.getOffset());
        }

        exchangeRates.removeIf(Objects::isNull);
        if (exchangeRates.isEmpty()) {
            log.warn("Exchange rate list for get exchange rates request is empty:\n{}", request);
        }
        result.setExchangeRates(exchangeRates);

        return result;
    }
}
