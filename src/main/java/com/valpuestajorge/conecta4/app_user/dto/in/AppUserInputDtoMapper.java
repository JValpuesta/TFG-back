package com.valpuestajorge.conecta4.app_user.dto.in;

import com.valpuestajorge.conecta4.app_user.entity.business.AppUser;
import com.valpuestajorge.conecta4.shared.restapibusiness.dto.in.BusinessInputDtoMapper;
import org.mapstruct.Mapper;

@Mapper
public abstract class AppUserInputDtoMapper extends BusinessInputDtoMapper<AppUser, AppUserInputDto> {
}
