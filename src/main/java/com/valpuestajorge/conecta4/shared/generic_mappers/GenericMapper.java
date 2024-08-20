package com.valpuestajorge.conecta4.shared.generic_mappers;

import com.valpuestajorge.conecta4.shared.restapibusiness.entity.business.Business;
import com.valpuestajorge.conecta4.shared.restapibusiness.entity.persistence.BusinessEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Generic mapper that converts from the persistence entity to the domain entity.
 *
 * @param <E1> The class for the first entity layer.
 * @param <E2> The class for the second entity layer.
 */
public interface GenericMapper<E1, E2> {
    Mono<E1> toFirst(Mono<E2> persistence);
    Flux<E1> toFirst(Flux<E2> persistence);
    E1 toFirst(E2 persistence);
    List<E1> toFirst(List<E2> domain);
    Mono<E2> toSecond(Mono<E1> domain);
    Flux<E2> toSecond(Flux<E1> domain);
    E2 toSecond(E1 domain);
    List<E2> toSecond(List<E1> domain);
}
