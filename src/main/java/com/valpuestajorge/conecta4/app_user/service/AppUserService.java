package com.valpuestajorge.conecta4.app_user.service;

import com.valpuestajorge.conecta4.app_user.domain.AppUser;
import com.valpuestajorge.conecta4.app_user.repository.AppUserRepository;
import com.valpuestajorge.conecta4.errors.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Getter
public class AppUserService implements AppUserServicePort {

    private final AppUserRepository repo;

    @Override
    public Flux<AppUser> getAllUsers() {
        return getRepo().findAll();
    }

    @Override
    public Mono<AppUser> getUserById(Long id) {
        return getRepo().findById(id).
                switchIfEmpty(Mono.error(new NotFoundException("No se ha podido encontrar la partida " + id)));
    }

    @Override
    public Mono<AppUser> addUser(String username, String ip) {
        return getRepo().save(new AppUser(username, ip));
    }

    @Override
    public Mono<Void> deleteUserById(Long id) {
        return getRepo().deleteById(id);
    }
}