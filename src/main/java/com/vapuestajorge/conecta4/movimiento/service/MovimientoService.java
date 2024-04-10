package com.vapuestajorge.conecta4.movimiento.service;

import com.vapuestajorge.conecta4.movimiento.business.Movimiento;
import com.vapuestajorge.conecta4.tablero.business.Tablero;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface MovimientoService {
    Mono<Movimiento> addMovimiento(Tablero tablero, int columna);
}
