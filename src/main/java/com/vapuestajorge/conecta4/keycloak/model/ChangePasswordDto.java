package com.vapuestajorge.conecta4.keycloak.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ChangePasswordDto {
    private String userName;
    private String previousPassword;
    private String newPassword;
}
