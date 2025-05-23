# 🏦 BankingApp

BankingApp — это простое и расширяемое backend-приложение для управления пользователями, их телефонами и email-адресами.
Оно построено с использованием Spring Boot, JPA, Spring Security и JWT.

## 🚀 Возможности

- аутентификация пользователей (с использованием JWT)
- Управление профилем пользователя (имя, дата рождения, пароль)
- Добавление, редактирование и удаление телефонных номеров
- Добавление, редактирование и удаление email
- Поиск пользователей по имени, email, телефону и дате рождения
- Логирование действий для аудита и отладки

## 🛠️ Технологии

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Lombok
- JWT (JSON Web Token)
- Docker
- Swagger

## 📦 Установка и запуск

### Предварительные требования

- JDK 17+
- Maven
- PostgreSQL

## Примеры запросов
# Аутентификация
POST /api/auth/login

{
"email": "user@example.com",
"password": "password123"
}

# Добавление телефона
POST /api/phones
{
"phone": "+1234567890"
}

👨‍💻 Разработчик
Автор: Арзуман Кочоян

LinkedIn: https://www.linkedin.com/in/arzuman-kochoyan/