package com.valpuestajorge.conecta4.shared.restapibusiness.repository;

import com.valpuestajorge.conecta4.shared.restapibusiness.entity.BusinessEntityMapper;
import com.valpuestajorge.conecta4.shared.restapibusiness.entity.business.Business;
import com.valpuestajorge.conecta4.shared.restapibusiness.entity.persistence.BusinessEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public abstract class BusinessRepository<B extends Business, P extends BusinessEntity> implements BusinessRepositoryPort<B> {

    protected abstract BusinessReactiveRepository<P> getBusinessReactiveRepository();

    protected abstract BusinessEntityMapper<B, P> getBusinessEntityMapper();

    @Override
    public Mono<B> save(B entity) {
        return getBusinessEntityMapper()
                .toFirst(getBusinessReactiveRepository().save(getBusinessEntityMapper().toSecond(entity)));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return getBusinessReactiveRepository().deleteById(id);
    }

    @Override
    public Mono<B> getById(Long id) {
        return getBusinessEntityMapper().toFirst(getBusinessReactiveRepository().findById(id));
    }

    @Override
    public Mono<Page<B>> getAll(Pageable pageable) {
        return getBusinessReactiveRepository().findAll()
                .collectList()
                .zipWith(getBusinessReactiveRepository().count())
                .map(tuple -> {
                    List<B> allEntities = getBusinessEntityMapper().toFirst(tuple.getT1());
                    long totalElements = tuple.getT2();
                    int start = (int) pageable.getOffset();
                    int end = Math.min((start + pageable.getPageSize()), allEntities.size());
                    List<B> subList = allEntities.subList(start, end);
                    return new PageImpl<>(subList, pageable, totalElements);
                });
    }
}