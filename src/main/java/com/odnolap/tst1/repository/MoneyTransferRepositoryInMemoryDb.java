package com.odnolap.tst1.repository;

import com.odnolap.tst1.helper.db.InMemoryDbHelper;
import com.odnolap.tst1.model.db.MoneyTransferTransaction;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import javax.inject.Singleton;
import java.util.List;

@Singleton
@Slf4j
public class MoneyTransferRepositoryInMemoryDb implements MoneyTransferRepository {

    private Session session = InMemoryDbHelper.getSession(); // It's used only 1 session for test purposes.

    @Override
    public MoneyTransferTransaction getTransaction(Long transactionId) {
        return session.byId(MoneyTransferTransaction.class).load(transactionId);
    }

    @Override
    public List<MoneyTransferTransaction> getAccountTransactions(Long accountId, int startFrom, int offset) {
        log.trace("Getting transactions for accountId {}, startFrom={}, offset={}", accountId, startFrom, offset);
        return session.createNamedQuery(MoneyTransferTransaction.BY_ACCT_ID, MoneyTransferTransaction.class)
            .setParameter("accountId", accountId)
            .setFirstResult(startFrom)
            .setMaxResults(offset)
            .getResultList();
    }

    @Override
    public List<MoneyTransferTransaction> getCustomerTransactions(Long customerId, int startFrom, int offset) {
        log.trace("Getting transactions for customerId {}, startFrom={}, offset={}", customerId, startFrom, offset);
        return session.createNamedQuery(MoneyTransferTransaction.BY_CUST_ID, MoneyTransferTransaction.class)
            .setParameter("customerId", customerId)
            .setFirstResult(startFrom)
            .setMaxResults(offset)
            .getResultList();
    }

    @Override
    public List<MoneyTransferTransaction> getAllTransactions(int startFrom, int offset) {
        log.trace("Getting all transactions startFrom={}, offset={}", startFrom, offset);
        return session.createNamedQuery(MoneyTransferTransaction.ALL, MoneyTransferTransaction.class)
            .setFirstResult(startFrom)
            .setMaxResults(offset)
            .getResultList();
    }

    public MoneyTransferTransaction saveTransaction(MoneyTransferTransaction transaction) {
        log.trace("Saving a transaction.\n {}", transaction);
        session.beginTransaction();
        Long transactionId = (Long)session.save("transactions", transaction);
        session.getTransaction().commit();
        return getTransaction(transactionId);
    }

}
