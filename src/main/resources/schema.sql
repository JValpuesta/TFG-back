CREATE TABLE IF NOT EXISTS "app_user" (
    "id" SERIAL PRIMARY KEY,
    "created_date" TIMESTAMP,
    "username" VARCHAR(255),
    "ip" VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS "tablero" (
    "id_tablero" SERIAL PRIMARY KEY,
    "user1" BIGINT NOT NULL,
    "user2" BIGINT,
    "posicion" INTEGER[][] NOT NULL,
    "turno" INTEGER NOT NULL DEFAULT 0,
    "ganador" VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS "movimiento" (
    "id_movimiento" SERIAL PRIMARY KEY,
    "tablero" INTEGER NOT NULL,
    "num_jugada" INTEGER NOT NULL,
    "jugador" BIGINT NOT NULL,
    "fecha_hora" TIMESTAMP NOT NULL,
    "columna" INTEGER NOT NULL,
    FOREIGN KEY ("tablero") REFERENCES "tablero" ("id_tablero") ON DELETE CASCADE,
    FOREIGN KEY ("jugador") REFERENCES "app_user" ("id") ON DELETE CASCADE
);