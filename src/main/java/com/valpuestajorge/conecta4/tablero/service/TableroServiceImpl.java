package com.valpuestajorge.conecta4.tablero.service;

import com.valpuestajorge.conecta4.app_user.domain.AppUser;
import com.valpuestajorge.conecta4.app_user.repository.AppUserRepository;
import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.errors.UnprocessableEntityException;
import com.valpuestajorge.conecta4.movimiento.service.MovimientoService;
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

    @Autowired
    TableroRepository tableroRepository;
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    MovimientoService movimientoService;

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
    public Mono<Tablero> addTablero(String nombre, String ip) {
        return appUserRepository.findByUsernameAndIp(nombre, ip)
                .switchIfEmpty(
                        appUserRepository.save(new AppUser(nombre, ip))
                )
                .flatMap(appUser -> {
                    Tablero tablero = new Tablero(appUser.getId());
                    return tableroRepository.save(tablero);
                });
    }

    @Override
    public Mono<Void> deleteTableroById(int id) {
        return tableroRepository.deleteById(id);
    }

    @Override
    public Mono<Tablero> addJugador2Tablero(Integer idTablero, String nombre, String ip) {
        return tableroRepository.findById(idTablero)
                .switchIfEmpty(Mono.error(new NotFoundException("No se ha podido encontrar la partida con ID " + idTablero)))
                .flatMap(tablero -> {
                    if (Objects.nonNull(tablero.getUser2())) {
                        return Mono.error(new UnprocessableEntityException("La partida " + idTablero + " ya está empezada."));
                    }
                    return appUserRepository.findByUsernameAndIp(nombre, ip)
                            .switchIfEmpty(
                                    appUserRepository.save(new AppUser(nombre, ip))
                            )
                            .flatMap(appUser -> {
                                if (Objects.equals(appUser.getId(), tablero.getUser1())) {
                                    return Mono.error(new UnprocessableEntityException("No puedes unirte a una partida creada por ti."));
                                } else {
                                    tablero.setUser2(appUser.getId());
                                    return tableroRepository.save(tablero);
                                }
                            });
                });
    }


    @Override
    public Mono<Tablero> addFichaTablero(int id, int columna) {
        return tableroRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("No se ha podido encontrar la partida " + id)))
                .flatMap(tablero -> {
                    if (Objects.nonNull(tablero.getGanador())) {
                        return Mono.error(new UnprocessableEntityException("La partida ya ha terminado."));
                    }
                    Long playerInTurn;
                    if (tablero.getTurno() % 2 == 0) {
                        playerInTurn = tablero.getUser1();
                    } else {
                        playerInTurn = tablero.getUser2();
                    }
                    return movimientoService.addMovimiento(id, tablero.getTurno(), playerInTurn, columna)
                            .then(tablero.anyadirFicha(columna))
                            .flatMap(updatedTablero -> tableroRepository.save(updatedTablero));
                });
    }

}