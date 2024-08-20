package com.valpuestajorge.conecta4.errors;

import java.time.LocalDateTime;
import java.util.Date;

public class UnprocessableEntityException extends RuntimeException {
    private final CustomError error;

    public UnprocessableEntityException(String message) {
        super(message);
        error = new CustomError(LocalDateTime.now(), 422, message);
    }

    public CustomError getError() {
        return error;
    }
}
