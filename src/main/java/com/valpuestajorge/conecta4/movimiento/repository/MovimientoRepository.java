package com.valpuestajorge.conecta4.movimiento.repository;

import com.valpuestajorge.conecta4.movimiento.business.Movimiento;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MovimientoRepository extends ReactiveCrudRepository<Movimiento,Integer> {
}
