package com.revolut.exception;

public class AccountDisabledException extends BaseException {

    public AccountDisabledException(String message) {
        super(message,422);
    }
}
