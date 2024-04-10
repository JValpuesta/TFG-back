package com.vapuestajorge.conecta4.movimiento.repository;

import com.vapuestajorge.conecta4.movimiento.business.Movimiento;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MovimientoRepository extends ReactiveCrudRepository<Movimiento,Integer> {
}
