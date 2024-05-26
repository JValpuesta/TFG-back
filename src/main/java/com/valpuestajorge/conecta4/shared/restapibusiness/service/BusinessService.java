package com.valpuestajorge.conecta4.shared.restapibusiness.service;

import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.errors.UnprocessableEntityException;
import com.valpuestajorge.conecta4.shared.patch_compare.PatchComparePort;
import com.valpuestajorge.conecta4.shared.restapibusiness.entity.persistence.BusinessEntity;
import com.valpuestajorge.conecta4.shared.restapibusiness.repository.BusinessReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class BusinessService<B extends BusinessEntity> implements BusinessServicePort<B> {

    protected abstract BusinessReactiveRepository<B> getRepo();

    protected abstract PatchComparePort getPatchComparePort();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<B> post(B entity) throws UnprocessableEntityException, NotFoundException {
        B beforePost = beforePost(entity);
        return getRepo().save(beforePost);
    }

    protected B beforePost(B domain) {
        return domain;
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<B> getById(Long id) {

        return getRepo().findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Page<B>> getAll(Pageable pageable) {
        return getRepo().findAll().collectList()
                .flatMap(businessList -> {
                    int start = (int) pageable.getOffset();
                    int end = Math.min((start + pageable.getPageSize()), businessList.size());
                    List<B> sublist = businessList.subList(start, end);
                    return Mono.just(new PageImpl<>(sublist, pageable, businessList.size()));
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> delete(Long id) throws NotFoundException {
        return getRepo().deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Mono<B> patch(Long id, Map<String, Object> inputMap) throws NotFoundException, ParseException, ClassNotFoundException {

        Mono<B> old = getRepo().findById(id);
        return getPatchComparePort().getPatchCompare(inputMap, old).flatMap(business -> getRepo().save(business));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<B> update(B businessObject) throws NotFoundException {
        return getRepo().save(businessObject);
    }

}