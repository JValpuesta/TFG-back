package com.valpuestajorge.conecta4.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class LoginOutputDto {
    private String token;
    private String expirationTime;
    private String refreshToken;
    private String refreshTime;
    private String type;
    private HttpStatus status;
    private String message;
}
