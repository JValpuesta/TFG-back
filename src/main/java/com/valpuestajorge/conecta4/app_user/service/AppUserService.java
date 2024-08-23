package com.valpuestajorge.conecta4.app_user.service;

import com.valpuestajorge.conecta4.app_user.domain.AppUser;
import com.valpuestajorge.conecta4.app_user.dto.in.CreateUserDto;
import com.valpuestajorge.conecta4.app_user.dto.in.LoginDto;
import com.valpuestajorge.conecta4.app_user.dto.in.TokenDto;
import com.valpuestajorge.conecta4.app_user.repository.AppUserRepository;
import com.valpuestajorge.conecta4.errors.CustomError;
import com.valpuestajorge.conecta4.security.jwt.JwtProvider;
import com.valpuestajorge.conecta4.shared.patch_compare.PatchComparePort;
import com.valpuestajorge.conecta4.shared.util.UserRolesEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Getter
public class AppUserService implements AppUserServicePort {

    private final PatchComparePort patchComparePort;
    private final AppUserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public Mono<TokenDto> login(LoginDto dto) {
        return userRepository.findByUsernameOrEmail(dto.getUsername(), dto.getUsername())
                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
                .map(user -> new TokenDto(jwtProvider.generateToken(user)))
                .switchIfEmpty(Mono.error(new CustomError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "bad credentials")));
    }

    public Mono<AppUser> create(CreateUserDto dto) {
        AppUser user = AppUser.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(UserRolesEnum.ROLE_USER.name())
                .build();
        Mono<Boolean> userExists = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail()).hasElement();
        return userExists
                .flatMap(exists -> exists ?
                        Mono.error(new CustomError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "username or email already in use"))
                        : userRepository.save(user));
    }

    @Override
    public Mono<AppUser> findByUsernameOrEmail(String username, String email) {
        return getUserRepository().findByUsernameOrEmail(username, email);
    }
}