package com.valpuestajorge.conecta4;

public class Conecta4 {

    public static final int FILAS = 6;
    public static final int COLUMNAS = 7;
    public static final int VACIO = 0;
    public static final int JUGADOR = 1;
    public static final int IA = 2;
    public static final int MAX_PROFUNDIDAD = 7;

    public static void main(String[] args) {
        int[][] tablero = new int[FILAS][COLUMNAS];
        // Configurar el tablero manualmente con las posiciones deseadas
        tablero[5][3] = JUGADOR;  // El jugador coloca una ficha en la columna 3 (fila 5)
        tablero[4][3] = IA;       // La IA coloca una ficha en la columna 3 (fila 4)
        tablero[5][2] = JUGADOR;  // El jugador coloca una ficha en la columna 2 (fila 5)
        tablero[5][4] = IA;       // La IA coloca una ficha en la columna 4 (fila 5)
        tablero[5][1] = JUGADOR;
        tablero[5][0] = IA;
        tablero[4][2] = JUGADOR;
        tablero[3][2] = IA;
        tablero[3][3] = JUGADOR;
        tablero[2][3] = IA;
        tablero[4][1] = JUGADOR;
        tablero[2][2] = IA;
        tablero[4][4] = JUGADOR;
        tablero[1][2] = IA;
        tablero[0][2] = JUGADOR;
        tablero[1][3] = IA;
        tablero[0][3] = JUGADOR;
        tablero[4][0] = IA;
        tablero[3][4] = JUGADOR;

        // Mostrar el tablero antes del movimiento de la IA
        imprimirTablero(tablero);

        // La IA elige el mejor movimiento
        int mejorColumna = mejorMovimiento(tablero, MAX_PROFUNDIDAD);
        System.out.println("La mejor columna para la IA es: " + mejorColumna);

        // La IA hace su movimiento en la mejor columna
        hacerMovimiento(tablero, mejorColumna, IA);

        // Mostrar el tablero después del movimiento de la IA
        imprimirTablero(tablero);
    }

    public static int mejorMovimiento(int[][] tablero, int profundidad) {
        int mejorValor = Integer.MIN_VALUE;
        int mejorColumna = -1;
        for (int col = 0; col < COLUMNAS; col++) {
            if (esMovimientoValido(tablero, col)) {
                int[][] copiaTablero = copiarTablero(tablero);
                hacerMovimiento(copiaTablero, col, IA);
                int valorMovimiento = minimax(copiaTablero, profundidad - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                if (valorMovimiento > mejorValor) {
                    mejorValor = valorMovimiento;
                    mejorColumna = col;
                }
            }
        }
        return mejorColumna;
    }

    public static int minimax(int[][] tablero, int profundidad, int alpha, int beta, boolean maximizando) {
        if (profundidad == 0 || esVictoria(tablero, JUGADOR) || esVictoria(tablero, IA) || esTableroLleno(tablero)) {
            return evaluarTablero(tablero);
        }

        if (maximizando) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < COLUMNAS; col++) {
                if (esMovimientoValido(tablero, col)) {
                    int[][] copiaTablero = copiarTablero(tablero);
                    hacerMovimiento(copiaTablero, col, IA);
                    int eval = minimax(copiaTablero, profundidad - 1, alpha, beta, false);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) {
                        break; // Poda beta
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < COLUMNAS; col++) {
                if (esMovimientoValido(tablero, col)) {
                    int[][] copiaTablero = copiarTablero(tablero);
                    hacerMovimiento(copiaTablero, col, JUGADOR);
                    int eval = minimax(copiaTablero, profundidad - 1, alpha, beta, true);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) {
                        break; // Poda alfa
                    }
                }
            }
            return minEval;
        }
    }

    public static int evaluarTablero(int[][] tablero) {
        // Verificar si la IA o el jugador tiene una victoria inmediata
        if (esVictoria(tablero, IA)) {
            return 10000; // Puntuación muy alta para una victoria de la IA
        } else if (esVictoria(tablero, JUGADOR)) {
            return -10000; // Puntuación muy baja para evitar que el jugador gane
        }

        int puntuacion = 0;

        // Incentiva las casillas centrales
        for (int fila = 0; fila < FILAS; fila++) {
            if (tablero[fila][COLUMNAS / 2] == IA) {
                puntuacion += 3; // Mayor peso a las casillas centrales
            } else if (tablero[fila][COLUMNAS / 2] == JUGADOR) {
                puntuacion -= 3; // Penaliza si el jugador ocupa casillas centrales
            }
        }

        // Evaluar filas, columnas y diagonales
        puntuacion += evaluarLineas(tablero, IA) - evaluarLineas(tablero, JUGADOR);

        return puntuacion;
    }

    public static int evaluarLineas(int[][] tablero, int jugador) {
        int puntuacion = 0;

        // Evaluar filas
        for (int fila = 0; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS - 3; col++) {
                puntuacion += evaluarSegmento(tablero, fila, col, 0, 1, jugador);
            }
        }

        // Evaluar columnas
        for (int col = 0; col < COLUMNAS; col++) {
            for (int fila = 0; fila < FILAS - 3; fila++) {
                puntuacion += evaluarSegmento(tablero, fila, col, 1, 0, jugador);
            }
        }

        // Evaluar diagonales
        for (int fila = 0; fila < FILAS - 3; fila++) {
            for (int col = 0; col < COLUMNAS - 3; col++) {
                puntuacion += evaluarSegmento(tablero, fila, col, 1, 1, jugador);
            }
        }

        for (int fila = 3; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS - 3; col++) {
                puntuacion += evaluarSegmento(tablero, fila, col, -1, 1, jugador);
            }
        }

        return puntuacion;
    }

    public static int evaluarSegmento(int[][] tablero, int fila, int col, int dirFila, int dirCol, int jugador) {
        int contadorJugador = 0;
        int contadorVacios = 0;

        for (int i = 0; i < 4; i++) {
            int celda = tablero[fila + i * dirFila][col + i * dirCol];
            if (celda == jugador) {
                contadorJugador++;
            } else if (celda == VACIO) {
                contadorVacios++;
            }
        }

        if (contadorJugador == 4) {
            return 10000; // Victoria
        } else if (contadorJugador == 3 && contadorVacios == 1) {
            return 100; // Alta probabilidad de ganar
        } else if (contadorJugador == 2 && contadorVacios == 2) {
            return 50; // Buena situación
        }

        return 0;
    }

    public static boolean esMovimientoValido(int[][] tablero, int col) {
        return tablero[0][col] == VACIO;
    }

    public static void hacerMovimiento(int[][] tablero, int col, int jugador) {
        for (int fila = FILAS - 1; fila >= 0; fila--) {
            if (tablero[fila][col] == VACIO) {
                tablero[fila][col] = jugador;
                break;
            }
        }
    }

    public static boolean esVictoria(int[][] tablero, int jugador) {
        // Comprobaciones de victoria en filas, columnas y diagonales
        return (comprobarFilas(tablero, jugador) || comprobarColumnas(tablero, jugador) || comprobarDiagonales(tablero, jugador));
    }

    public static boolean comprobarFilas(int[][] tablero, int jugador) {
        for (int fila = 0; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS - 3; col++) {
                if (tablero[fila][col] == jugador && tablero[fila][col + 1] == jugador &&
                        tablero[fila][col + 2] == jugador && tablero[fila][col + 3] == jugador) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean comprobarColumnas(int[][] tablero, int jugador) {
        for (int col = 0; col < COLUMNAS; col++) {
            for (int fila = 0; fila < FILAS - 3; fila++) {
                if (tablero[fila][col] == jugador && tablero[fila + 1][col] == jugador &&
                        tablero[fila + 2][col] == jugador && tablero[fila + 3][col] == jugador) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean comprobarDiagonales(int[][] tablero, int jugador) {
        // Diagonales hacia abajo
        for (int fila = 0; fila < FILAS - 3; fila++) {
            for (int col = 0; col < COLUMNAS - 3; col++) {
                if (tablero[fila][col] == jugador && tablero[fila + 1][col + 1] == jugador &&
                        tablero[fila + 2][col + 2] == jugador && tablero[fila + 3][col + 3] == jugador) {
                    return true;
                }
            }
        }
        // Diagonales hacia arriba
        for (int fila = 3; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS - 3; col++) {
                if (tablero[fila][col] == jugador && tablero[fila - 1][col + 1] == jugador &&
                        tablero[fila - 2][col + 2] == jugador && tablero[fila - 3][col + 3] == jugador) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean esTableroLleno(int[][] tablero) {
        for (int col = 0; col < COLUMNAS; col++) {
            if (tablero[0][col] == VACIO) {
                return false;
            }
        }
        return true;
    }

    public static int[][] copiarTablero(int[][] tablero) {
        int[][] copia = new int[FILAS][COLUMNAS];
        for (int fila = 0; fila < FILAS; fila++) {
            System.arraycopy(tablero[fila], 0, copia[fila], 0, COLUMNAS);
        }
        return copia;
    }

    public static void imprimirTablero(int[][] tablero) {
        for (int fila = 0; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {
                System.out.print(tablero[fila][col] + " ");
            }
            System.out.println();
        }
    }
}

