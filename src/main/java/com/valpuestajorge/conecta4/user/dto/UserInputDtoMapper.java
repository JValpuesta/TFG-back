package com.valpuestajorge.conecta4.user.dto;

import com.valpuestajorge.conecta4.shared.generic_mappers.GenericInputMapper;
import com.valpuestajorge.conecta4.user.business.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {
        }
)
public abstract class UserInputDtoMapper implements GenericInputMapper<AppUser, UserInputDto> {
}