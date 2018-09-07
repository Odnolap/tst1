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
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.function.Function;

@Slf4j
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
            + "Try to get /accountTransaction?accountId=123 for example.";

        HttpHandler routes = new RoutingHandler()
            .get("/", HttpHandlerHelper.simpleTextHandler(rootDescription))
            .get("/accountTransaction", HttpHandlerHelper.jsonHttpHandler(getAccountTransactionHandlerFunction(service)))

//        .get("/myRoute", HttpHandlerHelper.constantStringHandler("GET - My Route"))
//        .post("/myRoute", HttpHandlerHelper.constantStringHandler("POST - My Route"))
//        .get("/myOtherRoute", HttpHandlerHelper.constantStringHandler("GET - My Other Route"))
            // Wildcards and RoutingHandler had some bugs before version 1.4.8.Final
//        .get("/myRoutePrefix*", HttpHandlerHelper.constantStringHandler("GET - My Prefixed Route"))
            // Pass a handler as a method reference.
            .setFallbackHandler(HttpHandlerHelper.getNotFoundHandler())
            ;

        return new AccessLogHandler(
            routes,
            new Slf4jAccessLogReceiver(LoggerFactory.getLogger("com.odnolap.tst1.accesslog")),
            "combined",
            MoneyTransferRestServer.class.getClassLoader())
        ;
    }

    private static Function<HttpServerExchange, Object> getAccountTransactionHandlerFunction(MoneyTransferService service) {
        return exchange -> {
            String accountIdStr = RequestHelper.getParamFirstValue(exchange, "accountId");
            if (StringUtils.isBlank(accountIdStr)) {
                throw new IllegalArgumentException("Empty accountId param.");
            }
            try {
                return service.getAccountTransactions(Long.valueOf(accountIdStr));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Parameter accountId has invalid integer format \""
                    + accountIdStr + "\"", ex);
            }
        };
    }
}
