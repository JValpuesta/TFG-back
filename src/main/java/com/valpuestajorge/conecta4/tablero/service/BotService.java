package com.valpuestajorge.conecta4.tablero.service;

import org.springframework.stereotype.Service;

@Service
public interface BotService {
   int mejorMovimiento(int[][] tablero);

   void imprimirTablero(int[][] tablero);
}
