package com.valpuestajorge.conecta4.configuration;

import com.valpuestajorge.conecta4.shared.util.UserRolesEnum;
import com.valpuestajorge.conecta4.user.entity.business.AppUser;
import com.valpuestajorge.conecta4.user.repository.UserRepository;
import com.valpuestajorge.conecta4.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class CreateSuperAdmin {
    private static final String SUPER_ADMIN = "adminc4";
    private static final String PASSWORD = "adminc4";
    private static final String EMAIL = "administrator@nuvex.com";

    private final UserService userService;

    @PostConstruct
    public void createSuperAdminUser() {
        userService.getByLogin(SUPER_ADMIN)
                .switchIfEmpty(createAdminUser())
                .subscribe(
                        user -> log.info("Super admin user exists or has been created successfully: {}", user),
                        error -> log.error("Error creating super admin user", error)
                );
    }

    private Mono<AppUser> createAdminUser() {
        AppUser user = new AppUser();
        user.setCreatedDate(LocalDateTime.now());
        user.setLogin(SUPER_ADMIN);
        user.setUsername(SUPER_ADMIN);
        user.setPassword(PASSWORD);
        user.setEmail(EMAIL);
        user.setUserRole(UserRolesEnum.ADMIN);
        user.setIsAccountNonLocked(true);
        user.setIsAccountNonExpired(true);
        user.setIsCredentialsNonExpired(true);
        user.setTemporaryPassword("");
        user.setConfigurations("");
        user.setRequiredPasswordChangeFlag(false);
        return userService.post(user);
    }
}
