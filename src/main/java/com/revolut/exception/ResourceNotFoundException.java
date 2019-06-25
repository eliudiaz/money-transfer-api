package com.revolut.exception;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(final String message) {
        super(message, 404);
    }
}
