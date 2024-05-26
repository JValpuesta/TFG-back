package com.valpuestajorge.conecta4.user.entity.business;

import com.valpuestajorge.conecta4.shared.restapibusiness.entity.persistence.BusinessEntity;
import com.valpuestajorge.conecta4.shared.util.NationalityEnum;
import com.valpuestajorge.conecta4.shared.util.UserRolesEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Table(name = "app_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class AppUser extends BusinessEntity {

    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "login", nullable = false)
    private String login;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    @Column(name = "ip")
    private String ip;
    @Column(name = "user_available_flag")
    private Boolean userAvailableFlag;
    @Column(name = "required_password_change_flag")
    private Boolean requiredPasswordChangeFlag;
    @Column(name = "account_not_locked")
    private Boolean accountNotLocked;
    @Column(name = "account_not_expired")
    private Boolean accountNotExpired;
    @Column(name = "credential_not_expired")
    private Boolean credentialNotExpired;
    @Column(name = "temporary_password")
    private String temporaryPassword;
    @Column(name = "configurations")
    private String configurations;
    @Column(name = "motive_failed_login")
    private String motiveFailedLogin;
    @Column(name = "user_role", nullable = false)
    private UserRolesEnum userRole;
    @Column(name = "nationality")
    private NationalityEnum nationality;
}
