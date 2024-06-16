package com.valpuestajorge.conecta4.app_user.dto.out;

import com.valpuestajorge.conecta4.app_user.entity.business.AppUser;
import com.valpuestajorge.conecta4.shared.restapibusiness.dto.out.BusinessOutputDtoMapper;
import org.mapstruct.Mapper;

@Mapper
public abstract class AppUserOutputDtoMapper extends BusinessOutputDtoMapper<AppUser, AppUserOutputDto> {
}