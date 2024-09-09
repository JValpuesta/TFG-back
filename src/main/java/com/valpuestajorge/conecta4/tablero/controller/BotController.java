package com.valpuestajorge.conecta4.tablero.controller;

import com.valpuestajorge.conecta4.app_user.dto.in.AppUserInputDto;
import com.valpuestajorge.conecta4.tablero.domain.Tablero;
import com.valpuestajorge.conecta4.tablero.service.BotService;
import com.valpuestajorge.conecta4.tablero.service.TableroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Tag(name = "Bot", description = "Bot operations")
@RequestMapping("/v1/bot")
public class BotController {
    @Autowired
    private BotService botService;
    @Autowired
    private TableroService tableroService;
    public static final String BOT_NAME = "bot";
    @PostMapping
    public Mono<Tablero> addTableroBot(@RequestBody AppUserInputDto userInputDto) {
        return tableroService.addTablero(userInputDto.getUsername(), userInputDto.getIp())
                .flatMap(tablero -> tableroService.addJugador2Tablero(tablero.getIdTablero(), BOT_NAME, BOT_NAME));
    }

    @PutMapping("/{id}")
    public Mono<Tablero> addFichaTableroBot(@PathVariable Integer id, @RequestParam Integer columna) {
        return tableroService.addFichaTablero(id, columna)
                .flatMap(tablero -> {
                    int columnaMinMax = botService.mejorMovimiento(tablero.getPosicion());
                    return tableroService.addFichaTablero(id, columnaMinMax);
                });
    }

}
