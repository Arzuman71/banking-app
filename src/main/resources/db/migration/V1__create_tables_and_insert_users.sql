-- Создание таблицы USER
CREATE TABLE "user"
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(500),
    date_of_birth DATE,
    password      VARCHAR(500) NOT NULL CHECK (LENGTH(password) BETWEEN 8 AND 500)
);

-- Создание таблицы ACCOUNT
CREATE TABLE account
(
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT UNIQUE NOT NULL,
    balance         DECIMAL       NOT NULL CHECK (balance >= 0),
    added_deposit_percent  BIGINT DEFAULT 0,
    CONSTRAINT fk_user_account FOREIGN KEY (user_id) REFERENCES "user" (id)
);

-- Создание таблицы EMAIL_DATA
CREATE TABLE email_data
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT              NOT NULL,
    email   VARCHAR(200) UNIQUE NOT NULL CHECK (email ~ '^[^@]+@[^@]+\.[^@]+$'
        ),
    CONSTRAINT fk_user_email FOREIGN KEY (user_id) REFERENCES "user" (id)
);

-- Создание таблицы PHONE_DATA
CREATE TABLE phone_data
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT             NOT NULL,
    phone   VARCHAR(13) UNIQUE NOT NULL,
    CONSTRAINT fk_user_phone FOREIGN KEY (user_id) REFERENCES "user" (id)
);

-- password for 2 users is - user
INSERT INTO "user" (name, date_of_birth, password)
VALUES ('user1', '1990-05-15', '$2a$10$3F86urutOz8BTs4AuMW7Y.rimdkyAM5smdVIahynZ0YqGIVeV20yW'),
       ('user2', '1985-10-22', '$2a$10$3F86urutOz8BTs4AuMW7Y.rimdkyAM5smdVIahynZ0YqGIVeV20yW');

INSERT INTO account (user_id, balance)
VALUES (1, 10000.00),
       (2, 5000.00);


INSERT INTO email_data (user_id, email)
VALUES (1, 'ivan@example.com'),
       (2, 'ivan.work@example.com');

INSERT INTO phone_data (user_id, phone)
VALUES (1, '79101234567'),
       (2, '79201234567');