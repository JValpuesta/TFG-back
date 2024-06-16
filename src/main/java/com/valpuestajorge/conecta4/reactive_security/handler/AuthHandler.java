package com.valpuestajorge.conecta4.reactive_security.handler;

import com.valpuestajorge.conecta4.reactive_security.dto.CreateUserDto;
import com.valpuestajorge.conecta4.reactive_security.dto.LoginDto;
import com.valpuestajorge.conecta4.reactive_security.dto.TokenDto;
import com.valpuestajorge.conecta4.reactive_security.service.UserService;
import com.valpuestajorge.conecta4.reactive_security.entity.AppUser;
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

    private final UserService userService;

    public Mono<ServerResponse> login(ServerRequest request){
        Mono<LoginDto> dtoMono = request.bodyToMono(LoginDto.class);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.login(dto), TokenDto.class));
    }

    public Mono<ServerResponse> create(ServerRequest request){
        Mono<CreateUserDto> dtoMono = request.bodyToMono(CreateUserDto.class);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.create(dto), AppUser.class));
    }

}
