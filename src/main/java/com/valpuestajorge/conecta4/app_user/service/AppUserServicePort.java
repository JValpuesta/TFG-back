package com.valpuestajorge.conecta4.app_user.service;

import com.valpuestajorge.conecta4.app_user.domain.AppUser;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface AppUserServicePort {
    Flux<AppUser> getAllUsers();

    Mono<AppUser> getUserById(Long id);

    Mono<AppUser> addUser(String username, String ip);

    Mono<Void> deleteUserById(Long id);
}
