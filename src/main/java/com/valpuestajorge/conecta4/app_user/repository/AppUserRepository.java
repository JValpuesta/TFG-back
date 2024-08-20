package com.valpuestajorge.conecta4.app_user.repository;

import com.valpuestajorge.conecta4.app_user.domain.AppUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AppUserRepository extends ReactiveCrudRepository<AppUser,Integer> {

    Mono<AppUser> findByUsernameOrEmail(String username, String email);

}