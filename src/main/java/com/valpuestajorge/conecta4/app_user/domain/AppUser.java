package com.valpuestajorge.conecta4.app_user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valpuestajorge.conecta4.shared.util.NationalityEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUser implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
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
    private String roles;
    @Column(name = "nationality")
    private NationalityEnum nationality;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(roles.split(", ")).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getEmail() {
        return email;
    }
}
