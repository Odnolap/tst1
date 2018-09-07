package com.odnolap.helper.undertow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odnolap.model.NewTransactionRequest;
import com.odnolap.model.GetTransactionsRequest;
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

    public static final ObjectMapper MAPPER;
    static {
        MAPPER = new ObjectMapper();

        // Don't throw an exception when json has extra fields you are
        // not serializing on. This is useful when you want to use a pojo
        // for deserialization and only care about a portion of the json
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Ignore null values when writing json.
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private static final String ID = "id";
    private static final String ACCOUNT_ID_PARAM = "accountId";
    private static final String CUSTOMER_ID_PARAM = "customerId";

    private static final String PAGE_NUMBER = "page";
    private static final String OFFSET = "offset";
    private static final int DEFAULT_OFFSET = 10;

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
