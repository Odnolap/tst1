package com.odnolap.model.undertow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.MimeMappings;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import static com.odnolap.helper.undertow.RequestHelper.BASE_MAPPER;

@Slf4j
public class JsonHttpHandler implements HttpHandler {

    private Function<HttpServerExchange, Object> function;
    private ObjectMapper mapper;

    public JsonHttpHandler(Function<HttpServerExchange, Object> function) {
        this(function, null);
    }

    public JsonHttpHandler(Function<HttpServerExchange, Object> function, ObjectMapper mapper) {
        Objects.requireNonNull(function, "Function must not be null.");
        this.function = function;
        this.mapper = mapper != null ? mapper : BASE_MAPPER;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws JsonProcessingException {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE,
            MimeMappings.DEFAULT_MIME_MAPPINGS.get("json"));
        try {
            exchange.getResponseSender().send(mapper.writeValueAsString(function.apply(exchange)));
        } catch (IllegalArgumentException ex) {
            sendErrorResponse(exchange, 400, ex);
        } catch (Exception ex) {
            sendErrorResponse(exchange, 500, ex);
        }
    }

    private void sendErrorResponse(HttpServerExchange exchange, int errorCode, Exception ex)
        throws JsonProcessingException {
        String errorMessage = ex.getMessage();
        UUID uuid = UUID.randomUUID();
        log.error(errorMessage + "; error UUID: " + uuid, ex);
        exchange.getResponseSender()
            .send(mapper.writeValueAsString(new ErrorResponse(errorCode, uuid, errorMessage)));
    }
}
