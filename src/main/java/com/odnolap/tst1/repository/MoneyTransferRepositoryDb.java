package com.odnolap.tst1.repository;

import com.odnolap.tst1.model.db.MoneyTransferTransaction;
import com.odnolap.tst1.model.exceptions.DbPersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
@Slf4j
public class MoneyTransferRepositoryDb implements MoneyTransferRepository {

    @Inject
    private SessionFactory sessionFactory;

    @Override
    public MoneyTransferTransaction getTransaction(Long transactionId) {
        return getTransaction(transactionId, null);
    }

    private MoneyTransferTransaction getTransaction(Long transactionId, Session session) {
        log.trace("Getting transaction by id: {}", transactionId);
        boolean isSessionNew = session == null;
        if (isSessionNew) {
            session = sessionFactory.openSession();
        }
        MoneyTransferTransaction transaction = session.byId(MoneyTransferTransaction.class).load(transactionId);
        if (isSessionNew) {
            session.close();
        }
        return transaction;
    }

    @Override
    public List<MoneyTransferTransaction> getAccountTransactions(Long accountId, int startFrom, int offset) {
        log.trace("Getting transactions for accountId {}, startFrom={}, offset={}", accountId, startFrom, offset);
        Session session = sessionFactory.openSession();
        List<MoneyTransferTransaction> transactionList = session.createNamedQuery(MoneyTransferTransaction.BY_ACCT_ID, MoneyTransferTransaction.class)
            .setParameter("accountId", accountId)
            .setFirstResult(startFrom)
            .setMaxResults(offset)
            .getResultList();
        session.close();
        return transactionList;
    }

    @Override
    public List<MoneyTransferTransaction> getCustomerTransactions(Long customerId, int startFrom, int offset) {
        log.trace("Getting transactions for customerId {}, startFrom={}, offset={}", customerId, startFrom, offset);
        Session session = sessionFactory.openSession();
        List<MoneyTransferTransaction> transactionList = session.createNamedQuery(MoneyTransferTransaction.BY_CUST_ID, MoneyTransferTransaction.class)
            .setParameter("customerId", customerId)
            .setFirstResult(startFrom)
            .setMaxResults(offset)
            .getResultList();
        session.close();
        return transactionList;
    }

    @Override
    public List<MoneyTransferTransaction> getAllTransactions(int startFrom, int offset) {
        log.trace("Getting all transactions startFrom={}, offset={}", startFrom, offset);
        Session session = sessionFactory.openSession();
        List<MoneyTransferTransaction> transactionList = session.createNamedQuery(MoneyTransferTransaction.ALL, MoneyTransferTransaction.class)
            .setFirstResult(startFrom)
            .setMaxResults(offset)
            .getResultList();
        session.close();
        return transactionList;
    }

    public MoneyTransferTransaction saveTransaction(MoneyTransferTransaction transaction) {
        log.trace("Saving a transaction in DB.\n {}", transaction);
        Session session = sessionFactory.openSession();
        Transaction dbTransaction = null;
        Long transactionId;
        try {
            dbTransaction = session.beginTransaction();
            transactionId = (Long)session.save(transaction);
            dbTransaction.commit();
        } catch (HibernateException ex) {
            if (dbTransaction != null) {
                dbTransaction.rollback();
            }
            session.close();
            throw new DbPersistenceException("Error during saving a new transaction to DB.", ex);
        }
        MoneyTransferTransaction newTransaction = getTransaction(transactionId, session);
        session.close();
        return newTransaction;
    }

}
