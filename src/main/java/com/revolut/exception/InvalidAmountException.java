package com.revolut.exception;

public class InvalidAmountException extends BaseException {
    public InvalidAmountException(String message) {
        super(message,422);
    }
}
