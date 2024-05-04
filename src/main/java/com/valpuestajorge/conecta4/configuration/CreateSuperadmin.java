package com.valpuestajorge.conecta4.configuration;

import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.user.business.AppUser;
import com.valpuestajorge.conecta4.user.service.UserService;
import com.valpuestajorge.conecta4.user.util.UserRolesEnum;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class CreateSuperadmin {
    private static final String SUPER_ADMIN = "adminc4";
    private static final String PASSWORD = "adminc4";
    private static final String EMAIL = "administrator@nuvex.com";

    private final UserService userService;

    @PostConstruct
    public void createSuperadminUser() throws NotFoundException {
        AppUser user;
        if (Objects.nonNull(userService.getByLogin(SUPER_ADMIN).block())) {
            return;
        } else {
            user = new AppUser();
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
        }
        userService.post(user);
        log.info("Usuario admin creado");
    }
}
