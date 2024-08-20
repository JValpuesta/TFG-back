package com.valpuestajorge.conecta4.movimiento.business;

import com.valpuestajorge.conecta4.app_user.domain.AppUser;
import com.valpuestajorge.conecta4.tablero.domain.Tablero;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

@Table(name = "movimiento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movimiento {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMovimiento;
    @Column(name = "tablero")
    private Tablero tablero;
    @Column(name = "num_jugada")
    private Integer numJugada;
    @Column(name = "jugador")
    private AppUser jugador;
    @Column(name = "fecha_hora")
    private Date fechaHora;
    @Column(name = "columna")
    private Integer columna;

    public Movimiento(Tablero tablero, Integer numJugada, AppUser jugador, Date fechaHora, Integer columna){

        this.tablero = tablero;
        this.numJugada = numJugada;
        this.jugador = jugador;
        this.fechaHora = fechaHora;
        this.columna = columna;
    }
}
