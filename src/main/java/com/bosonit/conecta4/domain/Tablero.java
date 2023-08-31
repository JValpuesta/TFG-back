package com.bosonit.conecta4.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tablero{

    @Id
    private Integer idTablero;
    @Column
    private String nombreJugador1;
    @Column
    private String ipCliente1;
    @Column
    private String nombreJugador2;
    @Column
    private String ipCliente2;
    @Column
    private int[][] posicion = new int[6][7]; //0 -> casilla vacía; 1 -> ficha amarilla; 2 -> ficha roja
    @Column
    private List<Integer> historial = new ArrayList<>();
    @Column
    private String ganador; //null -> partida sin acabar; nombreJugador que ha ganado o empate

    public Tablero(String nombre, String ip){

        this.nombreJugador1 = nombre;
        this.ipCliente1 = ip;

    }

}
