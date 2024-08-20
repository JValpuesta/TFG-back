CREATE TABLE IF NOT EXISTS "app_user" (
    "id" SERIAL PRIMARY KEY,
    "created_date" TIMESTAMP,
    "email" VARCHAR(255) UNIQUE,
    "username" VARCHAR(255),
    "login" VARCHAR(255),
    "password" VARCHAR(255),
    "ip" VARCHAR(255),
    "last_login" TIMESTAMP,
    "user_available_flag" BOOLEAN,
    "required_password_change_flag" BOOLEAN,
    "account_not_locked" BOOLEAN,
    "account_not_expired" BOOLEAN,
    "credential_not_expired" BOOLEAN,
    "temporary_password" VARCHAR(255),
    "configurations" VARCHAR(255),
    "motive_failed_login" VARCHAR(255),
    "user_role" VARCHAR(255),
    "nationality" VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS "tablero" (
    "id_tablero" SERIAL PRIMARY KEY,
    "jugador1_id" INTEGER NOT NULL, -- Referencia a AppUser para el jugador 1
    "jugador2_id" INTEGER,          -- Referencia a AppUser para el jugador 2
    "posicion" INTEGER[][],         -- Arreglo bidimensional para las posiciones del tablero
    "historial" INTEGER[],          -- Arreglo de enteros para almacenar el historial de movimientos
    "turno" INTEGER NOT NULL,       -- Turno actual de la partida
    "ganador_id" INTEGER,           -- Referencia a AppUser para el ganador de la partida
    FOREIGN KEY ("jugador1_id") REFERENCES "app_user" ("id") ON DELETE CASCADE,
    FOREIGN KEY ("jugador2_id") REFERENCES "app_user" ("id") ON DELETE SET NULL,
    FOREIGN KEY ("ganador_id") REFERENCES "app_user" ("id") ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS "movimiento" (
    "id_movimiento" SERIAL PRIMARY KEY,
    "tablero_id" INTEGER NOT NULL, -- Referencia a la tabla Tablero
    "num_jugada" INTEGER NOT NULL, -- Número de la jugada
    "jugador_id" INTEGER NOT NULL, -- Referencia a la tabla AppUser (jugador que hizo la jugada)
    "fecha_hora" TIMESTAMP NOT NULL, -- Fecha y hora del movimiento
    "columna" INTEGER NOT NULL, -- Columna en la que se hizo la jugada
    FOREIGN KEY ("tablero_id") REFERENCES "tablero" ("id_tablero") ON DELETE CASCADE,
    FOREIGN KEY ("jugador_id") REFERENCES "app_user" ("id") ON DELETE CASCADE
);