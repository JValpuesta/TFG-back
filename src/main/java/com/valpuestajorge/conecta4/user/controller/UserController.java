package com.valpuestajorge.conecta4.user.controller;

import com.valpuestajorge.conecta4.shared.restapibusiness.controller.BusinessController;
import com.valpuestajorge.conecta4.user.business.AppUser;
import com.valpuestajorge.conecta4.user.dto.AppUserInputDto;
import com.valpuestajorge.conecta4.user.dto.AppUserOutputDtoMapper;
import com.valpuestajorge.conecta4.user.dto.AppUserInputDtoMapper;
import com.valpuestajorge.conecta4.user.dto.AppUserOutputDto;
import com.valpuestajorge.conecta4.user.service.UserService;
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
public class UserController extends BusinessController<AppUser, AppUserOutputDto, AppUserInputDto> {

    private final UserService service;

    private final AppUserOutputDtoMapper outputMapper;

    private final AppUserInputDtoMapper inputMapper;

}