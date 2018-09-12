package com.odnolap.tst1.repository;

import com.odnolap.tst1.model.db.Currency;
import com.odnolap.tst1.model.db.ExchangeRate;

import java.util.Date;
import java.util.List;

public interface ExchangeRateRepository {
    ExchangeRate getExchangeRate(Long exchangeRateId);

    ExchangeRate getAppropriateRate(Date date, Currency currencyFrom, Currency currencyTo);

    List<ExchangeRate> getAllExchangeRates(int startFrom, int offset);
}
