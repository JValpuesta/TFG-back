package com.valpuestajorge.conecta4.user.business;

import com.valpuestajorge.conecta4.shared.restapibusiness.persistance.BusinessEntity;
import com.valpuestajorge.conecta4.shared.util.NationalityEnum;
import com.valpuestajorge.conecta4.shared.util.UserRolesEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUser extends BusinessEntity {

    @Column
    private String email;
    @Column
    private String username;
    @Column
    private String login;
    @Column
    private String password;
    @Column
    private LocalDateTime lastLogin;
    @Column
    private Boolean userAvailableFlag;
    @Column
    private Boolean requiredPasswordChangeFlag;
    @Column
    private Boolean accountNotLocked;
    @Column
    private Boolean accountNotExpired;
    @Column
    private Boolean credentialNotExpired;
    @Column
    private String temporaryPassword;
    @Column
    private String configurations;
    @Column
    private String motiveFailedLogin;
    @Column
    private UserRolesEnum userRole;
    @Column
    private NationalityEnum nationality;
}
