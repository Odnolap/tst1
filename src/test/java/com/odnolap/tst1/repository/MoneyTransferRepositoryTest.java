package com.odnolap.tst1.repository;

import com.odnolap.tst1.unitcommon.BaseUnitTest;
import com.odnolap.tst1.model.db.MoneyTransferTransaction;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class MoneyTransferRepositoryTest extends BaseUnitTest {

    protected static MoneyTransferRepository repository;

    @BeforeClass
    public static void initTests() {
        repository = injector.getInstance(MoneyTransferRepository.class);
    }

    @Test
    public void getTransactionsByAccountId() {
        List<MoneyTransferTransaction> transactions = repository.getAccountTransactions(1234L, 0, 10);
//        System.out.println("accId: " + transactions.iterator().next().getAccountFromId());
    }

    @Test
    public void getTransactionsByCustomerId() {
        List<MoneyTransferTransaction> transactions = repository.getCustomerTransactions(4567L, 0, 10);
//        System.out.println("description: " + transactions.iterator().next().getDescription());
    }

    @Test
    public void getTransactionsById() {
        MoneyTransferTransaction transaction = repository.getTransaction(1004L);
//        System.out.println("transactionId: " + transactions.iterator().next().getId());
    }

/*
    @Test
    public void createMoneyTransferTransaction() {
        MoneyTransferRequest request = new MoneyTransferRequest();
        MoneyTransferTransaction transacion = repository.saveTransaction(request);
//        System.out.println("status: " + transacion.getStatus());
    }
*/

}
