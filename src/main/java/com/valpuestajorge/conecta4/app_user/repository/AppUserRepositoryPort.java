package com.valpuestajorge.conecta4.app_user.repository;

import com.valpuestajorge.conecta4.app_user.entity.business.AppUser;
import com.valpuestajorge.conecta4.shared.restapibusiness.repository.BusinessRepositoryPort;
import reactor.core.publisher.Mono;

public interface AppUserRepositoryPort extends BusinessRepositoryPort<AppUser> {

    Mono<AppUser> findByUsernameOrEmail(String username, String email);

}