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

## 🚀 Как запустить

1. **Склонируйте репозиторий:**
   ```bash
   git clone https://github.com/Arzuman71/AccountBalanceAPI
   cd account-banking-app
1. **откройте среда разработки:**
   например откройте inteliji idea
2. **откройте docker-compose.yml:**
   запустите docker-compose.yml
3.  **запустите проект:**
    через main метод
### Предварительные требования

- JDK 17+
- Maven
- PostgreSQL

## Примеры запросов
# Аутентификация
POST http://localhost:8080/api/auth/login

{
"email": "user@example.com",
"password": "password123"
}

# переводить деньги
POST http://localhost:8080/api/accounts/transfer
{
"value": "100",
"transferTo": 2
}

# удалить электронную почту
http://localhost:8080/api/emailData/3

# изменить адрес электронной почты
PUT http://localhost:8080/api/emailData
{
"id":3,
"email":"test2@mail.com"
}

# добавить адрес электронной почты
POST http://localhost:8080/api/emailData
{
"email":"test@mail.com"
}

# Добавление телефона
POST  http://localhost:8080/api/phoneData
{
"phone": "+1234567890"
}

# удалить телефон
DELETE http://localhost:8080/api/phoneData/3

# сменить телефон
PUT http://localhost:8080/api/phoneData
{
"phone":"22222222",
"id":3
}

# изменить данные пользователя
PUT http://localhost:8080/api/users
{
"name": "test",
"dateOfBirth":"1998-06-05",
"password":"user"
}
# поиск пользователя
POST http://localhost:8080/api/users/search
{
"name": "",
"email": "",
"phone": "",
"dateOfBirth": "",
"page": 0,
"size": 0
}









👨‍💻 Разработчик
Автор: Арзуман Кочоян

LinkedIn: https://www.linkedin.com/in/arzuman-kochoyan/