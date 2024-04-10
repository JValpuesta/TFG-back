package com.vapuestajorge.conecta4.user.service;

import com.vapuestajorge.conecta4.tablero.business.Tablero;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface UserService {

    Flux<Tablero> getAllTableros();
    Flux<Tablero> getAllTablerosById(List<Integer> lista);
    Mono<Tablero> getTableroById(int id);
    Mono<Tablero> addTablero(String nombre,String ip);
    Mono<Void> deleteTableroById(int id);
    Mono<Tablero> addJugador2Tablero(int id, String nombre2, String ip2);
    Mono<Tablero> addFichaTablero(int id, int columna);

    Mono<Tablero> addMovimientoToHistorial(int id, int idMovimiento);
}
