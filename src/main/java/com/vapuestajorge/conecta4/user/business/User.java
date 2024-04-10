package com.vapuestajorge.conecta4.user.business;

import com.vapuestajorge.conecta4.user.util.NationalityEnum;
import com.vapuestajorge.conecta4.user.util.UserRolesEnum;
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
public class User {

    @Id
    private Integer id;
    @Column
    private String email;
    @Column
    private String name;
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
