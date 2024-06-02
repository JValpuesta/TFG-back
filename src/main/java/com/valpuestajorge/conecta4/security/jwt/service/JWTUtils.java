package com.valpuestajorge.conecta4.security.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.valpuestajorge.conecta4.security.jwt.service.exception.LoginException;
import com.valpuestajorge.conecta4.security.model.LoginOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JWTUtils {

    @Value("${spring.jwt.key}")
    private String key;
    @Value("${spring.jwt.user}")
    private String userGenerator;
    @Value("${spring.jwt.expirationTime}")
    private String expirationLimit;
    @Value("${spring.jwt.refreshTime}")
    private String refreshLimit;

    @Autowired
    private MessageSource messageSource;

    public LoginOutputDto getToken(Authentication authentication) {
        try {
            Algorithm algorithm = getAlgorithm();
            String username = authentication.getPrincipal().toString();
            String authorities = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
            long expirationTime = Long.parseLong(expirationLimit);
            Date current = new Date();
            String token = JWT.create()
                    .withIssuer(userGenerator)
                    .withSubject(username)
                    .withClaim("authorities", authorities)
                    .withIssuedAt(current)
                    .withExpiresAt(new Date(current.getTime() + expirationTime))
                    .withJWTId(UUID.randomUUID().toString())
                    .withNotBefore(current)
                    .sign(algorithm);
            String refreshToken = "TODO"; // Debes implementar el token de refresco
            Locale locale = LocaleContextHolder.getLocale();
            String message = messageSource.getMessage("user.successfulLogin.message", null, locale);
            return new LoginOutputDto(token, expirationLimit, refreshToken, refreshLimit, "", HttpStatus.OK, message);

        } catch (JWTCreationException exception) {
            return null;
        }
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(key);
    }

    public DecodedJWT verifyToken(String token) throws LoginException {
        try {
            Algorithm algorithm = getAlgorithm();
            JWTVerifier jwtVerifier = JWT.require(algorithm)
                    .withIssuer(userGenerator)
                    .build();
            return jwtVerifier.verify(token);
        } catch (JWTVerificationException jwtVerificationException) {
            Locale locale = LocaleContextHolder.getLocale();
            String message = messageSource.getMessage("exception.unauthorizedException", null, locale);
            throw new LoginException(message);
        }
    }

    public String extractUsername(DecodedJWT token) {
        return token.getSubject();
    }

    public Claim getSpecificClaim(DecodedJWT token, String key) {
        return token.getClaim(key);
    }

    public Map<String, Claim> getAllClaims(DecodedJWT token) {
        return token.getClaims();
    }
}
