package com.valpuestajorge.conecta4.app_user.entity;

import com.valpuestajorge.conecta4.app_user.entity.business.AppUser;
import com.valpuestajorge.conecta4.app_user.entity.persistence.AppUserEntity;
import com.valpuestajorge.conecta4.shared.restapibusiness.entity.BusinessEntityMapper;
import org.mapstruct.Mapper;

@Mapper
public abstract class AppUserEntityMapper extends BusinessEntityMapper<AppUser, AppUserEntity> {
}
