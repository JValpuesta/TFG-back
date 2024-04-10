package com.vapuestajorge.conecta4.tablero.repository;

import com.vapuestajorge.conecta4.tablero.business.Tablero;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TableroRepository extends ReactiveCrudRepository<Tablero,Integer> {
}