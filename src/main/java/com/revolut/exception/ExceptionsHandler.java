package com.revolut.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionsHandler extends RuntimeException implements ExceptionMapper<BaseException> {

    @Override
    public Response toResponse(BaseException exception) {
        return Response.status(exception.getCode())
                .entity(ErrorMessage.builder()
                .message(exception.getMessage()).build())
                .type("application/json").build();
    }
}
