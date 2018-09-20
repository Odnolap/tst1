package com.odnolap.tst1.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odnolap.tst1.common.BaseIntegrationTest;
import com.odnolap.tst1.model.NewExchangeRateRequest;
import com.odnolap.tst1.model.NewMoneyTransferRequest;
import com.odnolap.tst1.model.db.Currency;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Calendar;

import static com.odnolap.tst1.helper.undertow.RequestHelper.ACCOUNT_ID_PARAM;
import static com.odnolap.tst1.helper.undertow.RequestHelper.CUSTOMER_ID_PARAM;
import static com.odnolap.tst1.helper.undertow.RequestHelper.ID;
import static com.odnolap.tst1.helper.undertow.RequestHelper.MAPPER;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.stringContainsInOrder;

public class Tst1IntegrationTest extends BaseIntegrationTest {

    private static final MathContext MC = new MathContext(23);

    @Test
    public void testGettingAllTransactionsWithLogingResponse() throws JsonProcessingException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.SEPTEMBER, 11, 1, 20, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        String expectedCreationTimestamp = MAPPER.writeValueAsString(calendar).replace("\"", "");

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2018, Calendar.SEPTEMBER, 11, 1, 20, 1);
        calendar2.set(Calendar.MILLISECOND, 0);
        String expectedFinalizationTimestamp = MAPPER.writeValueAsString(calendar2).replace("\"", "");

        client.logResponse().transactionGet()
            .get()
            .then()
            .body("transactions.id", hasItems(1001, 1002, 1003, 1004, 1005, 1006))
            .body("transactions.find { it.id == 1006 }.customerFromId", is(1))
            .body("transactions.find { it.id == 1006 }.customerToId", is(2))
            .body("transactions.find { it.id == 1006 }.accountFromId", is(11))
            .body("transactions.find { it.id == 1006 }.accountToId", is(14))
            .body("transactions.find { it.id == 1006 }.amountFrom", is(352348191.53f))
            .body("transactions.find { it.id == 1006 }.amountTo", is(352348191.53f))
            .body("transactions.find { it.id == 1006 }.currencyFrom", is("USD"))
            .body("transactions.find { it.id == 1006 }.currencyTo", is("USD"))
            .body("transactions.find { it.id == 1006 }.rate", is(1f))
            .body("transactions.find { it.id == 1006 }.creationTimestamp", is(expectedCreationTimestamp))
            .body("transactions.find { it.id == 1006 }.finalizationTimestamp.toString()", is(expectedFinalizationTimestamp))
            .body("transactions.find { it.id == 1006 }.status", is("SUCCESSFUL"))
            .body("transactions.find { it.id == 1006 }.description", is("transfer USD -> USD to other client (Melinda G.)"))
            ;
    }

    @Test
    public void testGettingAllTransactionsWithPagination() {
        // After initialization of DB there are 6 transactions
        client.transactionWithoutPaginationGet()
            .queryParam("page", 1)
            .queryParam("offset", 3)
            .get()
            .then()
            .body("transactions", hasSize(3))
        ;

        client.transactionWithoutPaginationGet()
            .queryParam("page", 0)
            .queryParam("offset", 8)
            .get()
            .then()
            .body("transactions", hasSize(greaterThanOrEqualTo(6)))
            .body("transactions", hasSize(lessThanOrEqualTo(8)))
        ;

        client.transactionWithoutPaginationGet()
            .queryParam("page", 10)
            .queryParam("offset", 5)
            .get()
            .then()
            .body("transactions", hasSize(0))
        ;
    }

    @Test
    public void testGettingTransactionsByAccountId() {
        client.transactionGet()
            .queryParam("accountId", 11)
            .get()
            .then()
            .body("transactions.id", hasItems(1001, 1004, 1005, 1006))
            .body("transactions.id", not(hasItems(1002, 1003)))
            ;
    }

    @Test
    public void testGettingTransactionsByIncorrectAccountId() {
        client.transactionGet()
            .queryParam("accountId", "abcd")
            .get()
            .then()
            .body("errorCode", is(400))
            .body("errorMessage", stringContainsInOrder(
                asList("Parameter", ACCOUNT_ID_PARAM, "has invalid integer format or it's too long:")))
        ;
    }

    @Test
    public void testGettingTransactionsByNonExistingAccountId() {
        client.transactionGet()
            .queryParam("accountId", 99999)
            .get()
            .then()
            .body("errorCode", is(400))
            .body("errorMessage", stringContainsInOrder(
                asList("Account with id ", " doesn't exist. Transaction wasn't created")))
            ;
    }

    @Test
    public void testGettingTransactionsByCustomerId() {
        client.transactionGet()
            .queryParam("customerId", 2)
            .get()
            .then()
            .body("transactions.id", hasItems(1002, 1006))
            .body("transactions.id", not(hasItems(1001, 1003, 1004, 1005)))
            ;
    }

    @Test
    public void testGettingTransactionsByIncorrectCustomerId() {
        client.transactionGet()
            .queryParam("customerId", 10.928)
            .get()
            .then()
            .body("errorCode", is(400))
            .body("errorMessage", stringContainsInOrder(
                asList("Parameter", CUSTOMER_ID_PARAM, "has invalid integer format or it's too long:")))
        ;
    }

    @Test
    public void testGettingTransactionsByNonExistingCustomerId() {
        client.transactionGet()
            .queryParam("customerId", 99999)
            .get()
            .then()
            .body("errorCode", is(400))
            .body("errorMessage", stringContainsInOrder(
                asList("Customer with id", "doesn't exist. Transaction wasn't created")))
        ;
    }

    @Test
    public void testGettingTransactionsById() {
        client.transactionGet("1005")
            .get()
            .then()
            .body("transactions", hasSize(1))
            .body("transactions.id", contains(1005))
            ;
    }

    @Test
    public void testGettingTransactionsByIncorrectId() {
        client.transactionGet("qwerty")
            .get()
            .then()
            .body("errorCode", is(400))
            .body("errorMessage", stringContainsInOrder(
                asList("URL path", ID, "has invalid integer format or it's too long")))
            ;
    }

    @Test
    public void testCreatingNewTransactionWithLoggingResponse() throws JsonProcessingException {
        Long expectedAccountFromId = 11L; // Account with USD
        Long expectedCustomerToId = 2L;
        Currency expectedCurrencyFrom = Currency.USD;
        Currency expectedCurrencyTo = Currency.USD;
        BigDecimal expectedAmountFrom = new BigDecimal(1000.12345, MC);
        NewMoneyTransferRequest requestBody =
            new NewMoneyTransferRequest(expectedAccountFromId, expectedCustomerToId, expectedAmountFrom, expectedCurrencyTo);
        client.logResponse().createTransactionRequest(requestBody)
            .post()
            .then()
            .body("id", notNullValue())
            .body("accountFromId", is(expectedAccountFromId.intValue()))
            .body("customerToId", is(expectedCustomerToId.intValue()))
            .body("currencyFrom", is(expectedCurrencyFrom.toString()))
            .body("currencyTo", is(expectedCurrencyTo.toString()))
            .body("amountFrom", is(expectedAmountFrom.floatValue()))
            .body("description", stringContainsInOrder(
                singletonList("transfer " + expectedCurrencyFrom + " -> " + expectedCurrencyTo + " to other client ")))
        ;
    }

    @Test
    public void testCreatingNewTransactionWithCreatingNewRates() throws JsonProcessingException {
        Long expectedAccountFromId = 12L; // account with EUR
        Currency expectedCurrencyFrom = Currency.EUR;
        Long expectedCustomerToId = 3L;
        Currency expectedCurrencyTo = Currency.USD;
        BigDecimal expectedAmountFrom = new BigDecimal(100000, MC);
        BigDecimal firstRate = new BigDecimal(1.15, MC);
        BigDecimal firstExpectedAmountTo = expectedAmountFrom.multiply(firstRate);
        BigDecimal secondRate = new BigDecimal(1.25, MC);
        BigDecimal secondExpectedAmountTo = expectedAmountFrom.multiply(secondRate);

        //Insert a new rate from the present moment
        NewExchangeRateRequest rateRequest =
            new NewExchangeRateRequest(expectedCurrencyFrom, expectedCurrencyTo, firstRate, null, null);
        client.createExchangeRateRequest(rateRequest)
            .post()
            .then()
            .body("id", notNullValue())
            .body("currencyFrom", is(expectedCurrencyFrom.toString()))
            .body("currencyTo", is(expectedCurrencyTo.toString()))
            .body("rate", is(firstRate.floatValue()))
        ;

        // Wait 0.5 sec
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }

        // The first transaction
        NewMoneyTransferRequest requestBody =
            new NewMoneyTransferRequest(expectedAccountFromId, expectedCustomerToId, expectedAmountFrom, expectedCurrencyTo);
        client.createTransactionRequest(requestBody)
            .post()
            .then()
            .body("amountTo", is(firstExpectedAmountTo.floatValue()))
            .body("rate", is(firstRate.floatValue()))
        ;

        //Insert a new rate from the present moment
        rateRequest =
            new NewExchangeRateRequest(expectedCurrencyFrom, expectedCurrencyTo, secondRate, null, null);
        client.createExchangeRateRequest(rateRequest)
            .post()
            .then()
            .body("id", notNullValue())
            .body("currencyFrom", is(expectedCurrencyFrom.toString()))
            .body("currencyTo", is(expectedCurrencyTo.toString()))
            .body("rate", is(secondRate.floatValue()))
        ;

        // Wait 0.5 sec
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }

        // The second transaction
        requestBody =
            new NewMoneyTransferRequest(expectedAccountFromId, expectedCustomerToId, expectedAmountFrom, expectedCurrencyTo);
        client.createTransactionRequest(requestBody)
            .post()
            .then()
            .body("amountTo", is(secondExpectedAmountTo.floatValue()))
            .body("rate", is(secondRate.floatValue()))
        ;
    }
}
