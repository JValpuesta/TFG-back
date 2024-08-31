package com.valpuestajorge.conecta4.security.handler;

import com.valpuestajorge.conecta4.app_user.domain.AppUser;
import com.valpuestajorge.conecta4.app_user.dto.in.CreateUserDto;
import com.valpuestajorge.conecta4.app_user.dto.in.LoginDto;
import com.valpuestajorge.conecta4.app_user.dto.in.TokenDto;
import com.valpuestajorge.conecta4.app_user.service.AppUserService;
import com.valpuestajorge.conecta4.shared.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthHandler {

    private final AppUserService userService;

    private final ObjectValidator objectValidator;

    public Mono<ServerResponse> login(ServerRequest request) {
        Mono<LoginDto> dtoMono = request.bodyToMono(LoginDto.class).doOnNext(objectValidator::validate);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.login(dto), TokenDto.class));
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<CreateUserDto> dtoMono = request.bodyToMono(CreateUserDto.class).doOnNext(objectValidator::validate);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.create(dto), AppUser.class));
    }
}