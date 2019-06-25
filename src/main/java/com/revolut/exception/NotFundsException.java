package com.revolut.exception;

public class NotFundsException extends BaseException {
    public NotFundsException(String message) {
        super(message, 404);
    }
}