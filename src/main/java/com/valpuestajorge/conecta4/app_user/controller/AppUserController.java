package com.valpuestajorge.conecta4.app_user.controller;

import com.valpuestajorge.conecta4.app_user.dto.in.AppUserInputDto;
import com.valpuestajorge.conecta4.app_user.dto.in.AppUserInputDtoMapper;
import com.valpuestajorge.conecta4.app_user.dto.out.AppUserOutputDto;
import com.valpuestajorge.conecta4.app_user.dto.out.AppUserOutputDtoMapper;
import com.valpuestajorge.conecta4.app_user.entity.business.AppUser;
import com.valpuestajorge.conecta4.app_user.service.AppUserServicePort;
import com.valpuestajorge.conecta4.shared.restapibusiness.controller.BusinessController;
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
public class AppUserController extends BusinessController<AppUser, AppUserOutputDto, AppUserInputDto> {

    private final AppUserServicePort service;
    private final AppUserInputDtoMapper inputMapper;
    private final AppUserOutputDtoMapper outputMapper;

}
