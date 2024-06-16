package com.valpuestajorge.conecta4.reactive_security.controller;

import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.errors.UnprocessableEntityException;
import com.valpuestajorge.conecta4.reactive_security.entity.AppUser;
import com.valpuestajorge.conecta4.reactive_security.dto.CreateUserDto;
import com.valpuestajorge.conecta4.reactive_security.dto.LoginDto;
import com.valpuestajorge.conecta4.reactive_security.dto.TokenDto;
import com.valpuestajorge.conecta4.reactive_security.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Tag(name = "User", description = "User operations")
@RequestMapping
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin("*")
@AllArgsConstructor
@Getter
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/login")
    public Mono<ResponseEntity<TokenDto>> login(@RequestBody LoginDto loginDto) throws NotFoundException, UnprocessableEntityException {
        return userService.login(loginDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<Mono<AppUser>> addUser(@PathVariable CreateUserDto userDto) {
        return ResponseEntity.ok(userService.create(userDto));
    }

}