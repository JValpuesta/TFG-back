package com.valpuestajorge.conecta4.user.repository;

import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.user.business.AppUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<AppUser,Integer> {

    Mono<AppUser> findUserByLogin(String login) throws NotFoundException;
    Mono<AppUser> findUserByUsername(String login) throws NotFoundException;
    Boolean existsByLogin(String login);

}