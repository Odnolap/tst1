package com.odnolap.tst1.helper.undertow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.odnolap.tst1.helper.PropertiesHelper;
import com.odnolap.tst1.model.GetTransactionsRequest;
import com.odnolap.tst1.model.MoneyTransferRequest;
import com.odnolap.tst1.model.exceptions.RequestParsingException;
import io.undertow.server.HttpServerExchange;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Deque;
import java.util.Optional;
import java.util.TreeMap;

@Slf4j
public class RequestHelper {

    public static final ObjectMapper MAPPER;
    private static int DEFAULT_OFFSET;
    static {
        MAPPER = new ObjectMapper();

        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        MAPPER.registerModule(new JavaTimeModule());

        DEFAULT_OFFSET = PropertiesHelper.getProperty("default.offset", 10);
    }

    private static final String ID = "id";
    private static final String ACCOUNT_ID_PARAM = "accountId";
    private static final String CUSTOMER_ID_PARAM = "customerId";
    private static final String PAGE_NUMBER = "page";
    private static final String OFFSET = "offset";

    public static GetTransactionsRequest createGetTransactionRequest(HttpServerExchange exchange) {
        String idStr = getParamFirstValue(exchange, ID); // Located in path but stored in exchange.getQueryParameters
        String accountIdStr = getParamFirstValue(exchange, ACCOUNT_ID_PARAM);
        String customerIdStr = getParamFirstValue(exchange, CUSTOMER_ID_PARAM);

        GetTransactionsRequest result = new GetTransactionsRequest();
        boolean parseSuccessful = false;
        // id
        if (StringUtils.isNotBlank(idStr)) {
            try {
                result.setTransactionId(Long.valueOf(idStr));
            } catch (NumberFormatException ex) {
                throw new RequestParsingException("URL path \"" + ID
                    + "\" has invalid integer format or it's too long: \"" + idStr + "\"", ex);
            }
            parseSuccessful = true;
        }
        // accountId
        if (StringUtils.isNotBlank(accountIdStr)) {
            try {
                result.setAccountId(Long.valueOf(accountIdStr));
            } catch (NumberFormatException ex) {
                throw new RequestParsingException("Parameter \"" + ACCOUNT_ID_PARAM
                    + "\" has invalid integer format or it's too long: \"" + accountIdStr + "\"", ex);
            }
            parseSuccessful = true;
        }
        // customerId
        if (StringUtils.isNotBlank(customerIdStr)) {
            try {
                result.setCustomerId(Long.valueOf(customerIdStr));
            } catch (NumberFormatException ex) {
                throw new RequestParsingException("Parameter \"" + CUSTOMER_ID_PARAM
                    + "\" has invalid integer format or it's too long: \"" + customerIdStr + "\"", ex);
            }
            parseSuccessful = true;
        }

        // There should be at least one of id, accountId or customerId
        if (!parseSuccessful) {
            throw new RequestParsingException("You've sent GET request. There is no " + ID + "in path and empty \""
                + ACCOUNT_ID_PARAM + "\" and \"" + CUSTOMER_ID_PARAM + "\" params.");
        } else {
            // offset
            String offsetStr = getParamFirstValue(exchange, OFFSET);
            int offset;
            try {
                offset = Integer.parseInt(offsetStr);
            } catch (NumberFormatException ex) {
                offset = DEFAULT_OFFSET;
            }
            result.setOffset(offset);

            // startFrom
            String pageStr = getParamFirstValue(exchange, PAGE_NUMBER);
            int page;
            try {
                page = Integer.parseInt(pageStr);
                result.setStartFrom(page * offset);
            } catch (NumberFormatException ignored) {
            }

            return result;
        }
    }

    public static MoneyTransferRequest createNewTransactionRequest(HttpServerExchange exchange) {
        return getRequestBodyWithThrow(exchange, MoneyTransferRequest.class);
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
        return MAPPER.readValue(exchange.getInputStream(), clazz);
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
