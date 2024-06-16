package com.valpuestajorge.conecta4.reactive_security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valpuestajorge.conecta4.shared.restapibusiness.entity.persistence.BusinessEntity;
import com.valpuestajorge.conecta4.shared.util.NationalityEnum;
import com.valpuestajorge.conecta4.shared.util.UserRolesEnum;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Table(name = "app_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Builder
public class AppUser extends BusinessEntity implements UserDetails {

    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "login", nullable = false)
    private String login;
    @JsonIgnore
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
    @Column(name = "account_non_locked")
    private Boolean isAccountNonLocked;
    @Column(name = "account_non_expired")
    private Boolean isAccountNonExpired;
    @Column(name = "credential_non_expired")
    private Boolean isCredentialsNonExpired;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(userRole.getDescription()).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isCredentialsNonExpired && isAccountNonLocked && isAccountNonExpired;
    }
}
