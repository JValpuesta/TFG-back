package com.vapuestajorge.conecta4.historial.repository;

import com.vapuestajorge.conecta4.historial.business.Historial;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface HistorialRepository extends ReactiveCrudRepository<Historial,Integer> {

    public Mono<Historial> findByIpJugador(String ipCliente);

}
