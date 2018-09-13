package com.odnolap.tst1.common;

import com.odnolap.tst1.Application;
import com.odnolap.tst1.helper.PropertiesHelper;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class BaseIntegrationTest {
    private static final int PORT = PropertiesHelper.getProperty("port", 8080);
    private static final String HOST = PropertiesHelper.getProperty("host", "localhost");

    protected APIClient client = new APIClient();

    @BeforeClass
    public static void initEnvironment() {
        Application.main(null);
    }

    @AfterClass
    public static void shutdownEnvironment() {
        new APIClient().quitRequest().get();
    }

    protected static class APIClient {

        private boolean logResponse = false;

        /**
         * Use this method to log the response to debug tests
         */
        public APIClient logResponse() {
            this.logResponse = true;
            return this;
        }

        public RequestSpecification transactionGet() {
            return transactionGet(null);
        }

        public RequestSpecification transactionGet(String id) {
            return transactionWithoutPaginationGet(id)
                .queryParam("page", 0)
                .queryParam("offset", 10);
        }

        public RequestSpecification transactionWithoutPaginationGet() {
            return transactionWithoutPaginationGet(null);
        }

        public RequestSpecification transactionWithoutPaginationGet(String id) {
            return baseRequest()
                .basePath(isBlank(id)
                    ? "/v1/transactions"
                    : "/v1/transactions/" + id);
        }

        public RequestSpecification quitRequest() {
            return baseRequest()
                .basePath("/v1/quit");
        }

        public RequestSpecification baseRequest() {
            RequestSpecification requestSpecification = given()
                .baseUri("http://" + HOST).port(PORT).log().all();

            if (logResponse) {
                requestSpecification = requestSpecification.filter(new ResponseLoggingFilter());
            }

            return requestSpecification;
        }

    }
}
