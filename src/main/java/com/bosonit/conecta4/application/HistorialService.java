package com.bosonit.conecta4.application;

import com.bosonit.conecta4.domain.Historial;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface HistorialService {

    Mono<Historial> getHistorialById(String ipCliente);
    Mono<Historial> addPartidaHistorialByIp(String ipCliente, int ip);

}
