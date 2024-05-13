package com.valpuestajorge.conecta4.shared.restapibusiness.dto.out;

import com.valpuestajorge.conecta4.shared.generic_mappers.GenericOutputMapper;
import com.valpuestajorge.conecta4.shared.restapibusiness.persistance.BusinessEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

public abstract class BusinessOutputDtoMapper<B extends BusinessEntity, O extends BusinessOutputDto>
        implements GenericOutputMapper<B, O> {

    public Long getId(B business) {

        if (Objects.isNull(business))
            return null;
        return business.getId();
    }

    public abstract List<O> toListOutput(List<B> business);

    public abstract Page<O> toPageOutput(Page<B> business);

}
