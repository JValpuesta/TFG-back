package com.valpuestajorge.conecta4.tablero.service;

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
    Mono<Tablero> addTablero(String nombre, String ip);
    Mono<Tablero> addJugador2Tablero(Integer id, String nombre, String ip);
    Mono<Tablero> addFichaTablero(int id, int columna);
    Mono<Void> deleteTableroById(int id);
}
