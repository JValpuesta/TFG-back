package com.valpuestajorge.conecta4.app_user.repository;

import com.valpuestajorge.conecta4.app_user.entity.AppUserEntityMapper;
import com.valpuestajorge.conecta4.app_user.entity.business.AppUser;
import com.valpuestajorge.conecta4.app_user.entity.persistence.AppUserEntity;
import com.valpuestajorge.conecta4.shared.restapibusiness.repository.BusinessRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
@Getter
@Setter
public class AppUserRepository extends BusinessRepository<AppUser, AppUserEntity> implements AppUserRepositoryPort {

    private final AppUserReactiveRepository businessReactiveRepository;

    private final AppUserEntityMapper businessEntityMapper;

    @Override
    public Mono<AppUser> findByUsernameOrEmail(String username, String email) {
        return getBusinessReactiveRepository().findByUsernameOrEmail(username, email);
    }
}
