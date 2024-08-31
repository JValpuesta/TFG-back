package com.valpuestajorge.conecta4.security.config;

import com.valpuestajorge.conecta4.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class MainSecurity {

    private final ServerSecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http, JwtFilter jwtFilter) {
        return http
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/**").permitAll()
                        .anyExchange().permitAll()
                )
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.FIRST)
                .securityContextRepository(securityContextRepository)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // Deshabilitamos CSRF usando un lambda
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)  // Deshabilitamos formLogin
                .logout(ServerHttpSecurity.LogoutSpec::disable)  // Deshabilitamos logout
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)  // Deshabilitamos httpBasic
                .build();
    }
}