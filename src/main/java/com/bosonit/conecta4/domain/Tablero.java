package com.bosonit.conecta4.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tablero{

    @Id
    private Integer idTablero;
    private String nombreJugador1;
    private String ipCliente1;
    private String nombreJugador2;
    private String ipCliente2;
    //private int[][] posicion = new int[6][7]; //0 -> casilla vacía; 1 -> ficha amarilla; 2 -> ficha roja
    //private List<Movimiento> historial = new ArrayList<>();


}
