package com.valpuestajorge.conecta4.configuration;

import com.valpuestajorge.conecta4.user.business.AppUser;
import com.valpuestajorge.conecta4.user.service.UserService;
import com.valpuestajorge.conecta4.shared.util.UserRolesEnum;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class CreateSuperadmin {
    private static final String SUPER_ADMIN = "adminc4";
    private static final String PASSWORD = "adminc4";
    private static final String EMAIL = "administrator@nuvex.com";

    private final UserService userService;

    @PostConstruct
    public void createSuperadminUser() {
        userService.getByLogin(SUPER_ADMIN)
                .switchIfEmpty(createAdminUser())
                .subscribe(
                        user -> log.info("Usuario admin creado"),
                        error -> log.error("Error al crear el usuario admin: {}", error.getMessage())
                );
    }

    private Mono<AppUser> createAdminUser() {
        AppUser user = new AppUser();
        user.setLogin(SUPER_ADMIN);
        user.setUsername(SUPER_ADMIN);
        user.setPassword(PASSWORD);
        user.setEmail(EMAIL);
        user.setUserRole(UserRolesEnum.ADMIN);
        user.setAccountNotLocked(true);
        user.setAccountNotExpired(true);
        user.setCredentialNotExpired(true);
        user.setTemporaryPassword("");
        user.setConfigurations("");
        user.setRequiredPasswordChangeFlag(false);

        return userService.post(user);
    }
}
