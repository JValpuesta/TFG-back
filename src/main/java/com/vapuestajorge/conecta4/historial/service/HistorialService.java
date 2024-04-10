package com.vapuestajorge.conecta4.historial.service;

import com.vapuestajorge.conecta4.historial.business.Historial;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface HistorialService {

    Mono<Historial> getHistorialById(String ipCliente);
    Mono<Historial> addPartidaHistorialByIp(String ipCliente, int ip);

}
