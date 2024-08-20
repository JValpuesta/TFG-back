package com.valpuestajorge.conecta4.app_user.service;

import com.valpuestajorge.conecta4.app_user.domain.AppUser;
import com.valpuestajorge.conecta4.app_user.repository.AppUserRepository;
import com.valpuestajorge.conecta4.shared.patch_compare.PatchComparePort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Getter
public class AppUserService implements AppUserServicePort {

    private final AppUserRepository repo;
    private final PatchComparePort patchComparePort;

    @Override
    public Mono<AppUser> findByUsernameOrEmail(String username, String email) {
        return getRepo().findByUsernameOrEmail(username, email);
    }
}