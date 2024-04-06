package com.bosonit.conecta4.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Historial {

    @Id
    private Integer idHistorial;
    private String ipJugador;
    private List<Integer> historialPartidas = new ArrayList<>();

    public Historial(String ipJugador) {
        this.ipJugador = ipJugador;
    }
}