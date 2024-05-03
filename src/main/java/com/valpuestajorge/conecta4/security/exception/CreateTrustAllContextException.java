package com.valpuestajorge.conecta4.security.exception;

public class CreateTrustAllContextException extends RuntimeException{
    public CreateTrustAllContextException(String message, Exception e){
        super(message, e);
    }
}
