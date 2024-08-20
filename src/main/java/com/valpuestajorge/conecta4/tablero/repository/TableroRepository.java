package com.valpuestajorge.conecta4.tablero.repository;

import com.valpuestajorge.conecta4.tablero.domain.Tablero;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TableroRepository extends ReactiveCrudRepository<Tablero,Integer> {
}