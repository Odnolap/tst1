package com.odnolap.helper.undertow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odnolap.model.undertow.ErrorResponse;
import com.odnolap.model.undertow.JsonHttpHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.MimeMappings;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.function.Function;

import static com.odnolap.helper.undertow.RequestHelper.BASE_MAPPER;

@Slf4j
public class HttpHandlerHelper {
    public static HttpHandler simpleTextHandler(String value) {
        return (HttpServerExchange exchange) -> {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE,
                MimeMappings.DEFAULT_MIME_MAPPINGS.get("txt"));
            exchange.getResponseSender().send(value);
        };
    }

    public static HttpHandler jsonHttpHandler(Function<HttpServerExchange, Object> function) {
        return new JsonHttpHandler(function);
    }

    public static HttpHandler jsonHttpHandler(Function<HttpServerExchange, Object> function, ObjectMapper mapper) {
        return new JsonHttpHandler(function, mapper);
    }

    public static void notFoundHandler(HttpServerExchange exchange) throws JsonProcessingException {
        String errorMessage = "Page not found!";
        UUID uuid = UUID.randomUUID();
        log.error("{}; error UUID: {}", errorMessage, uuid);

        exchange.getResponseSender()
            .send(BASE_MAPPER.writeValueAsString(new ErrorResponse(404, uuid, errorMessage)));
    }
}
