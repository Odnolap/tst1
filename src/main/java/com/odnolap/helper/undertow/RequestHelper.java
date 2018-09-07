package com.odnolap.helper.undertow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.odnolap.model.NewTransactionRequest;
import com.odnolap.model.GetTransactionRequest;
import com.odnolap.model.MoneyTransferRequest;
import com.odnolap.model.exceptions.RequestParsingException;
import io.undertow.server.HttpServerExchange;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Deque;
import java.util.Optional;
import java.util.TreeMap;

public class RequestHelper {

    public static final ObjectMapper BASE_MAPPER;
    static {
        BASE_MAPPER = new ObjectMapper();

        // Don't throw an exception when json has extra fields you are
        // not serializing on. This is useful when you want to use a pojo
        // for deserialization and only care about a portion of the json
        BASE_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Ignore null values when writing json.
        BASE_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private static final String ACCOUNT_ID_PARAM = "accountId";
    private static final String CUSTOMER_ID_PARAM = "customerId";

    public static GetTransactionRequest createGetTransactionRequest(HttpServerExchange exchange) {
        String accountIdStr = getParamFirstValue(exchange, ACCOUNT_ID_PARAM);
        String customerIdStr = getParamFirstValue(exchange, CUSTOMER_ID_PARAM);

        GetTransactionRequest result = new GetTransactionRequest();
        boolean parseSuccessful = false;
        if (StringUtils.isNotBlank(accountIdStr)) {
            try {
                result.setAccountId(Long.valueOf(accountIdStr));
            } catch (NumberFormatException ex) {
                throw new RequestParsingException("Parameter \"" + ACCOUNT_ID_PARAM + "\" has invalid integer format \""
                    + accountIdStr + "\"", ex);
            }
            parseSuccessful = true;
        }
        if (StringUtils.isNotBlank(customerIdStr)) {
            try {
                result.setCustomerId(Long.valueOf(customerIdStr));
            } catch (NumberFormatException ex) {
                throw new RequestParsingException("Parameter \"" + CUSTOMER_ID_PARAM + "\" has invalid integer format \""
                    + customerIdStr + "\"", ex);
            }
            parseSuccessful = true;
        }

        if (!parseSuccessful) {
            throw new RequestParsingException("Empty \"" + ACCOUNT_ID_PARAM + "\" and \""
                + CUSTOMER_ID_PARAM + "\" params.");
        } else {
            return result;
        }
    }

    public static NewTransactionRequest createNewTransactionRequest(HttpServerExchange exchange) {
        MoneyTransferRequest moneyTransferRequest = RequestHelper.getRequestBodyWithThrow(exchange, MoneyTransferRequest.class);
        return new NewTransactionRequest(moneyTransferRequest);
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

    public static String getParamFirstValueWithThrow(HttpServerExchange exchange, String paramName) {
        String value = getParamFirstValue(exchange, paramName);
        if (StringUtils.isBlank(value)) {
            throw new RequestParsingException("Empty " + paramName + " param.");
        }
        return value;
    }

    public static <T> T getRequestBody(HttpServerExchange exchange, Class<T> clazz) throws IOException {
        exchange.startBlocking();
        return BASE_MAPPER.readValue(exchange.getInputStream(), clazz);
    }

    public static <T> T getRequestBodyWithThrow(HttpServerExchange exchange, Class<T> clazz) {
        T body;
        try {
            body = getRequestBody(exchange, clazz);
        } catch (IOException ex) {
            throw new RequestParsingException("Error during parsing request body.", ex);
        }
        return body;
    }


}
