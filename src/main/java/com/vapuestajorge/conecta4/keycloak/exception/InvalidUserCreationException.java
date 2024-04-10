package com.vapuestajorge.conecta4.keycloak.exception;

public class InvalidUserCreationException extends RuntimeException{
    public InvalidUserCreationException(String message){
        super(message);
    }
}
