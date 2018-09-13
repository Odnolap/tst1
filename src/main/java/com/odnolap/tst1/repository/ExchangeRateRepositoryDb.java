package com.odnolap.tst1.repository;

import com.odnolap.tst1.model.db.Currency;
import com.odnolap.tst1.model.db.ExchangeRate;
import com.odnolap.tst1.model.exceptions.DbPersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Singleton
@Slf4j
public class ExchangeRateRepositoryDb implements ExchangeRateRepository {

    @Inject
    private SessionFactory sessionFactory;


    @Override
    public ExchangeRate getExchangeRate(Long exchangeRateId) {
        return getExchangeRate(exchangeRateId, null);
    }

    private ExchangeRate getExchangeRate(Long exchangeRateId, Session session) {
        log.trace("Getting exchange rate by id: {}", exchangeRateId);
        boolean isSessionNew = session == null;
        if (isSessionNew) {
            session = sessionFactory.openSession();
        }
        ExchangeRate exchangeRate = session.byId(ExchangeRate.class).load(exchangeRateId);
        if (isSessionNew) {
            session.close();
        }
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

    @Override
    public ExchangeRate insertExchangeRate(ExchangeRate rate) {
        log.trace("Inserting a new exchange rate in DB.\n {}", rate);
        Session session = sessionFactory.openSession();
        Transaction dbTransaction = null;
        try {
            dbTransaction = session.beginTransaction();
            Long rateId = (Long)session.save(rate);
            dbTransaction.commit();
            ExchangeRate savedRate = getExchangeRate(rateId, session);
            session.close();
            return savedRate;
        } catch (HibernateException ex) {
            if (dbTransaction != null) {
                dbTransaction.rollback();
            }
            session.close();
            throw new DbPersistenceException("Error during inserting a new exchange rate in DB.", ex);
        }
    }

    @Override
    public ExchangeRate updateOldAndInsertNewRates(ExchangeRate oldExchangeRate, ExchangeRate newExchangeRate) {
        log.trace("Inserting a new ExchangeRate in DB with changing validTo for old one.");
        Session session = sessionFactory.openSession();
        Transaction dbTransaction = null;
        try {
            dbTransaction = session.beginTransaction();
            session.saveOrUpdate(oldExchangeRate);
            Long rateId = (Long)session.save(newExchangeRate);
            dbTransaction.commit();
            ExchangeRate savedNewRate = getExchangeRate(rateId, session);
            session.close();
            return savedNewRate;
        } catch (HibernateException ex) {
            if (dbTransaction != null) {
                dbTransaction.rollback();
            }
            session.close();
            throw new DbPersistenceException("Error during updating and old and inserting a new exchange rate in DB.", ex);
        }
    }
}
