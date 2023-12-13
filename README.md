# softplat-back
Repository of the backend part of SoftPlat2048
## Описание 
Бекэнд сервиса-площадки по продаже лиценционного ПО.

RESTful API имеет многомодульную архитектуру: 
основной модуль с главной частью бизнес-логики, модуль сервиса статистики (хранит данные по продажам), модуль gateway (отвечает за работу с персональными данными пользователей).
Приложение имеет домен, по которому можно обращаться с запросами "http://softplat.ru". 

## ER diagram
Main:

Stats:

Gateway:

## Пример взаимодействия с БД
```
@Query("SELECT new ru.softplat.stats.server.dto.SellerReportEntry( " +
            "s.product.name, sum (s.quantity), sum(s.amount)) " +
            "FROM Stats s " +
            "WHERE s.dateBuy BETWEEN :start AND :end " +
            "GROUP BY s.product.seller.id, s.product.id, s.product.name ")
    List<SellerReportEntry> getAllStats(
            LocalDateTime start,
            LocalDateTime end);
```
----

## Примеры взаимодействия с приложением
Далее будут представлены варианты запросов для 
всех ролей, представленных в системе.

Admin:
- Удаление комментария к продукту админом: DELETE "http://api.softplat.ru/comment/{commentId}"

Seller:
- Создание карточки товара: POST "http://api.softplat.ru/product"

Buyer:
Добавление продукта в свою корзину: POST "http://api.softplat.ru/basket/{productId}"

Public:
- Регистрация нового пользователя: POST "http://api.softplat.ru/registration"
- Получение списка продуктов/поиск/фильтрация: GET "http://api.softplat.ru/product/search?minId=0&pageSize=10&sort=NEWEST"

## Стек
- Java SE 9
- Spring Boot
- Spring Data JPA
- Spring Security
- Spring Mail
- PostgreSQL 
- H2
- Hibernate
- Query DSL
- Mapstruct
- Lombok
- Gson
- Swagger

### Документация Swagger
After starting application:
- http://softplat.acceleratorpracticum.ru/swagger-ui/#/

### Commit rules
- ```feat```: for new feature additions.
- ```fix```: for fixing bugs or issues.
- ```docs```: for documentation changes or updates. 
- ```style```: for code style and formatting changes. 
- ```refactor```: for code refactoring without changing functionality. 
- ```test```: for adding or modifying tests. 
- ```chore```: for maintenance tasks and general housekeeping. 
- ```perf```: for performance improvements.

### Examples of good commits
- ```feat```: Add user registration functionality
- ```fix```: Resolve issue with login validation
- ```docs```: Update README with installation instructions
