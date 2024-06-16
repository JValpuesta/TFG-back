package com.valpuestajorge.conecta4.app_user.service;

import com.valpuestajorge.conecta4.app_user.entity.business.AppUser;
import com.valpuestajorge.conecta4.app_user.repository.AppUserRepositoryPort;
import com.valpuestajorge.conecta4.shared.patch_compare.PatchComparePort;
import com.valpuestajorge.conecta4.shared.restapibusiness.service.BusinessService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Getter
public class AppUserService extends BusinessService<AppUser> implements AppUserServicePort {

    private final AppUserRepositoryPort repo;
    private final PatchComparePort patchComparePort;

    @Override
    public Mono<AppUser> findByUsernameOrEmail(String username, String email) {
        return getRepo().findByUsernameOrEmail(username, email);
    }
}