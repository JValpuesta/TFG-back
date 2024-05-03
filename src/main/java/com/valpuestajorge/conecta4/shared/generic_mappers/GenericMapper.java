package com.valpuestajorge.conecta4.shared.generic_mappers;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Generic mapper that converts from the persistence entity to the domain entity.
 *
 * @param <E1> The class for the first entity layer.
 * @param <E2> The class for the second entity layer.
 */
public interface GenericMapper<E1, E2> {
    Mono<E1> toFirst(Mono<E2> persistence);
    Flux<E1> toFirst(Flux<E2> persistence);
    Mono<E2> toSecond(Mono<E1> domain);
    Flux<E2> toSecond(Flux<E1> domain);
}
