import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

// Creamos una instancia de SockJS y Stomp

const socket = new SockJS('http://localhost:4200/ws');
const stompClient = Stomp.over(socket);

// Nos conectamos al servidor WebSocket

stompClient.connect({}, frame => {
    console.log('Conexión WebSocket establecida:', frame);

    // Ahora vamos a subscribirnos a un topic específico

    stompClient.subscribe('/conecta4/game-updates', message => {
        const gameUpdate = JSON.parse(message.body);
        console.log('Actualización del juego recibida:', gameUpdate);

    });
});

// Esta función de ejemplo sirve para enviar un movimiento al servidor

function makeMove(gameId, playerNumber, column) {
    const move = {
        gameId: gameId,
        playerNumber: playerNumber,
        column: column
    };

    stompClient.send('/app/move', {}, JSON.stringify(move));
    console.log('Movimiento enviado:', move);
}