package com.valpuestajorge.conecta4.shared.restapibusiness.dto.in;

import com.valpuestajorge.conecta4.shared.generic_mappers.GenericInputMapper;
import com.valpuestajorge.conecta4.shared.restapibusiness.entity.business.Business;

public abstract class BusinessInputDtoMapper<B extends Business, I extends BusinessInputDto>
        implements GenericInputMapper<B, I> {

}
