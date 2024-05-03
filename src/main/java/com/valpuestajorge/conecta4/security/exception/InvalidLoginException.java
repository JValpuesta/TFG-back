package com.valpuestajorge.conecta4.security.exception;

public class InvalidLoginException extends RuntimeException{
    public InvalidLoginException(String message) {
       super(message);
    }
}
