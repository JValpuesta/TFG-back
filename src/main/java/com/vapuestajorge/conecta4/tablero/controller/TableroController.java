package com.vapuestajorge.conecta4.tablero.controller;

import com.vapuestajorge.conecta4.historial.service.HistorialService;
import com.vapuestajorge.conecta4.movimiento.service.MovimientoService;
import com.vapuestajorge.conecta4.tablero.service.TableroService;
import com.vapuestajorge.conecta4.historial.business.Historial;
import com.vapuestajorge.conecta4.movimiento.business.Movimiento;
import com.vapuestajorge.conecta4.tablero.business.Tablero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/partida")
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
    public Mono<Tablero> addTablero(@RequestParam String nombre, @RequestParam String ip){
        return tableroService.addTablero(nombre, ip);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteTableroById(@PathVariable int id ){
        return tableroService.deleteTableroById(id);
    }

    @PutMapping("/{id}")
    public Mono<Tablero> addJugador2Tablero(@PathVariable int id, @RequestParam String nombre2, @RequestParam String ip2) {
        simpMessagingTemplate.convertAndSend("/topic/tablero/" + id, "Jugador 2 se ha conectado");
        return tableroService.addJugador2Tablero(id, nombre2, ip2);
    }

    @PutMapping("/conecta4/{id}")
    public Mono<Tablero> addFichaTablero(@PathVariable int id, @RequestParam int columna, @RequestParam int idJugador) {
        Movimiento movimiento = movimientoService.addMovimiento(tableroService.getTableroById(id).block(), columna)
                .block();
        tableroService.addMovimientoToHistorial(id, Objects.requireNonNull(movimiento).getIdMovimiento()).block();

        return tableroService.addFichaTablero(id, columna).map((t)->{
            if(!t.getGanador().isEmpty()){
                historialService.addPartidaHistorialByIp(t.getIpCliente1(), id).subscribe();
                historialService.addPartidaHistorialByIp(t.getIpCliente2(), id).subscribe();
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
