package com.valpuestajorge.conecta4.movimiento.service;

import com.valpuestajorge.conecta4.movimiento.business.Movimiento;
import com.valpuestajorge.conecta4.tablero.domain.Tablero;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface MovimientoService {
    Mono<Movimiento> addMovimiento(Tablero tablero, int columna);
}
