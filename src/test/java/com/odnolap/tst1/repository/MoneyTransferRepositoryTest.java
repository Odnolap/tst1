package com.odnolap.tst1.repository;

import com.odnolap.tst1.unitcommon.BaseUnitTest;
import com.odnolap.tst1.model.MoneyTransferRequest;
import com.odnolap.tst1.model.NewTransactionRequest;
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
        List<MoneyTransferTransaction> transactions = repository.getAccountTransactions(1234L);
//        System.out.println("accId: " + transactions.iterator().next().getAccountFromId());
    }

    @Test
    public void getTransactionsByCustomerId() {
        List<MoneyTransferTransaction> transactions = repository.getCustomerTransactions(4567L);
//        System.out.println("description: " + transactions.iterator().next().getDescription());
    }

    @Test
    public void getTransactionsById() {
        List<MoneyTransferTransaction> transactions = repository.getTransaction(5555L);
//        System.out.println("transactionId: " + transactions.iterator().next().getId());
    }

    @Test
    public void createMoneyTransferTransaction() {
        MoneyTransferRequest request = new MoneyTransferRequest();
        MoneyTransferTransaction transacion = repository.createNewTransaction(new NewTransactionRequest(request));
//        System.out.println("status: " + transacion.getStatus());
    }

}
