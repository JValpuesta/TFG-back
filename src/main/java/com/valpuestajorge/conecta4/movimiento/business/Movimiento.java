package com.valpuestajorge.conecta4.movimiento.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "movimiento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movimiento {
    @Id
    private Integer idMovimiento;
    @Column
    private Integer tablero;
    @Column
    private Integer numJugada;
    @Column
    private Long jugador;
    @Column
    private LocalDateTime fechaHora;
    @Column
    private Integer columna;

    public Movimiento(Integer tablero, Integer numJugada, Long jugador, Integer columna) {
        this.tablero = tablero;
        this.numJugada = numJugada;
        this.jugador = jugador;
        this.fechaHora = LocalDateTime.now();
        this.columna = columna;
    }
}
