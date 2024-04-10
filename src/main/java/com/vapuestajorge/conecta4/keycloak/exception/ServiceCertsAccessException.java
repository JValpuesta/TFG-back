package com.vapuestajorge.conecta4.keycloak.exception;

public class ServiceCertsAccessException extends RuntimeException{
    public ServiceCertsAccessException(Exception e){
        super(e);
    }
}
