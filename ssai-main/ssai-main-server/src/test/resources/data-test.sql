insert into seller(name, email, number, registration_time)
values ('seller1', 'seller1@email.ru', '1111111111', CURRENT_TIMESTAMP),
       ('seller2', 'seller2@email.ru', '2222222222', CURRENT_TIMESTAMP),
       ('seller3', 'seller3@email.ru', '3333333333', CURRENT_TIMESTAMP);

insert into buyer(name, email, number, registration_time)
values ('buyer1', 'buyer1@email.ru', '1111111111', CURRENT_TIMESTAMP),
       ('buyer2', 'buyer2@email.ru', '2222222222', CURRENT_TIMESTAMP),
       ('buyer3', 'buyer3@email.ru', '3333333333', CURRENT_TIMESTAMP);

insert into product(name, description, version, production_time, category_id, vendor_id, seller_id, price,
                    quantity, status)
values ('product1', 'product1 description', '2.0.0.1', TIMESTAMP '2023-11-15 12:34:56', 1, 1, 1, 1000, 5, 'PUBLISHED'),
       ('product2', 'product2 description', '2.0.0.1', TIMESTAMP '2023-10-15 12:34:56', 2, 2, 2, 2000, 5, 'PUBLISHED'),
       ('product3', 'product3 description and details', '2.0.0.1', TIMESTAMP '2023-09-15 12:34:56', 2, 3, 3, 500, 5,
        'PUBLISHED'),
       ('product4', 'product4 description', '2.0.0.1', TIMESTAMP '2023-11-15 12:34:56', 1, 1, 1, 1000, 5, 'SHIPPED');

insert into product(name, description, version, production_time, category_id, vendor_id, seller_id, price,
                    installation_price, quantity, installation, status)
values ('product5', 'product4 description', '2.0.0.1', TIMESTAMP '2010-11-15 12:34:56', 4, 3, 1, 100000,
        100, 5, true, 'PUBLISHED');

insert into "order"(production_time, amount, buyer_id)
values (TIMESTAMP '2023-12-05 12:00:00', 1, 1),
       (TIMESTAMP '2023-12-05 11:00:00', 1, 2),
       (TIMESTAMP '2023-12-05 10:00:00', 1, 3);

insert into order_position(order_id, product_id, quantity, amount)
values (1, 1, 2, 1),
       (2, 1, 1, 1),
       (3, 1, 1, 1),
       (3, 2, 1, 1),
       (3, 3, 1, 1);

insert into favorite(buyer_id, product_id)
values (1, 1), (1, 2),
       (2, 2), (2, 3),
       (3, 1), (3, 5);