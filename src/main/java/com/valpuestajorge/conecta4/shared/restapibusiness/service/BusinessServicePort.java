package com.valpuestajorge.conecta4.shared.restapibusiness.service;

import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.errors.UnprocessableEntityException;
import com.valpuestajorge.conecta4.shared.restapibusiness.entity.business.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Map;

public interface BusinessServicePort<B extends Business> {

    Mono<B> post(B entity) throws UnprocessableEntityException, NotFoundException;

    Mono<B> getById(Long id);

    Mono<Page<B>> getAll(Pageable pageable);

    Mono<Void> delete(Long id) throws NotFoundException, UnprocessableEntityException;

    Mono<B> update(B businessEntity) throws NotFoundException;

    Mono<B> patch(Long id, Map<String, Object> inputMap) throws UnprocessableEntityException, NotFoundException, ParseException, ClassNotFoundException;

}