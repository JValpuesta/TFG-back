package com.valpuestajorge.conecta4.errors;

import java.time.LocalDateTime;
import java.util.Date;

public class NotFoundException extends RuntimeException{
    private final CustomError error;

    public NotFoundException(String message) {
        super(message);
        error = new CustomError(LocalDateTime.now(), 404, message);
    }

    public CustomError getError() {
        return error;
    }
}
