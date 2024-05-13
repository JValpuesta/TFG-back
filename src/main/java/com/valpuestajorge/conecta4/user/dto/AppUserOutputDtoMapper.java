package com.valpuestajorge.conecta4.user.dto;

import com.valpuestajorge.conecta4.shared.restapibusiness.dto.out.BusinessOutputDtoMapper;
import com.valpuestajorge.conecta4.user.business.AppUser;
import com.valpuestajorge.conecta4.user.service.UserService;
import lombok.Getter;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AppUserOutputDtoMapper extends BusinessOutputDtoMapper<AppUser, AppUserOutputDto> {

        @Autowired
        @Getter
        private UserService service;
}
