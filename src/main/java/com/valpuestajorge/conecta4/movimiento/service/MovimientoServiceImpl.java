package com.valpuestajorge.conecta4.movimiento.service;

import com.valpuestajorge.conecta4.movimiento.business.Movimiento;
import com.valpuestajorge.conecta4.movimiento.repository.MovimientoRepository;
import com.valpuestajorge.conecta4.tablero.business.Tablero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class MovimientoServiceImpl implements MovimientoService{
    @Autowired
    MovimientoRepository movimientoRepository;
    @Override
    public Mono<Movimiento> addMovimiento(Tablero tablero, int columna) {
        String nombreJugador = (tablero.getTurno() % 2 == 0) ? tablero.getNombreJugador1() : tablero.getNombreJugador2();
        String ipCliente = (tablero.getTurno() % 2 == 0) ? tablero.getIpCliente1() : tablero.getIpCliente2();
        return movimientoRepository.save(new Movimiento(tablero.getIdTablero(), tablero.getTurno(), nombreJugador,
                new Date(), columna, ipCliente));
    }
}
