package com.valpuestajorge.conecta4.security.model;

import org.springframework.http.HttpStatusCode;

public record LoginOutputDto(String access_token, String expires_in, String refresh_token, String refresh_expires_in,
                             String configurations, HttpStatusCode status, String message) {

}
