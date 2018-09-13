package com.odnolap.tst1.repository;

import com.odnolap.tst1.model.db.Currency;
import com.odnolap.tst1.model.db.ExchangeRate;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;
import java.util.List;

@Singleton
@Slf4j
public class ExchangeRateRepositoryDb implements ExchangeRateRepository {

    @Inject
    private SessionFactory sessionFactory;


    @Override
    public ExchangeRate getExchangeRate(Long exchangeRateId) {
        log.trace("Getting exchange rate by id: {}", exchangeRateId);
        Session session = sessionFactory.openSession();
        ExchangeRate exchangeRate = session.byId(ExchangeRate.class).load(exchangeRateId);
        session.close();
        return exchangeRate;
    }

    @Override
    public ExchangeRate getAppropriateRate(Date date, Currency currencyFrom, Currency currencyTo) {
        log.trace("Getting the latest exchange {} -> {} rate that valid for {}", currencyFrom, currencyTo, date);
        Session session = sessionFactory.openSession();
        List<ExchangeRate> resultList = session.createNamedQuery(ExchangeRate.BY_CURRENCIES_AND_DATE, ExchangeRate.class)
            .setParameter("currencyFrom", currencyFrom)
            .setParameter("currencyTo", currencyTo)
            .setParameter("dt", date)
            .setMaxResults(1)
            .getResultList();
        ExchangeRate exchangeRateList = resultList.isEmpty() ? null : resultList.get(0);
        session.close();
        return exchangeRateList;
    }

    @Override
    public List<ExchangeRate> getAllExchangeRates(int startFrom, int offset) {
        log.trace("Getting all exchange rates startFrom={}, offset={}", startFrom, offset);
        Session session = sessionFactory.openSession();
        List<ExchangeRate> exchangeRateList = session.createNamedQuery(ExchangeRate.ALL, ExchangeRate.class)
            .setFirstResult(startFrom)
            .setMaxResults(offset)
            .getResultList();
        session.close();
        return exchangeRateList;
    }
}
