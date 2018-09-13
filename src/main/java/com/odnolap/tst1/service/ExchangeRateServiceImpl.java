package com.odnolap.tst1.service;

import com.odnolap.tst1.model.GetExchangeRatesResponse;
import com.odnolap.tst1.model.GetExchangeRatesRequest;
import com.odnolap.tst1.model.NewExchangeRateRequest;
import com.odnolap.tst1.model.db.Currency;
import com.odnolap.tst1.model.db.ExchangeRate;
import com.odnolap.tst1.model.exceptions.RequestParsingException;
import com.odnolap.tst1.repository.ExchangeRateRepository;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
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

    @Override
    public ExchangeRate createExchangeRate(NewExchangeRateRequest request) {
        Currency currencyFrom = request.getCurrencyFrom();
        Currency currencyTo = request.getCurrencyTo();
        BigDecimal newRate = request.getRate();
        Date newValidFrom = request.getValidFrom();
        Date newValidTo = request.getValidTo();
        if (currencyFrom == null) {
            throw new RequestParsingException("Invalid request: \"currencyFrom\" must not be null.");
        } else if (currencyTo == null) {
            throw new RequestParsingException("Invalid request: \"currencyTo\" must not be null.");
        } else if (newRate == null) {
            throw new RequestParsingException("Invalid request: \"rate\" must not be null.");
        } else if (newRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RequestParsingException("Invalid request: \"rate\" must not be greater than 0 (it's "
                + newRate + ").");
        } else if (newValidFrom != null && newValidFrom.before(new Date())) {
            throw new RequestParsingException("Invalid request: \"validFrom\" must be greater or equal to the current moment (it's "
                + newValidFrom + ").");
        } else if (newValidTo != null && newValidFrom != null && !newValidTo.after(newValidFrom)) {
            throw new RequestParsingException("Invalid request: \"validTo\" must be null or greater than validFrom (validFrom is "
                + newValidFrom + " and validTo is " + newValidTo + ")");
        } else if (newValidTo != null && newValidFrom == null && !newValidTo.after(new Date())) {
            throw new RequestParsingException("Invalid request: \"validTo\" must be null or greater than NOW() (validTo is " + newValidTo + ")");
        }

        if (newValidFrom == null) {
            newValidFrom = new Date();
        }
        // Change validTo for current valid rate (if it exists) to newValidFrom
        ExchangeRate oldExchangeRate = exchangeRateRepository.getAppropriateRate(newValidFrom, currencyFrom, currencyTo);
        if (oldExchangeRate != null) {
            oldExchangeRate.setValidTo(newValidFrom);
        }
        // When it is not test application, then we also need to get rates that have validFrom < newValidTo && validTo > newValidFrom considering null values and make their periods don't overlap
        ExchangeRate newExchangeRate = new ExchangeRate(null, currencyFrom, currencyTo,
            newRate, newValidFrom, newValidTo);
        ExchangeRate savedExchangeRate;
        if (oldExchangeRate == null) {
            savedExchangeRate = exchangeRateRepository.insertExchangeRate(newExchangeRate);
        } else {
            savedExchangeRate = exchangeRateRepository.updateOldAndInsertNewRates(oldExchangeRate, newExchangeRate);
        }

        return savedExchangeRate;
    }
}
