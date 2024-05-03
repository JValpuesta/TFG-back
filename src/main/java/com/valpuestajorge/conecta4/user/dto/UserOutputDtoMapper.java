package com.valpuestajorge.conecta4.user.dto;

import com.valpuestajorge.conecta4.shared.generic_mappers.GenericMapper;
import com.valpuestajorge.conecta4.user.business.AppUser;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring",
        uses = {
        }
)
public abstract class UserOutputDtoMapper implements GenericMapper<AppUser, UserOutputDto> {
}
