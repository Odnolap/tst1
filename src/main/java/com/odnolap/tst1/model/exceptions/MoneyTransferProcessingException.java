package com.odnolap.tst1.model.exceptions;

public class MoneyTransferProcessingException extends RuntimeException {

    public MoneyTransferProcessingException(String message) {
        super(message);
    }

    public MoneyTransferProcessingException(String message, Exception cause) {
        super(message, cause);
    }
}