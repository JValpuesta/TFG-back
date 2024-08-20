package com.valpuestajorge.conecta4.app_user.service;

import com.valpuestajorge.conecta4.app_user.domain.AppUser;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface AppUserServicePort {

    Mono<AppUser> findByUsernameOrEmail(String username, String email);
}
