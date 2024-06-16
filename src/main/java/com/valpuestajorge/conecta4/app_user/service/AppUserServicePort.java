package com.valpuestajorge.conecta4.app_user.service;

import com.valpuestajorge.conecta4.app_user.entity.business.AppUser;
import com.valpuestajorge.conecta4.shared.restapibusiness.service.BusinessServicePort;
import reactor.core.publisher.Mono;

public interface AppUserServicePort extends BusinessServicePort<AppUser> {

    Mono<AppUser> findByUsernameOrEmail(String username, String email);
}
