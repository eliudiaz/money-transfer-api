package com.revolut.exception;

public class DataValidationException extends BaseException {
    public DataValidationException(String message) {
        super(message,422);
    }
}
