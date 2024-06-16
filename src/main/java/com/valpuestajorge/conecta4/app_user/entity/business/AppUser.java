package com.valpuestajorge.conecta4.app_user.entity.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valpuestajorge.conecta4.shared.restapibusiness.entity.business.Business;
import com.valpuestajorge.conecta4.shared.restapibusiness.entity.persistence.BusinessEntity;
import com.valpuestajorge.conecta4.shared.util.NationalityEnum;
import com.valpuestajorge.conecta4.shared.util.UserRolesEnum;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class AppUser extends Business {

    private String email;
    private String username;
    private String login;
    private String password;
    private LocalDateTime lastLogin;
    private String ip;
    private Boolean userAvailableFlag;
    private Boolean requiredPasswordChangeFlag;
    private Boolean isAccountNonLocked;
    private Boolean isAccountNonExpired;
    private Boolean isCredentialsNonExpired;
    private String temporaryPassword;
    private String configurations;
    private String motiveFailedLogin;
    private UserRolesEnum userRole;
    private NationalityEnum nationality;

}