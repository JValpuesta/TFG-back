package com.bosonit.conecta4.controller;

import com.bosonit.conecta4.domain.Tablero;
import com.bosonit.conecta4.repository.TableroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/partida")
public class TableroController {

    @Autowired
    private TableroRepository tableroRepository;

    @GetMapping
    public Flux<Tablero> getAllTableros(){
        return tableroRepository.findAll();
    }
    @GetMapping("/{id}")
    public Mono<Tablero> getTableroById(@PathVariable int id){
        return tableroRepository.findById(id);
    }

    @PostMapping
    public Mono<Tablero> addTablero1(@RequestBody Tablero tablero){
        return tableroRepository.save(tablero);
    }

    @PostMapping
    public Mono<Tablero> addTablero(@RequestParam String nombre, @RequestParam String ip){
        return tableroRepository.save(new Tablero(nombre, ip));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteTableroById(@PathVariable int id ){
        return tableroRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Mono<Integer> addJugador2Tablero(@RequestParam String nombre2, @RequestParam String ip2) {
        return null;
    }

    @PutMapping("/{id}")
    public Mono<Integer> addFichaTablero(@RequestBody int columna) {
        return null;
    }

}
