package com.odnolap.tst1.repository;

import com.odnolap.tst1.helper.db.DbHelper;
import com.odnolap.tst1.model.NewTransactionRequest;
import com.odnolap.tst1.model.db.MoneyTransferTransaction;
import com.odnolap.tst1.model.db.MoneyTransferTransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import javax.inject.Singleton;
import java.util.List;

@Singleton
@Slf4j
public class MoneyTransferRepositoryInMemoryDb implements MoneyTransferRepository {

    private Session session = DbHelper.getSession(); // It's used only 1 session for test purposes.

    @Override
    public MoneyTransferTransaction getTransaction(Long transactionId) {
        return session.byId(MoneyTransferTransaction.class).load(transactionId);
    }

    @Override
    public List<MoneyTransferTransaction> getAccountTransactions(Long accountId, int startFrom, int offset) {
        List<MoneyTransferTransaction> result = session.createNamedQuery(MoneyTransferTransaction.BY_ACCT_ID, MoneyTransferTransaction.class)
            .setParameter("accountId", accountId)
            .setFirstResult(startFrom)
            .setMaxResults(offset)
            .getResultList();

        return result;
    }

    @Override
    public List<MoneyTransferTransaction> getCustomerTransactions(Long customerId, int startFrom, int offset) {
        List<MoneyTransferTransaction> result = session.createNamedQuery(MoneyTransferTransaction.BY_CUST_ID, MoneyTransferTransaction.class)
            .setParameter("customerId", customerId)
            .setFirstResult(startFrom)
            .setMaxResults(offset)
            .getResultList();

        return result;
    }

    public MoneyTransferTransaction createNewTransaction(NewTransactionRequest request) {
        log.info("Creating a new transaction.\n {}", request); // TODO
        MoneyTransferTransaction result = new MoneyTransferTransaction();
        result.setStatus(MoneyTransferTransactionStatus.SUCCESSFUL);
        return result;
    }

    /*
    session.beginTransaction();

    ContactEntity contactEntity = new ContactEntity();

        contactEntity.setBirthDate(new java.util.Date());
        contactEntity.setFirstName("Nick");
        contactEntity.setLastName("VN");

        session.save(contactEntity);
        session.getTransaction().commit();*/

}
