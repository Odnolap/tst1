package com.odnolap.model.exceptions;

public class RequestParsingException extends RuntimeException {

    public RequestParsingException(String message) {
        super(message);
    }

    public RequestParsingException(String message, Exception cause) {
        super(message, cause);
    }
}
