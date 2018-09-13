package com.odnolap.tst1.repository;

import com.odnolap.tst1.model.db.Account;
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
import java.util.List;

@Singleton
@Slf4j
public class AccountRepositoryDb implements AccountRepository {

    @Inject
    private SessionFactory sessionFactory;

    @Override
    public Account getAccount(Long accountId) {
        log.trace("Getting account by id: {}", accountId);
        Session session = sessionFactory.openSession();
        Account customer = session.byId(Account.class).load(accountId);
        session.close();
        return customer;
    }

    @Override
    public List<Account> getAllAccounts(int startFrom, int offset) {
        log.trace("Getting all accounts startFrom={}, offset={}", startFrom, offset);
        Session session = sessionFactory.openSession();
        List<Account> customerList = session.createNamedQuery(Account.ALL, Account.class)
            .setFirstResult(startFrom)
            .setMaxResults(offset)
            .getResultList();
        session.close();
        return customerList;
    }

    @Override
    public boolean moveMoneyBetweenAccounts(Long accountFromId, BigDecimal amountFrom, Long accountToId, BigDecimal amountTo) {
        log.trace("Moving money (amounts {} -> {}) between accounts ({} -> {}) in DB.",
            amountFrom, amountTo, accountFromId, accountToId);
        Session session = sessionFactory.openSession();
        Transaction dbTransaction = null;
        try {
            dbTransaction = session.beginTransaction();
            // LOCK and CHECK
            Account accountFrom = session.get(Account.class, accountFromId, LockMode.PESSIMISTIC_WRITE);
            session.refresh(accountFrom);
            BigDecimal balanceFrom = accountFrom.getBalance();
            if (balanceFrom.compareTo(amountFrom) < 0) {
                dbTransaction.rollback();
                session.close();
                return false;
            }
            Account accountTo = session.get(Account.class, accountToId, LockMode.PESSIMISTIC_WRITE);
            session.refresh(accountTo);
            accountFrom.setBalance(balanceFrom.subtract(amountFrom));
            accountTo.setBalance(accountTo.getBalance().add(amountTo));
            session.saveOrUpdate(accountFrom);
            session.saveOrUpdate(accountTo);
            session.flush();
            dbTransaction.commit();
            session.close();
            return true;
        } catch (HibernateException ex) {
            if (dbTransaction != null) {
                dbTransaction.rollback();
            }
            session.close();
            throw new DbPersistenceException("Error during saving an account balance changing in DB.", ex);
        }
    }
}
