package com.bosonit.conecta4.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movimiento {

    @Id
    private Integer idMovimiento;
    private Integer idTablero;
    private Integer numJugada;
    private String nombreJugador;
    private Date fechaHora;
    private Integer columna;
    private String ipCliente;

    public Movimiento(Integer idTablero, Integer numJugada, String nombreJugador, Date fechaHora, Integer columna, String ipCliente){

        this.idTablero = idTablero;
        this.numJugada = numJugada;
        this.nombreJugador = nombreJugador;
        this.fechaHora = fechaHora;
        this.columna = columna;
        this.ipCliente = ipCliente;

    }

    public Movimiento(Integer idTablero, Integer columna){

        this.idTablero = idTablero;
        this.columna = columna;

    }
}
