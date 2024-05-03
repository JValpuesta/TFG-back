package com.valpuestajorge.conecta4.shared.generic_mappers;

import java.util.List;
import java.util.Set;

/**
 * Generic mapper that converts from the persistence entity to the domain entity.
 *
 * @param <E1> The class for the first entity layer.
 * @param <E2> The class for the second entity layer.
 */
public interface GenericInputMapper<E1, E2> {
    E1 toFirst(E2 persistence);
    List<E1> toFirst(List<E2> persistence);
    Set<E1> toFirst(Set<E2> persistence);
}
