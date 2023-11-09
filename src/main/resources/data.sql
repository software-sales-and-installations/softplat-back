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
values ('admin@admin.ru', '$2a$12$18vPXf7skVRF667C8uJZ7.WlRxOCuMTbbJIXPVu8iF1tG2LZsrPSe', 'ADMIN', 'ACTIVE');

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

