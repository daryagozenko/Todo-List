1. перед запуском нужно создать .env в корне проекта

   - DB_PASSWORD=ваш_пароль
   - DB_ROOT_PASSWORD=еще_один_ваш_пароль


2. все настройки БД берутся из переменных окружения, которые я передаю через docker-compose, 
и кладутся в файл application.properties

3. Dockerfile для приложения (TodoList/Dockerfile)
   dockerfile
# ---- СТАДИЯ 1: СБОРКА ----
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY ./pom.xml .
COPY ./src ./src
RUN mvn clean package -Dmaven.test.skip=true

# ---- СТАДИЯ 2: ЗАПУСК  ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/target/TodoList-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

### Команды докер
- FROM maven:... AS build — базовый образ с Maven и JDK для сборки
- WORKDIR /build — создаем папку для сборки внутри контейнера
- COPY ./pom.xml . — копируем файл зависимостей
- COPY ./src ./src — копируем исходный код
- RUN mvn package — собираем JAR-файл

- FROM eclipse-temurin:21-jre — новый образ только с Java
- COPY --from=build — копируем JAR из первой стадии во вторую
- ENTRYPOINT ["java", "-jar", "app.jar"] — команда запуска приложения

Две стадии: так как в первой стадии maven и jdk компилируют код, а для запуска, вторая стадия, нам нужна только jre
это сильно экономит память

### Докер файл БД
FROM mysql:8.0
COPY init.sql /docker-entrypoint-initdb.d/
EXPOSE 3306
При первом запуске MySQL выполнит init.sql автоматически

4. docker-compose.yml
В docker-compose я создаю свою сеть:

networks:
    todo-network:
        driver: bridge

Теперь получается, что контейнеры в одной сети видят друг друга по именам сервисов
app может подключиться к db по адресу db:3306 


5. Volume

volumes:
    mysql_data:

volumes:
    mysql_data:/var/lib/mysql

volume сохраняет данные на хосте, и при перезапуске они не удаляются

6. ### Запуск контейнеров
- docker-compose up --build

собирается образ БД - собирается образ приложения - потом создается сеть - после создается volume
запускается сначала БД и как только она становится готова запускается само приложение

7. ### curl команды
1. Создать пользователя
   bash
   curl -X POST http://localhost:8080/users/create \
   -H "Content-Type: application/json" \
   -d '{
   "name": "Dasha",
   "surname": "Gozenko"
   }'
   Ответ:

json
{
"id": 1,
"name": "Dasha",
"surname": "Gozenko"
}
2. Получить пользователя по ID
   bash
   curl -X GET "http://localhost:8080/users/get?id=1"
   Ответ:

json
{
"id": 1,
"name": "Dasha",
"surname": "Gozenko"
}
3. Создать задачу для пользователя
   bash
   curl -X POST http://localhost:8080/tasks/create \
   -H "Content-Type: application/json" \
   -d '{
   "naming": "Решение чего-то",
   "priority": "Средний",
   "userId": 1
   }'
   Ответ:

json
{
"createdAt": "2026-03-15",
"naming": "Решение чего-то",
"priority": "Средний",
"userId": 1
}
4. Получить все задачи пользователя
   bash
   curl -X GET "http://localhost:8080/tasks/get/all?userId=1"