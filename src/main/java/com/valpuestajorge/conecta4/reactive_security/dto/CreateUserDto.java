package com.valpuestajorge.conecta4.reactive_security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateUserDto {

    @NotBlank(message = "username is mandatory")
    private String username;
    @Email(message = "invalid email")
    private String email;
    @NotBlank(message = "password is mandatory")
    private String password;

}
