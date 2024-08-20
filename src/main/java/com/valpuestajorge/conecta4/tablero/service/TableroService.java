package com.valpuestajorge.conecta4.tablero.service;

import com.valpuestajorge.conecta4.app_user.domain.AppUser;
import com.valpuestajorge.conecta4.tablero.domain.Tablero;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface TableroService {

    Flux<Tablero> getAllTableros();
    Flux<Tablero> getAllTablerosById(List<Integer> lista);
    Mono<Tablero> getTableroById(int id);
    Mono<Tablero> addTablero(AppUser appUser);
    Mono<Void> deleteTableroById(int id);
    Mono<Tablero> addJugador2Tablero(int id, AppUser appUser);
    Mono<Tablero> addFichaTablero(int id, int columna);
    Mono<Tablero> addMovimientoToHistorial(int id, int idMovimiento);
}
