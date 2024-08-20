package com.valpuestajorge.conecta4.shared.restapibusiness.service;

import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.errors.UnprocessableEntityException;
import com.valpuestajorge.conecta4.shared.patch_compare.PatchComparePort;
import com.valpuestajorge.conecta4.shared.restapibusiness.entity.business.Business;
import com.valpuestajorge.conecta4.shared.restapibusiness.repository.BusinessRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Map;

@Slf4j
public abstract class BusinessService<B extends Business> implements BusinessServicePort<B> {

    protected abstract BusinessRepositoryPort<B> getRepo();

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

        return getRepo().getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Page<B>> getAll(Pageable pageable) {
        return getRepo().getAll(pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> delete(Long id) throws NotFoundException {
        return getRepo().deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Mono<B> patch(Long id, Map<String, Object> inputMap) throws NotFoundException, ParseException, ClassNotFoundException {

        Mono<B> old = getRepo().getById(id);
        return getPatchComparePort().getPatchCompare(inputMap, old).flatMap(business -> getRepo().save(business));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<B> update(B businessObject) throws NotFoundException {
        return getRepo().save(businessObject);
    }

}