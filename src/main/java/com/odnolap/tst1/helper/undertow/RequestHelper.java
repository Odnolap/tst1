package com.odnolap.tst1.helper.undertow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.odnolap.tst1.helper.PropertiesHelper;
import com.odnolap.tst1.model.GetAccountsRequest;
import com.odnolap.tst1.model.GetCustomersRequest;
import com.odnolap.tst1.model.GetExchangeRatesRequest;
import com.odnolap.tst1.model.GetTransactionsRequest;
import com.odnolap.tst1.model.NewExchangeRateRequest;
import com.odnolap.tst1.model.NewMoneyTransferRequest;
import com.odnolap.tst1.model.PageableRequest;
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

    public static final String ID = "id";
    public static final String ACCOUNT_ID_PARAM = "accountId";
    public static final String CUSTOMER_ID_PARAM = "customerId";
    private static final String PAGE_NUMBER = "page";
    private static final String OFFSET = "offset";

    public static GetTransactionsRequest createGetTransactionsRequest(HttpServerExchange exchange) {
        String idStr = getParamFirstValue(exchange, ID); // Located in path but stored in exchange.getQueryParameters
        String accountIdStr = getParamFirstValue(exchange, ACCOUNT_ID_PARAM);
        String customerIdStr = getParamFirstValue(exchange, CUSTOMER_ID_PARAM);

        GetTransactionsRequest result = new GetTransactionsRequest();
        // id
        if (StringUtils.isNotBlank(idStr)) {
            try {
                result.setTransactionId(Long.valueOf(idStr));
            } catch (NumberFormatException ex) {
                throw new RequestParsingException("URL path \"" + ID
                    + "\" has invalid integer format or it's too long: \"" + idStr + "\"", ex);
            }
        }
        // accountId
        if (StringUtils.isNotBlank(accountIdStr)) {
            try {
                result.setAccountId(Long.valueOf(accountIdStr));
            } catch (NumberFormatException ex) {
                throw new RequestParsingException("Parameter \"" + ACCOUNT_ID_PARAM
                    + "\" has invalid integer format or it's too long: \"" + accountIdStr + "\"", ex);
            }
        }
        // customerId
        if (StringUtils.isNotBlank(customerIdStr)) {
            try {
                result.setCustomerId(Long.valueOf(customerIdStr));
            } catch (NumberFormatException ex) {
                throw new RequestParsingException("Parameter \"" + CUSTOMER_ID_PARAM
                    + "\" has invalid integer format or it's too long: \"" + customerIdStr + "\"", ex);
            }
        }

        putPaginationOptions(exchange, result);
        return result;

    }

    public static GetCustomersRequest createGetCustomersRequest(HttpServerExchange exchange) {
        String idStr = getParamFirstValue(exchange, ID); // Located in path but stored in exchange.getQueryParameters

        GetCustomersRequest result = new GetCustomersRequest();
        // id
        if (StringUtils.isNotBlank(idStr)) {
            try {
                result.setCustomerId(Long.valueOf(idStr));
            } catch (NumberFormatException ex) {
                throw new RequestParsingException("URL path \"" + ID
                    + "\" has invalid integer format or it's too long: \"" + idStr + "\"", ex);
            }
        }

        putPaginationOptions(exchange, result);
        return result;

    }

    public static GetAccountsRequest createGetAccountsRequest(HttpServerExchange exchange) {
        String idStr = getParamFirstValue(exchange, ID); // Located in path but stored in exchange.getQueryParameters

        GetAccountsRequest result = new GetAccountsRequest();
        // id
        if (StringUtils.isNotBlank(idStr)) {
            try {
                result.setAccountId(Long.valueOf(idStr));
            } catch (NumberFormatException ex) {
                throw new RequestParsingException("URL path \"" + ID
                    + "\" has invalid integer format or it's too long: \"" + idStr + "\"", ex);
            }
        }

        putPaginationOptions(exchange, result);
        return result;

    }

    public static GetExchangeRatesRequest createGetExchangeRatesRequest(HttpServerExchange exchange) {
        String idStr = getParamFirstValue(exchange, ID); // Located in path but stored in exchange.getQueryParameters

        GetExchangeRatesRequest result = new GetExchangeRatesRequest();
        // id
        if (StringUtils.isNotBlank(idStr)) {
            try {
                result.setExchangeRateId(Long.valueOf(idStr));
            } catch (NumberFormatException ex) {
                throw new RequestParsingException("URL path \"" + ID
                    + "\" has invalid integer format or it's too long: \"" + idStr + "\"", ex);
            }
        }

        putPaginationOptions(exchange, result);
        return result;

    }

    private static void putPaginationOptions(HttpServerExchange exchange, PageableRequest request) {
        // offset
        String offsetStr = getParamFirstValue(exchange, OFFSET);
        int offset;
        try {
            offset = Integer.parseInt(offsetStr);
        } catch (NumberFormatException ex) {
            offset = DEFAULT_OFFSET;
        }
        request.setOffset(offset);

        // startFrom
        String pageStr = getParamFirstValue(exchange, PAGE_NUMBER);
        int page;
        try {
            page = Integer.parseInt(pageStr);
            request.setStartFrom(page * offset);
        } catch (NumberFormatException ignored) {
        }
    }

    public static NewMoneyTransferRequest createNewTransactionRequest(HttpServerExchange exchange) {
        return getRequestBodyWithThrow(exchange, NewMoneyTransferRequest.class);
    }

    public static NewExchangeRateRequest createNewRateRequest(HttpServerExchange exchange) {
        return getRequestBodyWithThrow(exchange, NewExchangeRateRequest.class);
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
