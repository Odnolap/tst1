package com.odnolap.tst1.model.exceptions;

public class DbPersistenceException extends RuntimeException {

    public DbPersistenceException(String message) {
        super(message);
    }

    public DbPersistenceException(String message, Exception cause) {
        super(message, cause);
    }
}

