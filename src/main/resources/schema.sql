CREATE TABLE IF NOT EXISTS "app_user" (
    "id" SERIAL PRIMARY KEY,
    "email" VARCHAR(255),
    "username" VARCHAR(255),
    "login" VARCHAR(255),
    "password" VARCHAR(255),
    "last_login" TIMESTAMP,
    "date_of_registration" TIMESTAMP,
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
