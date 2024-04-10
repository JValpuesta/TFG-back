package com.vapuestajorge.conecta4.keycloak.exception;

public class CreateTrustAllContextException extends RuntimeException{
    public CreateTrustAllContextException(String message, Exception e){
        super(message, e);
    }
}
