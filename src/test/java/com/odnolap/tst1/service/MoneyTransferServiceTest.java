package com.odnolap.tst1.service;

import com.odnolap.tst1.model.db.Currency;
import com.odnolap.tst1.model.dto.MoneyTransferTransactionDto;
import com.odnolap.tst1.common.BaseUnitTest;
import com.odnolap.tst1.model.GetTransactionsRequest;
import com.odnolap.tst1.model.NewMoneyTransferRequest;
import com.odnolap.tst1.model.db.MoneyTransferTransactionStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.*;

public class MoneyTransferServiceTest extends BaseUnitTest {

    protected static MoneyTransferService service;

    @BeforeClass
    public static void initTests() {
        service = injector.getInstance(MoneyTransferService.class);
    }

    @Test
    public void testGettingAllTransactionsWithPagination() {
        GetTransactionsRequest request = new GetTransactionsRequest();
        request.setOffset(10);
        List<MoneyTransferTransactionDto> actualTransactionList = service.getTransactions(request).getTransactions();
        assertThat(actualTransactionList.size(), greaterThanOrEqualTo(6)); // initial count of transactions
        assertThat(actualTransactionList.size(), lessThanOrEqualTo(10));

        request.setOffset(5);
        actualTransactionList = service.getTransactions(request).getTransactions();
        assertThat(actualTransactionList.size(), is(5));

        request.setOffset(2);
        request.setStartFrom(2);
        actualTransactionList = service.getTransactions(request).getTransactions();
        assertThat(actualTransactionList.size(), is(2));

        request.setOffset(10);
        request.setStartFrom(200);
        actualTransactionList = service.getTransactions(request).getTransactions();
        assertThat(actualTransactionList.size(), is(0));
    }

    @Test
    public void testGettingTransactionsByAccountId() {
        long expectedAccId = 11L;
        GetTransactionsRequest request = new GetTransactionsRequest();
        request.setAccountId(expectedAccId);
        request.setOffset(10);
        List<MoneyTransferTransactionDto> actualTransactionList = service.getTransactions(request).getTransactions();
        assertThat(actualTransactionList.size(), greaterThan(0));
        assertThat(actualTransactionList.size(), lessThanOrEqualTo(10));
        for (MoneyTransferTransactionDto transaction: actualTransactionList) {
            assertTrue(transaction.getAccountFromId() == expectedAccId
                || transaction.getAccountToId() == expectedAccId);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGettingTransactionsByNonExistingAccountId() {
        long expectedAccId = 999999L;
        GetTransactionsRequest request = new GetTransactionsRequest();
        request.setAccountId(expectedAccId);
        request.setOffset(10);
        service.getTransactions(request);
    }

    @Test
    public void testGettingTransactionsByCustomerId() {
        long expectedCustomerId = 3L;
        GetTransactionsRequest request = new GetTransactionsRequest();
        request.setCustomerId(expectedCustomerId);
        request.setOffset(10);
        List<MoneyTransferTransactionDto> actualTransactionList = service.getTransactions(request).getTransactions();
        assertThat(actualTransactionList.size(), greaterThan(0));
        assertThat(actualTransactionList.size(), lessThanOrEqualTo(10));
        for (MoneyTransferTransactionDto transaction: actualTransactionList) {
            assertTrue(transaction.getCustomerFromId() == expectedCustomerId
                || transaction.getCustomerToId() == expectedCustomerId);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGettingTransactionsByNonExistingCustomerId() {
        long expectedCustomerId = 999999L;
        GetTransactionsRequest request = new GetTransactionsRequest();
        request.setCustomerId(expectedCustomerId);
        request.setOffset(10);
        service.getTransactions(request);
    }

    @Test
    public void testGettingTransactionsById() {
        long expectedTransactionId = 1004L;
        GetTransactionsRequest request = new GetTransactionsRequest();
        request.setTransactionId(expectedTransactionId);
        request.setOffset(10);
        List<MoneyTransferTransactionDto> actualTransactionList = service.getTransactions(request).getTransactions();
        assertThat(actualTransactionList.size(), lessThanOrEqualTo(1));
        assertThat(actualTransactionList.get(0).getId(), is(expectedTransactionId));
    }

    @Test
    public void testCreatingMoneyTransferTransaction() {
        long expectedAccountFromId = 11L;
        long expectedCustomerToId = 2L;
        BigDecimal expectedAmountFrom = new BigDecimal(1000);
        Currency expectedCurrencyTo = Currency.USD;
        NewMoneyTransferRequest request = new NewMoneyTransferRequest(expectedAccountFromId, expectedCustomerToId, expectedAmountFrom, expectedCurrencyTo);
        MoneyTransferTransactionDto response = service.createMoneyTransferTransaction(request);
        assertThat(response.getStatus(), is(MoneyTransferTransactionStatus.SUCCESSFUL));
        assertThat(response.getAccountFromId(), is(expectedAccountFromId));
        assertThat(response.getCustomerToId(), is(expectedCustomerToId));
        assertThat(response.getAmountFrom(), is(expectedAmountFrom));
        assertThat(response.getCurrencyTo(), is(expectedCurrencyTo));
        assertThat(response.getId(), notNullValue());
    }
}