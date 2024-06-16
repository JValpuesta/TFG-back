package com.valpuestajorge.conecta4.shared.restapibusiness.entity;


import com.valpuestajorge.conecta4.shared.generic_mappers.GenericMapper;
import com.valpuestajorge.conecta4.shared.restapibusiness.entity.business.Business;
import com.valpuestajorge.conecta4.shared.restapibusiness.entity.persistence.BusinessEntity;

public abstract class BusinessEntityMapper<B extends Business, P extends BusinessEntity> implements GenericMapper<B, P> {
}
