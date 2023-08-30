package com.bosonit.conecta4.repository;

import com.bosonit.conecta4.domain.Tablero;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TableroRepository extends ReactiveCrudRepository<Tablero,Integer> {
}
