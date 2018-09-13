package com.odnolap.tst1.repository;

import com.odnolap.tst1.model.db.MoneyTransferTransactionStatus;
import com.odnolap.tst1.common.BaseUnitTest;
import com.odnolap.tst1.model.db.MoneyTransferTransaction;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MoneyTransferRepositoryTest extends BaseUnitTest {

    protected static MoneyTransferRepository repository;

    @BeforeClass
    public static void initTests() {
        repository = injector.getInstance(MoneyTransferRepository.class);
    }

    @Test
    public void testGetAllTransactions() {
        List<MoneyTransferTransaction> allTransactions = repository.getAllTransactions(0, 10);
        assertThat(allTransactions.size(), greaterThanOrEqualTo(6)); // initial count of transactions
        assertThat(allTransactions.size(), lessThanOrEqualTo(10));
    }

    @Test
    public void testGetTransactionsByAccountId() {
        long expectedAccId = 11L;
        List<MoneyTransferTransaction> transactions = repository.getAccountTransactions(expectedAccId, 0, 10);
        assertThat(transactions.size(), greaterThan(0));
        assertThat(transactions.size(), lessThanOrEqualTo(10));
        for (MoneyTransferTransaction transaction: transactions) {
            assertTrue(transaction.getAccountFrom().getId() == expectedAccId
                || transaction.getAccountTo().getId() == expectedAccId);
        }
    }

    @Test
    public void testGetTransactionsByCustomerId() {
        long expectedCustomerId = 3L;
        List<MoneyTransferTransaction> transactions = repository.getCustomerTransactions(expectedCustomerId, 0, 10);
        assertThat(transactions.size(), greaterThan(0));
        assertThat(transactions.size(), lessThanOrEqualTo(10));
        for (MoneyTransferTransaction transaction: transactions) {
            assertTrue(transaction.getAccountFrom().getCustomer().getId() == expectedCustomerId
                || transaction.getAccountTo().getCustomer().getId() == expectedCustomerId);
        }
    }

    @Test
    public void testGetTransactionsById() {
        long expectedTransactionId = 1004L;
        MoneyTransferTransaction transaction = repository.getTransaction(expectedTransactionId);
        assertThat(transaction.getId(), is(expectedTransactionId));
    }

    @Test
    public void testCreateMoneyTransferTransaction() {
        MoneyTransferTransaction t = repository.getTransaction(1005L);
        BigDecimal newAmountFrom = t.getAmountFrom().multiply(BigDecimal.valueOf(2));
        BigDecimal newAmountTo = t.getAmountTo().multiply(BigDecimal.valueOf(2));
        String newDescription = "Transaction creation test";

        MoneyTransferTransaction newTransaction = new MoneyTransferTransaction(null, t.getAccountFrom(), t.getAccountTo(),
            t.getCurrencyFrom(), newAmountFrom, t.getCurrencyTo(), newAmountTo, t.getExchangeRate(), t.getCreationTimestamp(),
            new Date(), MoneyTransferTransactionStatus.SUCCESSFUL, newDescription);
        MoneyTransferTransaction savedTransacion = repository.saveTransaction(newTransaction);
        assertThat(savedTransacion.getId(), notNullValue());
        assertThat(savedTransacion.getAmountFrom(), is(newAmountFrom));
        assertThat(savedTransacion.getAmountTo(), is(newAmountTo));
        assertThat(savedTransacion.getDescription(), is(newDescription));
    }

}
