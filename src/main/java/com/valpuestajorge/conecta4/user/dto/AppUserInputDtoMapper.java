package com.valpuestajorge.conecta4.user.dto;

import com.valpuestajorge.conecta4.shared.restapibusiness.dto.in.BusinessInputDtoMapper;
import com.valpuestajorge.conecta4.user.business.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {
        }
)
public abstract class AppUserInputDtoMapper extends BusinessInputDtoMapper<AppUser, AppUserInputDto> {
}