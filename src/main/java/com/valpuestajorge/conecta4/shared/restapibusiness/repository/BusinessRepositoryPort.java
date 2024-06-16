package com.valpuestajorge.conecta4.shared.restapibusiness.repository;

import com.valpuestajorge.conecta4.shared.restapibusiness.entity.business.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface BusinessRepositoryPort<B extends Business> {

    Mono<B> save(B entity);

    Mono<Void> deleteById(Long id);

    Mono<B> getById(Long id);

    Mono<Page<B>> getAll(Pageable pageable);

}
