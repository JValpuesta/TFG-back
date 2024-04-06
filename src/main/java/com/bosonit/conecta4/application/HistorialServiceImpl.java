package com.bosonit.conecta4.application;

import com.bosonit.conecta4.domain.Historial;
import com.bosonit.conecta4.domain.error.EntityNotFoundException;
import com.bosonit.conecta4.repository.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
public class HistorialServiceImpl implements HistorialService{

    @Autowired
    HistorialRepository historialRepository;

    @Override
    public Mono<Historial> getHistorialById(String ipCliente) {
        return historialRepository.findByIpJugador(ipCliente)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("No se ha podido encontrar el historial de la IP "+ipCliente)));
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

