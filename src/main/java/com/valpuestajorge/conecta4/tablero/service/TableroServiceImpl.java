package com.valpuestajorge.conecta4.tablero.service;

import com.valpuestajorge.conecta4.app_user.domain.AppUser;
import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.errors.UnprocessableEntityException;
import com.valpuestajorge.conecta4.tablero.domain.Tablero;
import com.valpuestajorge.conecta4.tablero.repository.TableroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
public class TableroServiceImpl implements TableroService {

    private final static Integer MAX_TURNOS = 42;

    @Autowired
    TableroRepository tableroRepository;

    @Override
    public Flux<Tablero> getAllTableros() {
        return tableroRepository.findAll();
    }

    @Override
    public Flux<Tablero> getAllTablerosById(List<Integer> lista) {
        return tableroRepository.findAllById(lista);
    }

    @Override
    public Mono<Tablero> getTableroById(int id) {
        return tableroRepository.findById(id).
                switchIfEmpty(Mono.error(new NotFoundException("No se ha podido encontrar la partida " + id)));
    }

    @Override
    public Mono<Tablero> addTablero(AppUser appUser) {
        return tableroRepository.save(new Tablero(appUser));
    }

    @Override
    public Mono<Void> deleteTableroById(int id) {
        return tableroRepository.deleteById(id);
    }

    @Override
    public Mono<Tablero> addJugador2Tablero(int id, AppUser appUser) {
        Mono<Tablero> tableroMono = tableroRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("No se ha podido encontrar la partida " + id)));
        Mono<Tablero> tableroMono1 = tableroMono.handle((t, sink) -> {
            if (Objects.nonNull(t.getUser2())) {
                sink.error(new UnprocessableEntityException("La partida " + id + " ya está empezada"));
            } else {
                sink.next(t);
            }
        });
        return tableroMono1.map((t) -> {
            t.setUser2(appUser);
            return t;
        }).flatMap(t -> tableroRepository.save(t));
    }

    @Override
    public Mono<Tablero> addFichaTablero(int id, int columna) {
        Mono<Tablero> tableroMono = tableroRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("No se ha podido encontrar la partida " + id)));
      return tableroMono.map((t) -> {
            t.anyadirFicha(columna);
            return t;
        }).flatMap(t -> tableroRepository.save(t));
    }

    @Override
    public Mono<Tablero> addMovimientoToHistorial(int id, int idMovimiento){
        Mono<Tablero> tableroMono = tableroRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("No se ha podido encontrar la partida " + id)));
        return tableroMono.map((t) -> {
            t.addIntToHistorial(idMovimiento);
            return t;
        }).flatMap(t -> tableroRepository.save(t));
    }
}