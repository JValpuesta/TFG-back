package com.valpuestajorge.conecta4.tablero.domain;

import com.valpuestajorge.conecta4.app_user.domain.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import javax.persistence.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.ArrayList;
import java.util.List;

@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tablero {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTablero;
    @Column(name = "jugador1", nullable = false)
    private AppUser user1;
    @Column(name = "jugador2")
    private AppUser user2;
    @Column
    private int[][] posicion = new int[6][7]; //0 -> casilla vacía; 1 -> ficha amarilla; 2 -> ficha roja
    @Column
    private List<Integer> historial = new ArrayList<>();
    @Column(name = "turno", nullable = false)
    private Integer turno;
    @Column(name = "ganador")
    private AppUser ganador;

    public Tablero(AppUser appUser) {
        this.user1 = appUser;
        this.turno = 0;
    }

    public void anyadirFicha(int columna) { //los casos de columnaNoValida y columnaLlena se controlan en el front
        int fila = 0;
        while (fila < this.posicion.length) {
            if (posicion[fila][columna] == 0) {
                if(this.getTurno()%2==0){
                    posicion[fila][columna] = 1;
                } else {
                    posicion[fila][columna] = 2;
                }
                fila = this.posicion.length;
            } else {
                fila++;
            }
        }
        if(this.checkConnect4()){
            if(this.turno%2==0){
                this.ganador = getUser1();
            } else {
                this.ganador = getUser2();
            }
        }else {
            this.turno = getTurno() + 1;
        }
    }

    public void addIntToHistorial(int idMovimiento){
        getHistorial().add(idMovimiento);
    }

    public boolean checkConnect4() {
        if(this.getTurno()==this.posicion.length*this.posicion[0].length){
            setGanador(null);
            return true;
        }
        int turno = (this.getTurno()%2==0) ? 1 : 2;
        int rows = this.posicion.length;
        int cols = this.posicion[0].length;

        // Verificación de filas
        for (int[] ints : this.posicion) {
            for (int col = 0; col < cols - 3; col++) {
                boolean hasConnect4 = true;
                for (int i = 0; i < 4; i++) {
                    if (ints[col + i] != turno) {
                        hasConnect4 = false;
                        break;
                    }
                }
                if (hasConnect4) {
                    return true;
                }
            }
        }

        // Verificación de columnas
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows - 3; row++) {
                boolean hasConnect4 = true;
                for (int i = 0; i < 4; i++) {
                    if (this.posicion[row + i][col] != turno) {
                        hasConnect4 = false;
                        break;
                    }
                }
                if (hasConnect4) {
                    return true;
                }
            }
        }

        // Verificación de diagonales hacia abajo y hacia la derecha
        for (int row = 0; row < rows - 3; row++) {
            for (int col = 0; col < cols - 3; col++) {
                boolean hasConnect4 = true;
                for (int i = 0; i < 4; i++) {
                    if (this.posicion[row + i][col + i] != turno) {
                        hasConnect4 = false;
                        break;
                    }
                }
                if (hasConnect4) {
                    return true;
                }
            }
        }

        // Verificación de diagonales hacia arriba y hacia la derecha
        for (int row = 3; row < rows; row++) {
            for (int col = 0; col < cols - 3; col++) {
                boolean hasConnect4 = true;
                for (int i = 0; i < 4; i++) {
                    if (this.posicion[row - i][col + i] != turno) {
                        hasConnect4 = false;
                        break;
                    }
                }
                if (hasConnect4) {
                    return true;
                }
            }
        }
        return false;
    }

}
