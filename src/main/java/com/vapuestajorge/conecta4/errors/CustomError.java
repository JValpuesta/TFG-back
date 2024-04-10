package com.vapuestajorge.conecta4.errors;

import java.util.Date;

public class CustomError {
    private final Date timestamp;
    private final int HttpCode;
    private final String mensaje;

    public CustomError(Date timestamp, int HttpCode, String mensaje) {
        this.timestamp = timestamp;
        this.HttpCode = HttpCode;
        this.mensaje = mensaje;
    }

    // Getters y setters

    public Date getTimestamp() {
        return timestamp;
    }

    public int getHttpCode() {
        return HttpCode;
    }

    public String getMensaje() {
        return mensaje;
    }
}