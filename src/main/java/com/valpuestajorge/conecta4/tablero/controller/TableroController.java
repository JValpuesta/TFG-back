package com.valpuestajorge.conecta4.tablero.controller;

import com.valpuestajorge.conecta4.app_user.domain.AppUser;
import com.valpuestajorge.conecta4.historial.service.HistorialService;
import com.valpuestajorge.conecta4.movimiento.service.MovimientoService;
import com.valpuestajorge.conecta4.tablero.service.TableroService;
import com.valpuestajorge.conecta4.historial.business.Historial;
import com.valpuestajorge.conecta4.movimiento.business.Movimiento;
import com.valpuestajorge.conecta4.tablero.domain.Tablero;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@Tag(name = "Tablero", description = "Tablero operations")
@RequestMapping("/v1/partida")
@SecurityRequirement(name = "Bearer Authentication")
public class TableroController {

    @Autowired
    private TableroService tableroService;
    @Autowired
    private MovimientoService movimientoService;
    @Autowired
    private HistorialService historialService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/admin")
    public Flux<Tablero> getAllTableros(){
        return tableroService.getAllTableros();
    }

    @GetMapping("/{id}")
    public Mono<Tablero> getTableroById(@PathVariable int id){
        return tableroService.getTableroById(id);
    }

    @GetMapping("/perfil/{ip}")
    public Flux<Tablero> getHistorialTableros(@PathVariable String ip){
        List<Integer> lista = Objects.requireNonNull(historialService.getHistorialById(ip).block()).getHistorialPartidas();
        List<Tablero> listaTableros = new ArrayList<>();
        Flux<Tablero> fluxTablero = tableroService.getAllTablerosById(lista)
                .map((tablero -> {
                    listaTableros.add(tablero);
                    return tablero;
                }));
        simpMessagingTemplate.convertAndSend("/topic/historial/" + listaTableros);
        return fluxTablero;
    }

    @PostMapping("/perfil/{ip}")
    public Mono<Historial> addHistorial(@PathVariable String ip, @RequestParam int idPartida){
        return historialService.addPartidaHistorialByIp(ip, idPartida);
    }

    @PostMapping
    public Mono<Tablero> addTablero(@RequestParam AppUser user){
        return tableroService.addTablero(user);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteTableroById(@PathVariable int id ){
        return tableroService.deleteTableroById(id);
    }

    @PutMapping("/{id}")
    public Mono<Tablero> addJugador2Tablero(@PathVariable int id, @RequestParam AppUser user) {
        simpMessagingTemplate.convertAndSend("/topic/tablero/" + id, "Jugador 2 se ha conectado");
        return tableroService.addJugador2Tablero(id, user);
    }

    @PutMapping("/conecta4/{id}")
    public Mono<Tablero> addFichaTablero(@PathVariable int id, @RequestParam int columna, @RequestParam int idJugador) {
        Movimiento movimiento = movimientoService.addMovimiento(tableroService.getTableroById(id).block(), columna)
                .block();
        tableroService.addMovimientoToHistorial(id, Objects.requireNonNull(movimiento).getIdMovimiento()).block();

        return tableroService.addFichaTablero(id, columna).map((t)->{
            if(!Objects.nonNull(t.getGanador())){
                historialService.addPartidaHistorialByIp(t.getUser1().getIp(), id).subscribe();
                historialService.addPartidaHistorialByIp(t.getUser2().getIp(), id).subscribe();
            }
            if(idJugador == 1){
                simpMessagingTemplate.convertAndSend("/topic/tablero/" + id + "/" + 2, Objects.requireNonNull(t));
            }else {
                simpMessagingTemplate.convertAndSend("/topic/tablero/" + id + "/" + 1, Objects.requireNonNull(t));
            }
            return t;
        });
    }

}
