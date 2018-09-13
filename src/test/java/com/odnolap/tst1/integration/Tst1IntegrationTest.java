package com.odnolap.tst1.integration;

import com.odnolap.tst1.common.BaseIntegrationTest;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

@Ignore
public class Tst1IntegrationTest extends BaseIntegrationTest {

    @Test
    public void testGetAllTransactionsWithLogResponse() {
        client.logResponse().transactionGet()
            .get()
            .then()
            .body("transactions.id", containsInAnyOrder(1001, 1002, 1003, 1004, 1005, 1006))
            .body("transactions.find { it.id == 1006 }.customerFromId", is(1))
            .body("transactions.find { it.id == 1006 }.customerToId", is(2))
            .body("transactions.find { it.id == 1006 }.accountFromId", is(11))
            .body("transactions.find { it.id == 1006 }.accountToId", is(14))
            .body("transactions.find { it.id == 1006 }.amountFrom", is(352348191.53f))
            .body("transactions.find { it.id == 1006 }.amountTo", is(352348191.53f))
            .body("transactions.find { it.id == 1006 }.currencyFrom", is("USD"))
            .body("transactions.find { it.id == 1006 }.currencyTo", is("USD"))
            .body("transactions.find { it.id == 1006 }.rate", is(1f))
            .body("transactions.find { it.id == 1006 }.creationTimestamp", is("2018-09-10T22:20:00.000+0000"))
            .body("transactions.find { it.id == 1006 }.finalizationTimestamp", is("2018-09-10T22:10:01.000+0000"))
            .body("transactions.find { it.id == 1006 }.status", is("SUCCESSFUL"))
            .body("transactions.find { it.id == 1006 }.description", is("transfer USD -> USD to other client (Melinda G.)"))
            ;
    }
}
