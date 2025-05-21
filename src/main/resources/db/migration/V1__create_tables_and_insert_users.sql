-- Создание таблицы USER
CREATE TABLE "user"
(
    id            BIGINT PRIMARY KEY,
    name          VARCHAR(500),
    date_of_birth DATE password VARCHAR(500) NOT NULL CHECK (LENGTH(password) BETWEEN 8 AND 500)
);

-- Создание таблицы ACCOUNT
CREATE TABLE account
(
    id      BIGINT PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    balance DECIMAL       NOT NULL CHECK (balance >= 0),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user" (id)
);

-- Создание таблицы EMAIL_DATA
CREATE TABLE email_data
(
    id      BIGINT PRIMARY KEY,
    user_id BIGINT              NOT NULL,
    email   VARCHAR(200) UNIQUE NOT NULL CHECK (email ~ '^[^@]+@[^@]+\.[^@]+$'
) ,
    CONSTRAINT fk_user_email FOREIGN KEY (user_id) REFERENCES "user"(id)
);

-- Создание таблицы PHONE_DATA
CREATE TABLE phone_data
(
    id      BIGINT PRIMARY KEY,
    user_id BIGINT             NOT NULL,
    phone   VARCHAR(13) UNIQUE NOT NULL CHECK (phone ~ '^7\d{10}$'
) ,
    CONSTRAINT fk_user_phone FOREIGN KEY (user_id) REFERENCES "user"(id)
);