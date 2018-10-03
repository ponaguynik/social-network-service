package com.testtask.socialnetworkservice.exception;

public class UnableLoadDataException extends RuntimeException {

    public UnableLoadDataException() {
    }

    public UnableLoadDataException(String message) {
        super(message);
    }

    public UnableLoadDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
