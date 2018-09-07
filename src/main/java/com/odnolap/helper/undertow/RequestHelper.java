package com.odnolap.helper.undertow;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.server.HttpServerExchange;
import okhttp3.HttpUrl;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.Deque;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

public class RequestHelper {
    public static HttpUrl getCurrentUrl(HttpServerExchange exchange) {
        Objects.requireNonNull(exchange, "HttpServerExchange must not be null");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(exchange.getRequestURL()).newBuilder();

        if (!"".equals(exchange.getQueryString())) {
            urlBuilder = urlBuilder.encodedQuery(exchange.getQueryString());
        }
        return urlBuilder.build();
    }

    public static Deque<String> getParamValues(HttpServerExchange exchange, String paramName) {
        Deque<String> deque = exchange.getQueryParameters().get(paramName);
        if (CollectionUtils.isNotEmpty(deque)) {
            return deque;
        } else { // Try to find a parameter ignoring the case.
            TreeMap<String, Deque<String>> newMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            newMap.putAll(exchange.getQueryParameters());
            return newMap.get(paramName);
        }
    }

    public static String getParamFirstValue(HttpServerExchange exchange, String paramName) {
        return Optional.ofNullable(getParamValues(exchange, paramName))
            .map(Deque::getFirst).orElse(null);
    }

    public static <T> T getRequestBody(HttpServerExchange exchange,
                                ObjectMapper objectMapper, Class<T> clazz) throws IOException {
        return objectMapper.readValue(exchange.getInputStream(), clazz);
    }


}
