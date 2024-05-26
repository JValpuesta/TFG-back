package com.valpuestajorge.conecta4.user.dto;

import com.valpuestajorge.conecta4.shared.restapibusiness.dto.out.BusinessOutputDtoMapper;
import com.valpuestajorge.conecta4.user.entity.business.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AppUserOutputDtoMapper extends BusinessOutputDtoMapper<AppUser, AppUserOutputDto> {
}
