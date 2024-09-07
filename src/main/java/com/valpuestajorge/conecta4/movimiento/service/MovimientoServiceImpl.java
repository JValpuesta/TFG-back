package com.valpuestajorge.conecta4.movimiento.service;

import com.valpuestajorge.conecta4.movimiento.business.Movimiento;
import com.valpuestajorge.conecta4.movimiento.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MovimientoServiceImpl implements MovimientoService{
    @Autowired
    MovimientoRepository movimientoRepository;
    @Override
    public Mono<Movimiento> addMovimiento(Integer tablero, Integer numJugada, Long jugador, Integer columna) {
        return movimientoRepository.save(new Movimiento(tablero, numJugada, jugador, columna));
    }
}
