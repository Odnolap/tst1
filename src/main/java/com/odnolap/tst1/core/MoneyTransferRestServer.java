package com.odnolap.tst1.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.odnolap.tst1.config.AppInjector;
import com.odnolap.tst1.helper.undertow.HttpHandlerHelper;
import com.odnolap.tst1.helper.undertow.RequestHelper;
import com.odnolap.tst1.model.undertow.SimpleServer;
import com.odnolap.tst1.model.undertow.Slf4jAccessLogReceiver;
import com.odnolap.tst1.service.AccountService;
import com.odnolap.tst1.service.CustomerService;
import com.odnolap.tst1.service.ExchangeRateService;
import com.odnolap.tst1.service.MoneyTransferService;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.util.Headers;
import io.undertow.util.MimeMappings;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

@Slf4j
public class MoneyTransferRestServer {
    private static volatile boolean isServerRun;
    private static Injector injector;
    private static Undertow server;
    // Keep this session opened because loss of all connections to in-memory H2 DB closes it
    private static Session session;

    public static synchronized void startServer() {
        if (!isServerRun) {
            injector = Guice.createInjector(new AppInjector());
            MoneyTransferService moneyTransferService = injector.getInstance(MoneyTransferService.class);
            CustomerService customerService = injector.getInstance(CustomerService.class);
            AccountService accountService = injector.getInstance(AccountService.class);
            ExchangeRateService exchangeRateService = injector.getInstance(ExchangeRateService.class);
            SessionFactory sessionFactory = injector.getInstance(SessionFactory.class);
            session = sessionFactory.openSession();
            HttpHandler root = initRoot(moneyTransferService, customerService, accountService, exchangeRateService);
            server = SimpleServer.simpleServer(root).start();
            isServerRun = true;
            log.info("Money transfer server started.");
        }
    }

    private static HttpHandler initRoot(MoneyTransferService moneyTransferService, CustomerService customerService,
                                        AccountService accountService, ExchangeRateService exchangeRateService) {
        String rootDescription = "It's just a test server!\n"
            + "Try to get localhost:8080/v1/transactions?accountId=11&page=0&offset=10\n"
            + "or localhost:8080/v1/transactions?cusTOmeRId=1&page=1&offset=2\n"
            + "or localhost:8080/v1/transactions/1003\n"
            + "or localhost:8080/v1/transactions?page=0&offset=20 for example.\n"
            + "Available endpoints:\n"
            + "- /v1/transactions (GET, POST)\n"
            + "- /v1/transactions/{id} (GET)\n"
            + "- /v1/customers (GET)\n"
            + "- /v1/customers/{id} (GET)\n"
            + "- /v1/accounts (GET)\n"
            + "- /v1/accounts/{id} (GET)\n"
            + "- /v1/rates (GET, POST)\n"
            + "- /v1/rates/{id} (GET)\n"
            ;

        HttpHandler routes = new RoutingHandler()
            .get("/", HttpHandlerHelper.simpleTextHandler(rootDescription))

            // Transactions: GET, CREATE
            .get("/v1/transactions", HttpHandlerHelper.jsonHttpHandler(
                exchange -> moneyTransferService.getTransactions(RequestHelper.createGetTransactionsRequest(exchange)))
            )
            .get("/v1/transactions/{id}", HttpHandlerHelper.jsonHttpHandler(
                exchange -> moneyTransferService.getTransactions(RequestHelper.createGetTransactionsRequest(exchange)))
            )
            .post("/v1/transactions", HttpHandlerHelper.jsonBlockingHttpHandler(
                exchange -> moneyTransferService.createMoneyTransferTransaction(RequestHelper.createNewTransactionRequest(exchange)),
                201)
            )

            // Customers: GET
            .get("/v1/customers", HttpHandlerHelper.jsonHttpHandler(
                exchange -> customerService.getCustomers(RequestHelper.createGetCustomersRequest(exchange)))
            )
            .get("/v1/customers/{id}", HttpHandlerHelper.jsonHttpHandler(
                exchange -> customerService.getCustomers(RequestHelper.createGetCustomersRequest(exchange)))
            )

            // Accounts: GET
            .get("/v1/accounts", HttpHandlerHelper.jsonHttpHandler(
                exchange -> accountService.getAccounts(RequestHelper.createGetAccountsRequest(exchange)))
            )
            .get("/v1/accounts/{id}", HttpHandlerHelper.jsonHttpHandler(
                exchange -> accountService.getAccounts(RequestHelper.createGetAccountsRequest(exchange)))
            )

            // Exchange rates: GET
            .get("/v1/rates", HttpHandlerHelper.jsonHttpHandler(
                exchange -> exchangeRateService.getExchangeRates(RequestHelper.createGetExchangeRatesRequest(exchange)))
            )
            .get("/v1/rates/{id}", HttpHandlerHelper.jsonHttpHandler(
                exchange -> exchangeRateService.getExchangeRates(RequestHelper.createGetExchangeRatesRequest(exchange)))
            )
            .post("/v1/rates", HttpHandlerHelper.jsonBlockingHttpHandler(
                exchange -> exchangeRateService.createExchangeRate(RequestHelper.createNewRateRequest(exchange)),
                201)
            )

            // Stop application
            .get("/v1/quit", new BlockingHandler(exchange -> {
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE,
                    MimeMappings.DEFAULT_MIME_MAPPINGS.get("txt"));
                exchange.getResponseSender().send("Application is stopped.\n");
                quit();
            }))
            .setFallbackHandler(HttpHandlerHelper::notFoundHandler)
            ;

        return new AccessLogHandler(
            routes,
            new Slf4jAccessLogReceiver(LoggerFactory.getLogger("com.odnolap.tst1.accesslog")),
            "combined",
            MoneyTransferRestServer.class.getClassLoader())
        ;
    }

    static void quit() throws SQLException, IOException {
        log.info("Stopping application.");
        session.close();
        SessionFactory sessionFactory = injector.getInstance(SessionFactory.class);
        sessionFactory.close();
        server.stop();
        server = null;
        log.info("Application is stopped.");
    }
}
