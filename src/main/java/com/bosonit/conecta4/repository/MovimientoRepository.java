package com.bosonit.conecta4.repository;

import com.bosonit.conecta4.domain.Movimiento;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MovimientoRepository extends ReactiveCrudRepository<Movimiento,Integer> {
}
