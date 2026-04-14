# Viking Demo

Минимальный демонстрационный Maven-проект для практического занятия по связке **Spring Web + REST API + Swagger/OpenAPI**.

## Что делает приложение

Приложение запускает:

1. **GUI на Swing** с одной кнопкой **Create random viking**.
2. **Таблицу**, где отображаются созданные викинги.
3. **REST API** с методом:
   - `GET /api/vikings` — вернуть список уже созданных викингов.
4. **Swagger UI** для просмотра и тестирования API:
   - `http://localhost:8080/swagger-ui.html`

В начале работы список пустой. Каждый клик по кнопке создаёт нового случайного викинга.

## Модель викинга

У викинга есть:
- `name`
- `age`
- `heightCm`
- `hairColor` (`enum`)
- `beardStyle` (`enum`)
- `equipment` (`List<EquipmentItem>`)

## Технологии

- Java 24
- Maven
- Spring Boot
- Spring Web MVC
- springdoc-openapi + Swagger UI
- DataFaker (современная замена Java Faker)
- Swing

## Сборка и запуск

```bash
mvn clean spring-boot:run
```

или

```bash
mvn clean package
java -jar target/viking-demo-1.0.0.jar
```
