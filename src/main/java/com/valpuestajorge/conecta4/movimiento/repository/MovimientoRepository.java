package com.valpuestajorge.conecta4.movimiento.repository;

import com.valpuestajorge.conecta4.movimiento.business.Movimiento;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MovimientoRepository extends ReactiveCrudRepository<Movimiento,Integer> {
    @Query("SELECT * FROM movimiento WHERE tablero = :tablero AND num_jugada = :numJugada")
    Movimiento findByTableroAndNumJugada(@Param("tablero") Integer tablero, @Param("numJugada") Integer numJugada);

    @Modifying
    @Query("DELETE FROM movimiento WHERE tablero = :tablero AND num_jugada = :numJugada")
    void deleteByTableroAndNumJugada(@Param("tablero") Integer tablero, @Param("numJugada") Integer numJugada);
}
