package com.valpuestajorge.conecta4.reactive_security.repository;

import com.valpuestajorge.conecta4.reactive_security.entity.AppUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<AppUser, Integer> {

    Mono<AppUser> findByUsernameOrEmail(String username, String email);

}
