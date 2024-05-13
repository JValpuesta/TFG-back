package com.valpuestajorge.conecta4.user.repository;

import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.shared.restapibusiness.repository.BusinessReactiveRepository;
import com.valpuestajorge.conecta4.user.business.AppUser;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends BusinessReactiveRepository<AppUser> {

    Mono<AppUser> findUserByLogin(String login) throws NotFoundException;
    Mono<AppUser> findUserByUsername(String login) throws NotFoundException;
    Boolean existsByLogin(String login);

}