package com.odnolap.tst1.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.odnolap.tst1.config.AppInjector;
import com.odnolap.tst1.helper.db.DbHelper;
import com.odnolap.tst1.helper.undertow.HttpHandlerHelper;
import com.odnolap.tst1.helper.undertow.RequestHelper;
import com.odnolap.tst1.model.undertow.SimpleServer;
import com.odnolap.tst1.model.undertow.Slf4jAccessLogReceiver;
import com.odnolap.tst1.service.MoneyTransferService;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.util.Headers;
import io.undertow.util.MimeMappings;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

@Slf4j
public class MoneyTransferRestServer {
    private static volatile boolean isServerRun;
    private static Undertow server;

    public static synchronized void startServer() {
        if (!isServerRun) {
            Injector injector = Guice.createInjector(new AppInjector());
            MoneyTransferService service = injector.getInstance(MoneyTransferService.class);
            HttpHandler root = initRoot(service);
            server = SimpleServer.simpleServer(root).start();
            isServerRun = true;
            log.info("Money transfer server started.");
        }
    }

    private static HttpHandler initRoot(MoneyTransferService service) {
        String rootDescription = "It's just a test server!\n"
            + "Try to get /v1/transactions?accountId=123 or /v1/transactions?customerId=456 "
            + "or /v1/transactions/555 for example.\n"
            + "Available endpoints:\n"
            + "- /v1/transactions (GET, POST)\n"
            + "- /v1/transactions/{id} (GET)\n";

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
            .get("/v1/quit", exchange -> {
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE,
                    MimeMappings.DEFAULT_MIME_MAPPINGS.get("txt"));
                exchange.getResponseSender().send("Application is stopped.");
                quit();
            })
            .setFallbackHandler(HttpHandlerHelper::notFoundHandler)
            ;

        return new AccessLogHandler(
            routes,
            new Slf4jAccessLogReceiver(LoggerFactory.getLogger("com.odnolap.tst1.accesslog")),
            "combined",
            MoneyTransferRestServer.class.getClassLoader())
        ;
    }

    static void quit() throws SQLException {
        log.info("Stopping application.");
        DbHelper.shutdownDb();
        server.stop();
        server = null;
        log.info("Application is stopped.");
    }
}
