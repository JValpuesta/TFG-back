package com.bosonit.conecta4.application;

import com.bosonit.conecta4.domain.Movimiento;
import com.bosonit.conecta4.domain.Tablero;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface MovimientoService {
    Mono<Movimiento> addMovimiento(Tablero tablero, int columna);
}
