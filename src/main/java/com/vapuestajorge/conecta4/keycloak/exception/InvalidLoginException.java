package com.vapuestajorge.conecta4.keycloak.exception;

public class InvalidLoginException extends RuntimeException{
    public InvalidLoginException(String message) {
       super(message);
    }
}
