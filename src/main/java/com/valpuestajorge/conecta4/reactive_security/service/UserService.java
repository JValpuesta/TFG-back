package com.valpuestajorge.conecta4.reactive_security.service;

import com.valpuestajorge.conecta4.errors.CustomError;
import com.valpuestajorge.conecta4.reactive_security.dto.CreateUserDto;
import com.valpuestajorge.conecta4.reactive_security.dto.LoginDto;
import com.valpuestajorge.conecta4.reactive_security.dto.TokenDto;
import com.valpuestajorge.conecta4.reactive_security.jwt.JwtProvider;
import com.valpuestajorge.conecta4.reactive_security.repository.UserRepository;
import com.valpuestajorge.conecta4.shared.util.UserRolesEnum;
import com.valpuestajorge.conecta4.reactive_security.entity.AppUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    public Mono<TokenDto> login(LoginDto dto){
        return userRepository.findByUsernameOrEmail(dto.getUsername(), dto.getUsername())
                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
                .map(user -> new TokenDto(jwtProvider.generateToken(user)))
                .switchIfEmpty(Mono.error(new CustomError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "bad credentials")));
    }

    public Mono<AppUser> create(CreateUserDto dto) {
        AppUser appUser = AppUser.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .userRole(UserRolesEnum.ADMIN)
                .build();
        Mono<Boolean> userExists = userRepository.findByUsernameOrEmail(appUser.getUsername(), appUser.getEmail()).hasElement();
        return userExists
                .flatMap(exists -> exists ?
                        Mono.error(new CustomError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "username or email already in use"))
                        : userRepository.save(appUser));
    }
}
