
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

insert into users(email, password, role, status)
values ('admin@admin.ru',
        '$2a$12$18vPXf7skVRF667C8uJZ7.WlRxOCuMTbbJIXPVu8iF1tG2LZsrPSe',
        'ADMIN',
        'ACTIVE');

insert into VENDOR (name, description, country)
values ('Adobe',
        'Приложения от компании Adobe позволяют работать с векторной и растровой графикой, а также с видео и музыкой. На данной странице собраны самые популярные и доступные программы Adobe с ключами активации последних версий, а также версии Repack.',
        'USA');
insert into VENDOR (name, description, country)
values ('Microsoft',
        'Продукты компании включают операционные системы, приложения для повышения производительности различных устройств, серверные приложения, бизнес-решения, средства разработки программного обеспечения и видеоигры.',
        'USA');
insert into VENDOR (name, description, country)
values ('Kaspersky',
        'Международная компания, специализирующаяся на разработке систем защиты от компьютерных вирусов, спама, хакерских атак и прочих киберугроз.',
        'RUSSIA');

insert into VENDOR (name, description, country)
values ('Adobe',
        'Приложения от компании Adobe позволяют работать с векторной и растровой графикой, а также с видео и музыкой. На данной странице собраны самые популярные и доступные программы Adobe с ключами активации последних версий, а также версии Repack.',
        'USA');

insert into requisite (account)
values ('1111222233334444');

insert into requisite (account)
values ('5555666677778888');

insert into seller (email, name, description, number, registration_time, requisites_id)
values ('seller1@admin.ru',
        'ИП Огурец',
        'Мы продаем ПО как для себя.',
        '9111111111',
        '2022-06-16 16:37:23',
        1);

insert into seller (email, name, description, number, registration_time, requisites_id)
values ('seller2@admin.ru',
        'ИП Томат',
        'Мы торгуем помидорами и ПО. Шучу, не помидорами.',
        '9222222222',
        '2020-06-16 16:37:23',
        2);

insert into product (name, description, version, production_time, category_id, license, vendor_id, seller_id, price, installation_price, quantity, installation, status, availability)
values ('Adobe',
        'Adobe для вашего ПК',
        '2021.02.547',
        '2021-01-01 10:00:00',
        1,
        'FREE',
        1,
        1,
        2000,
        150,
        10,
        true,
        'SHIPPED',
        true);

insert into product (name, description, version, production_time, category_id, license, vendor_id, seller_id, price, installation_price, quantity, installation, status, availability)
values ('NOD32',
        'NOD32 для вашего ПК',
        '2023.10.0',
        '2022-11-01 10:00:00',
        1,
        'LICENSE',
        3,
        2,
        3500,
        150,
        50,
        true,
        'SHIPPED',
        true);

insert into product (name, description, version, production_time, category_id, license, vendor_id, seller_id, price, installation_price, quantity, installation, status, availability)
values ('Adobe PhotoShop',
        'Adobe PhotoShop для тебя, Лягушонок',
        '2020.10.0',
        '2020-11-01 10:00:00',
        1,
        'LICENSE',
        1,
        2,
        799,
        20,
        100,
        true,
        'SHIPPED',
        true);

insert into product (name, description, version, production_time, category_id, license, vendor_id, seller_id, price, installation_price, quantity, installation, status, availability)
values ('Kaspersky',
        'Kaspersky для тебя, Лягушонок',
        '2020.10.0',
        '2020-11-01 10:00:00',
        2,
        'LICENSE',
        3,
        2,
        300,
        10,
        3,
        true,
        'SHIPPED',
        true);

insert into product (name, description, version, production_time, category_id, license, vendor_id, seller_id, price, installation_price, quantity, installation, status, availability)
values ('Adobe',
        'Adobe для вашего ПК',
        '2021.02.547',
        '2021-01-01 10:00:00',
        1,
        'FREE',
        1,
        1,
        2000,
        150,
        10,
        true,
        'SHIPPED',
        true);

insert into buyer (email, name, number, registration_time)
values ('email@email.ru',
        'Ваня',
        '9551423658',
        '2020-11-01 10:00:00');

insert into buyer (email, name, number, registration_time)
values ('email1@email.ru',
        'Аня',
        '9955923758',
        '2023-11-01 10:00:00');

insert into basket(buyer_id)
values (1);

insert into basket(buyer_id)
values (2);

insert into "order" (production_time, amount, buyer_id)
values ('2023-11-08 14:43:00',
        2000,
        1);

insert into "order" (production_time, amount, buyer_id)
values ('2023-11-08 10:43:00',
        2000,
        2);

insert into "order" (production_time, amount, buyer_id)
values ('2023-11-13 10:43:00',
        2000,
        1);

insert into product_basket(basket_id, product_id, quantity)
values (1,
        1,
        2000);

insert into product_order(order_id, product_id, quantity, amount, installation)
values (1,
        1,
        2,
        4000,
        true);

insert into product_order(order_id, product_id, quantity, amount, installation)
values (2,
        1,
        3,
        6000,
        true);

insert into product_order(order_id, product_id, quantity, amount, installation)
values (2,
        4,
        1,
        2000,
        true)