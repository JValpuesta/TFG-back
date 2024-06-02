package com.valpuestajorge.conecta4.reactive_security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    @Value("${spring.jwt.key}")
    private String key;
    @Value("${spring.jwt.user}")
    private String userGenerator;
    @Value("${spring.jwt.expirationTime}")
    private String expirationLimit;
    @Value("${spring.jwt.refreshTime}")
    private String refreshLimit;

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + Integer.parseInt(expirationLimit)))
                .signWith(getKey(key))
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey(key)).build().parseClaimsJwt(token).getBody();
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validate(String token) {

        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    private Key getKey(String secret){
        byte[] secreteBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(secreteBytes);
    }

}
