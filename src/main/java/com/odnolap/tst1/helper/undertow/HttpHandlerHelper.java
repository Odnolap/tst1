package com.odnolap.tst1.helper.undertow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odnolap.tst1.model.ErrorResponse;
import com.odnolap.tst1.model.undertow.JsonHttpHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.Headers;
import io.undertow.util.MimeMappings;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.function.Function;

import static com.odnolap.tst1.helper.undertow.RequestHelper.MAPPER;

@Slf4j
public class HttpHandlerHelper {
    public static HttpHandler simpleTextHandler(String value) {
        return (HttpServerExchange exchange) -> {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE,
                MimeMappings.DEFAULT_MIME_MAPPINGS.get("txt"));
            exchange.getResponseSender().send(value);
        };
    }

    public static HttpHandler jsonBlockingHttpHandler(Function<HttpServerExchange, Object> function) {
        return wrapToBlockingHadnler(jsonHttpHandler(function));
    }

    public static HttpHandler jsonBlockingHttpHandler(Function<HttpServerExchange, Object> function, Integer successCode) {
        return wrapToBlockingHadnler(jsonHttpHandler(function, successCode));
    }

    public static HttpHandler jsonHttpHandler(Function<HttpServerExchange, Object> function) {
        return new JsonHttpHandler(function);
    }

    public static HttpHandler jsonHttpHandler(Function<HttpServerExchange, Object> function, Integer successCode) {
        return new JsonHttpHandler(function, successCode);
    }

    public static void notFoundHandler(HttpServerExchange exchange) throws JsonProcessingException {
        String errorMessage = "Page not found!";
        UUID uuid = UUID.randomUUID();
        log.error("{}; error UUID: {}", errorMessage, uuid);
        exchange.setStatusCode(404)
            .getResponseSender()
            .send(MAPPER.writeValueAsString(new ErrorResponse(404, uuid, errorMessage)));
    }

    public static HttpHandler wrapToBlockingHadnler(HttpHandler handler) {
        return new BlockingHandler(handler);
    }
}
