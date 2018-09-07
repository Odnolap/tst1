package com.odnolap;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.odnolap.config.AppInjector;
import com.odnolap.helper.undertow.HttpHandlerHelper;
import com.odnolap.helper.undertow.RequestHelper;
import com.odnolap.model.undertow.SimpleServer;
import com.odnolap.model.undertow.Slf4jAccessLogReceiver;
import com.odnolap.service.MoneyTransferService;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class MoneyTransferRestServer {
    private static volatile boolean isServerRun;

    @Inject
    @Getter
    private MoneyTransferService moneyTransferService;

    public static synchronized void startServer() {
        if (!isServerRun) {
            Injector injector = Guice.createInjector(new AppInjector());
            MoneyTransferRestServer server = injector.getInstance(MoneyTransferRestServer.class);
            MoneyTransferService service = server.getMoneyTransferService();
            HttpHandler root = initRoot(service);
            SimpleServer.simpleServer(root).start();
            isServerRun = true;
            log.info("Money transfer server started.");
        }
    }

    private static HttpHandler initRoot(MoneyTransferService service) {
        String rootDescription = "It's just a test server!\n"
            + "Try to get /v1/transactions?accountId=123 or /v1/transactions?customerId=456 for example.";

        HttpHandler routes = new RoutingHandler()
            .get("/", HttpHandlerHelper.simpleTextHandler(rootDescription))
            .get("/v1/transactions", HttpHandlerHelper.jsonHttpHandler(
                exchange -> service.getTransactions(RequestHelper.createGetTransactionRequest(exchange)))
            )
            .get("/v1/transactions/{id}", HttpHandlerHelper.jsonHttpHandler(
                exchange -> service.getTransactions(RequestHelper.createGetTransactionRequest(exchange)))
            )
            .post("/v1/transactions", HttpHandlerHelper.jsonBlockingHttpHandler(
                exchange -> service.createMoneyTransferTransaction(RequestHelper.createNewTransactionRequest(exchange)),
                201)
            )
            .setFallbackHandler(HttpHandlerHelper::notFoundHandler)
            ;

        return new AccessLogHandler(
            routes,
            new Slf4jAccessLogReceiver(LoggerFactory.getLogger("com.odnolap.tst1.accesslog")),
            "combined",
            MoneyTransferRestServer.class.getClassLoader())
        ;
    }
}
