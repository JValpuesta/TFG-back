package com.bosonit.conecta4.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Historial {

    @Id
    private String ipJugador;
    private int[] historialPartidas;

}
