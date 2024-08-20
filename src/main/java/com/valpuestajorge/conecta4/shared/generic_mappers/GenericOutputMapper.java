package com.valpuestajorge.conecta4.shared.generic_mappers;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

/**
 * Generic mapper that converts from the persistence entity to the domain entity.
 *
 * @param <E1> The class for the first entity layer.
 * @param <E2> The class for the second entity layer.
 */
public interface GenericOutputMapper<E1, E2> {
    E2 toSecond(E1 domain);
    List<E2> toSecondList(List<E1> domain);
    //Page<E2> toSecond(Page<E1> domain);
}
