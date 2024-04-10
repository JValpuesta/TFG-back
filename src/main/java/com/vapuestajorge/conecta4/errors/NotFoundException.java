package com.vapuestajorge.conecta4.errors;

import java.util.Date;

public class NotFoundException extends RuntimeException{
    private final CustomError error;

    public NotFoundException(String message) {
        super(message);
        error = new CustomError(new Date(), 404, message);
    }

    public CustomError getError() {
        return error;
    }
}
