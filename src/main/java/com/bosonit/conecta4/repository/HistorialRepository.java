package com.bosonit.conecta4.repository;

import com.bosonit.conecta4.domain.Historial;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface HistorialRepository extends ReactiveCrudRepository<Historial,Integer> {

    public Mono<Historial> findByIpJugador(String ipCliente);

}
