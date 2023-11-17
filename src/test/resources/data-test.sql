insert into CATEGORY (NAME)
values ('Офисные');
insert into CATEGORY (NAME)
values ('Системные');
insert into CATEGORY (NAME)
values ('Мультимедиа');
insert into CATEGORY (NAME)
values ('Конверторы');
insert into CATEGORY (NAME)
values ('Архиваторы');
insert into CATEGORY (NAME)
values ('Безопасность');
insert into CATEGORY (NAME)
values ('Интернет');
insert into CATEGORY (NAME)
values ('ERM и CRM');

-- insert into users(email, password, role, status)
-- values ('admin@admin.ru', 'admin', 'ADMIN', 'ACTIVE');

insert into seller(name, email, number, registration_time)
values ('seller1', 'seller1@email.ru', '1111111111', CURRENT_TIMESTAMP),
       ('seller2', 'seller2@email.ru', '2222222222', CURRENT_TIMESTAMP),
       ('seller3', 'seller3@email.ru', '3333333333', CURRENT_TIMESTAMP);

insert into product(name, description, version, production_time, category_id, license, vendor_id, seller_id, price,
                    quantity, status)
values ('product1', 'product1 description', '2.0.0.1', TIMESTAMP '2023-11-15 12:34:56', 1, 'DEMO', 1, 1, 1000, 5, 'PUBLISHED'),
       ('product2', 'product2 description', '2.0.0.1', TIMESTAMP '2023-10-15 12:34:56', 2, 'DEMO', 2, 2, 2000, 5, 'PUBLISHED'),
       ('product3', 'product3 description and details', '2.0.0.1', TIMESTAMP '2023-09-15 12:34:56', 2, 'DEMO', 3, 3, 500, 5, 'PUBLISHED'),
       ('product4', 'product4 description', '2.0.0.1', TIMESTAMP '2023-11-15 12:34:56', 1, 'DEMO', 1, 1, 1000, 5, 'SHIPPED');