package com.valpuestajorge.conecta4.reactive_security.jwt;

import com.valpuestajorge.conecta4.errors.CustomError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
@Slf4j
public class JwtFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        if(path.contains("auth")){
            return chain.filter(exchange);
        }
        String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(Objects.isNull(auth)){
            return Mono.error(new CustomError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "no token was found"));
        }
        if(auth.startsWith("Bearer ")){
            return Mono.error(new CustomError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "invalid auth"));
        }
        String token = auth.replace("Bearer ", "");
        exchange.getAttributes().put("token", token);
        return chain.filter(exchange);
    }

}
