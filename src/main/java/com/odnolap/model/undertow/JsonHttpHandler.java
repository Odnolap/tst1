package com.odnolap.model.undertow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.MimeMappings;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

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
        this.mapper = mapper != null ? mapper : getBaseObjectMapper();
    }

    private ObjectMapper getBaseObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Don't throw an exception when json has extra fields you are
        // not serializing on. This is useful when you want to use a pojo
        // for deserialization and only care about a portion of the json
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Ignore null values when writing json.
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Write times as a String instead of a Long so its human readable.
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.registerModule(new JavaTimeModule());

        return mapper;
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
