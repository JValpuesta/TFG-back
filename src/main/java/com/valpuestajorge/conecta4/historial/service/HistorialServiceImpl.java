package com.valpuestajorge.conecta4.historial.service;

import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.historial.repository.HistorialRepository;
import com.valpuestajorge.conecta4.historial.business.Historial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class HistorialServiceImpl implements HistorialService{

    @Autowired
    HistorialRepository historialRepository;

    @Override
    public Mono<Historial> getHistorialById(String ipCliente) {
        return historialRepository.findByIpJugador(ipCliente)
                .switchIfEmpty(Mono.error(new NotFoundException("No se ha podido encontrar el historial de la IP "+ipCliente)));
    }

    @Override
    public Mono<Historial> addPartidaHistorialByIp(String ipCliente, int idPartida) {
        return historialRepository.findByIpJugador(ipCliente)
                .switchIfEmpty(historialRepository.save(new Historial(ipCliente)))
                .map(((historial -> {
                    historial.getHistorialPartidas().add(idPartida);
                    return historial;
                }))).flatMap((h)->historialRepository.save(h));
    }

}

