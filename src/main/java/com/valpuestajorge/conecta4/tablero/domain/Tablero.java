package com.valpuestajorge.conecta4.tablero.domain;

import com.valpuestajorge.conecta4.shared.util.ResultadoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;

@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tablero {
    @Id
    private Integer idTablero;
    @Column
    private Long user1;
    @Column
    private Long user2;
    @Column
    private int[][] posicion = new int[6][7]; //0 -> casilla vacía; 1 -> ficha amarilla; 2 -> ficha roja
    @Column
    private Integer turno;
    @Column
    private String ganador;

    public Tablero(Long appUser) {
        this.user1 = appUser;
        this.turno = 0;
    }

    public Mono<Tablero> anyadirFicha(int columna) {
        return Mono.fromSupplier(() -> {
            int fila = 0;
            while (fila < this.posicion.length) {
                if (posicion[fila][columna] == 0) {
                    if (this.getTurno() % 2 == 0) {
                        posicion[fila][columna] = 1;
                    } else {
                        posicion[fila][columna] = 2;
                    }
                    fila = this.posicion.length;
                } else {
                    fila++;
                }
            }
            return this;
        }).flatMap(this::checkConnect4).map(t -> {
            if (t.getGanador() == null) {
                t.setTurno(t.getTurno() + 1);
            }
            return t;
        });
    }

    // Método para verificar si hay un ganador
    public Mono<Tablero> checkConnect4(Tablero tablero) {
        return Mono.fromSupplier(() -> {
            int[][] posicion = tablero.getPosicion();
            int turno = tablero.getTurno() % 2 == 0 ? 1 : 2;
            int rows = posicion.length;
            int cols = posicion[0].length;

            // Verificar empate
            if (tablero.getTurno() == rows * cols) {
                tablero.setGanador(ResultadoEnum.DRAW.name());
                return tablero;
            }

            // Verificación de filas
            for (int[] row : posicion) {
                for (int col = 0; col < cols - 3; col++) {
                    boolean hasConnect4 = true;
                    for (int i = 0; i < 4; i++) {
                        if (row[col + i] != turno) {
                            hasConnect4 = false;
                            break;
                        }
                    }
                    if (hasConnect4) {
                        tablero.setGanador(turno == 1 ? ResultadoEnum.PLAYER_1.name() : ResultadoEnum.PLAYER_2.name());
                        return tablero;
                    }
                }
            }

            // Verificación de columnas
            for (int col = 0; col < cols; col++) {
                for (int row = 0; row < rows - 3; row++) {
                    boolean hasConnect4 = true;
                    for (int i = 0; i < 4; i++) {
                        if (posicion[row + i][col] != turno) {
                            hasConnect4 = false;
                            break;
                        }
                    }
                    if (hasConnect4) {
                        tablero.setGanador(turno == 1 ? ResultadoEnum.PLAYER_1.name() : ResultadoEnum.PLAYER_2.name());
                        return tablero;
                    }
                }
            }

            // Verificación de diagonales hacia abajo y hacia la derecha
            for (int row = 0; row < rows - 3; row++) {
                for (int col = 0; col < cols - 3; col++) {
                    boolean hasConnect4 = true;
                    for (int i = 0; i < 4; i++) {
                        if (posicion[row + i][col + i] != turno) {
                            hasConnect4 = false;
                            break;
                        }
                    }
                    if (hasConnect4) {
                        tablero.setGanador(turno == 1 ? ResultadoEnum.PLAYER_1.name() : ResultadoEnum.PLAYER_2.name());
                        return tablero;
                    }
                }
            }

            // Verificación de diagonales hacia arriba y hacia la derecha
            for (int row = 3; row < rows; row++) {
                for (int col = 0; col < cols - 3; col++) {
                    boolean hasConnect4 = true;
                    for (int i = 0; i < 4; i++) {
                        if (posicion[row - i][col + i] != turno) {
                            hasConnect4 = false;
                            break;
                        }
                    }
                    if (hasConnect4) {
                        tablero.setGanador(turno == 1 ? ResultadoEnum.PLAYER_1.name() : ResultadoEnum.PLAYER_2.name());
                        return tablero;
                    }
                }
            }
            return tablero;
        });
    }

}
