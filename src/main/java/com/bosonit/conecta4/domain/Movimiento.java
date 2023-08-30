package com.bosonit.conecta4.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Table
@Getter
@Setter
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

}
