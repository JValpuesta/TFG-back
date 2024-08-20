package com.valpuestajorge.conecta4.app_user.controller;

import com.valpuestajorge.conecta4.app_user.service.AppUserServicePort;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "User", description = "User operations")
@RequestMapping("/v1/user")
@SecurityRequirement(name = "Bearer Authentication")
@AllArgsConstructor
@Getter
public class AppUserController {

    private final AppUserServicePort service;

}
