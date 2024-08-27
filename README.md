![Project Logo](assets/logo.png)

# Task Management System

## Описание

Простая система управления задачами (Task Management System) с использованием Java. Система обеспечивает создание, редактирование, удаление и просмотр задач. Каждая задача содержит заголовок, описание, статус ("в ожидании", "в процессе", "завершено") и приоритет ("высокий", "средний", "низкий"), а также автора задачи и исполнителя. Реализовано только API.

## Выполненые требования

1. Сервис поддерживает аутентификацию и авторизацию пользователей по email и паролю.
2. Доступ к API аутентифицирован с помощью JWT токена.
3. Пользователи могут управлять своими задачами: создавать новые, редактировать существующие, просматривать и удалять, менять статус и назначать исполнителей задачи.
4. Пользователи могут просматривать задачи других пользователей, а исполнители задачи могут менять статус своих задач.
5. К задачам можно оставлять комментарии.
6. API позволят получать задачи конкретного автора или исполнителя, а также все комментарии к ним. Обеспечена фильтрация и пагинация вывода.
7. Сервис корректно обрабатывает ошибки и возвращает понятные сообщения, а также валидирует входящие данные.
8. Сервис задокументирован. API описан с помощью Open API и Swagger. В сервисе настроен Swagger UI.
9. Написано несколько базовых тестов для проверки основных функций системы.
10. Для реализации системы использованы: язык Java 17, Spring, Spring Boot. В качестве БД использована PostgreSQL. Для реализации аутентификации и авторизации использован Spring Security и Keycloak.

## Установка и запуск

### Локальный запуск

1. Склонируйте репозиторий:
    ```bash
    git clone https://github.com/your-repo/task-management-system.git
    ```

2. Перейдите в директорию проекта:
    ```bash
    cd task-management-system
    ```

3. Создайте файл `.env` в корневой директории проекта и добавьте в него следующие переменные окружения:
    ```bash
    POSTGRES_USER=your_postgres_user
    POSTGRES_PASSWORD=your_postgres_password
    POSTGRES_DB=your_postgres_db
    KEYCLOAK_ADMIN=your_keycloak_admin
    KEYCLOAK_ADMIN_PASSWORD=your_keycloak_admin_password
    KC_DB=your_keycloak_db
    KEYCLOAK_CLIENT_SECRET=your_api_secret
    ```

4. Запустите Docker Compose:
    ```bash
    docker-compose up
    ```

5. Приложение будет доступно по адресу `http://localhost:8081`.

## Настройки Spring

### Конфигурация `application.yml`

Для работы приложения необходима конфигурация в файле `application.yml`. Пример конфигурации:

```yaml
# Spring application configuration
spring:
  security:
    oauth2:
      client:
        registration:
          keycloak:
            client-id: task-mgt-system
            # The secret key for the client, retrieved from the environment variable
            client-secret: "${API_SECRET}"
            # The OAuth2 grant type, set to authorization_code for authorization code flow
            authorization-grant-type: authorization_code
            # Scope of access, typically 'openid' for authentication
            scope: openid
            # Redirect URI after authentication, placeholders for baseUrl and registrationId
            #redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            redirect-uri: http://localhost:8081/login/oauth2/code/keycloak
        provider:
          keycloak:
            # The URI of the Keycloak server's issuer
            #issuer-uri: http://keycloak:8585/realms/taskmgt
            issuer-uri: http://localhost:8585/realms/taskmgt
      resourceserver:
        jwt:
          # URI where JWT tokens are validated
          #issuer-uri: http://keycloak:8585/realms/taskmgt
          issuer-uri: http://localhost:8585/realms/taskmgt

  application:
    name: task-mgt-system
    # Name of the application

  jpa:
    hibernate:
      ddl-auto: update
      # Automatically update the database schema based on the entity models
    properties:
        hibernate:
          dialect: org.hibernate.dialect.PostgreSQLDialect
          # Hibernate dialect for PostgreSQL
    open-in-view: true
    # Enables open session in view to avoid lazy loading exceptions

  datasource:
    #url: jdbc:postgresql://postgres-db:5432/task_management
    url: jdbc:postgresql://localhost:5432/task_management
    # JDBC URL for connecting to the PostgreSQL database
    username: postgres_user
    # Database username
    password: postgres_password
    # Database password
    hikari:
      schema: app_schema
      # Schema to use for the database connection
      connection-init-sql: create schema if not exists app_schema
      # SQL to run to ensure the schema exists
      connection-timeout: 10000
      # Connection timeout in milliseconds


server:
  port: 8081
  # Port on which the application will run

app:
   # Client ID for OAuth2 authentication
  client-id: task-mgt-system
   # URL for obtaining access tokens
   #resource-url: http://keycloak:8585/realms/taskmgt/protocol/openid-connect/token
  resource-url: http://localhost:8585/realms/taskmgt/protocol/openid-connect/token

  #jwk-set-uri: http://keycloak:8585/realms/taskmgt/protocol/openid-connect/certs
  jwk-set-uri: http://localhost:8585/realms/taskmgt/protocol/openid-connect/certs

  # OAuth2 grant type for password credentials
  grant-type: password

  # Secret key for the application, retrieved from the environment variable
  secret: "${API_SECRET}"

  cache:
    cacheType: inMemory
    # Type of cache being used (in-memory).
    cacheNames:
      - databaseEntities
      - databaseEntityById
    # List of cache names to be used.
    caches:
      databaseEntities:
        expiry: 15s
        # Expiry duration for the 'databaseEntities' cache.
      databaseEntityById:
        expiry: 20s
        # Expiry duration for the 'databaseEntityByName' cache.


# Springdoc configuration for API documentation
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
```

## Документация API

API документируется с помощью Swagger. После запуска приложения Swagger UI будет доступен по адресу `http://localhost:8081/swagger-ui.html`.

## Обработка ошибок

Приложение включает комплексную обработку ошибок и возвращает понятные сообщения об ошибках. Ответы об ошибках следуют структуре, определенной в DTO `ErrorResponse`.

## Сотрудничество

Вы можете вносить свой вклад в проект, отправляя запросы на внесение изменений или создавая проблемы. Убедитесь, что ваш код соответствует стандартам проекта и включает соответствующие тесты.

## Лицензия

Этот проект лицензирован под MIT License.
