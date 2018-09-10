package com.odnolap.tst1.service;

import com.odnolap.tst1.unitcommon.BaseUnitTest;
import com.odnolap.tst1.model.GetTransactionsRequest;
import com.odnolap.tst1.model.GetTransactionsResponse;
import com.odnolap.tst1.model.MoneyTransferRequest;
import com.odnolap.tst1.model.NewTransactionRequest;
import com.odnolap.tst1.model.db.MoneyTransferTransaction;
import com.odnolap.tst1.model.db.MoneyTransferTransactionStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoneyTransferServiceTest extends BaseUnitTest {

    protected static MoneyTransferService service;

    @BeforeClass
    public static void initTests() {
        service = injector.getInstance(MoneyTransferService.class);
    }

    @Test
    public void getTransactionsByAccountId() {
        long expectedAccId = 123L;
        GetTransactionsRequest request = new GetTransactionsRequest();
        request.setAccountId(expectedAccId);
        GetTransactionsResponse transactions = service.getTransactions(request);
        assertEquals(expectedAccId, transactions.getTransactions().iterator().next().getAccountFromId().longValue());
    }

    @Test
    public void getTransactionsByCustomerId() {
        GetTransactionsRequest request = new GetTransactionsRequest();
        request.setCustomerId(100L);
        GetTransactionsResponse transactions = service.getTransactions(request);
        assertEquals(400L, transactions.getTransactions().iterator().next().getId().longValue());
    }

    @Test
    public void getTransactionsById() {
        long expectedTransactionId = 555L;
        GetTransactionsRequest request = new GetTransactionsRequest();
        request.setTransactionId(expectedTransactionId);
        GetTransactionsResponse transactions = service.getTransactions(request);
        assertEquals(expectedTransactionId, transactions.getTransactions().iterator().next().getId().longValue());
    }

    @Test
    public void createMoneyTransferTransaction() {
        MoneyTransferRequest request = new MoneyTransferRequest();
        MoneyTransferTransaction response = service.createMoneyTransferTransaction(new NewTransactionRequest(request));
        assertEquals(MoneyTransferTransactionStatus.SUCCESFUL, response.getStatus());
    }
}