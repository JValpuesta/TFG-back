package com.valpuestajorge.conecta4.tablero.controller;

import com.valpuestajorge.conecta4.tablero.domain.Tablero;
import com.valpuestajorge.conecta4.tablero.service.TableroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@Tag(name = "Tablero", description = "Tablero operations")
@RequestMapping("/v1/partida")
public class TableroController {

    @Autowired
    private TableroService tableroService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/admin")
    public Flux<Tablero> getAllTableros(){
        return tableroService.getAllTableros();
    }

    @GetMapping("/{id}")
    public Mono<Tablero> getTableroById(@PathVariable Integer id){
        return tableroService.getTableroById(id);
    }

    @PostMapping
    public Mono<Tablero> addTablero(@RequestParam Long userId){
        return tableroService.addTablero(userId);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteTableroById(@PathVariable Integer id ){
        return tableroService.deleteTableroById(id);
    }

    @PutMapping("/{id}")
    public Mono<Tablero> addJugador2Tablero(@PathVariable Integer id, @RequestParam Long userId) {
        simpMessagingTemplate.convertAndSend("/topic/tablero/" + id, "Jugador 2 se ha conectado");
        return tableroService.addJugador2Tablero(id, userId);
    }

    @PutMapping("/conecta4/{id}")
    public Mono<Tablero> addFichaTablero(@PathVariable Integer id, @RequestParam Integer columna, @RequestParam Integer idJugador) {

        return tableroService.addFichaTablero(id, columna).map((t)->{
            if(idJugador == 1){
                simpMessagingTemplate.convertAndSend("/topic/tablero/" + id + "/" + 2, Objects.requireNonNull(t));
            }else {
                simpMessagingTemplate.convertAndSend("/topic/tablero/" + id + "/" + 1, Objects.requireNonNull(t));
            }
            return t;
        });
    }

}
