package com.valpuestajorge.conecta4.movimiento.service;

import com.valpuestajorge.conecta4.app_user.domain.AppUser;
import com.valpuestajorge.conecta4.movimiento.business.Movimiento;
import com.valpuestajorge.conecta4.movimiento.repository.MovimientoRepository;
import com.valpuestajorge.conecta4.tablero.domain.Tablero;
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
        AppUser jugador = (tablero.getTurno() % 2 == 0) ? tablero.getUser1() : tablero.getUser2();
        return movimientoRepository.save(new Movimiento(tablero, tablero.getTurno(), jugador,
                new Date(), columna));
    }
}
