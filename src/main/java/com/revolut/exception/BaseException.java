package com.revolut.exception;

import lombok.Getter;

public class BaseException extends RuntimeException {

    @Getter
    private final int code;

    public BaseException(String message, int code) {
        super(message);
        this.code = code;
    }
}
