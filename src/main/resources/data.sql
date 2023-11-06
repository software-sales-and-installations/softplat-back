insert into CATEGORY (ID, NAME)
    values ( 1, 'Офисные' );
insert into CATEGORY (ID, NAME)
    values ( 2, 'Системные' );
insert into CATEGORY (ID, NAME)
    values ( 3, 'Мультимедиа' );
insert into CATEGORY (ID, NAME)
    values ( 4, 'Конверторы' );
insert into CATEGORY (ID, NAME)
    values ( 5, 'Архиваторы' );
insert into CATEGORY (ID, NAME)
    values ( 6, 'Безопасность' );
insert into CATEGORY (ID, NAME)
    values ( 7, 'Интернет' );
insert into CATEGORY (ID, NAME)
    values ( 8, 'ERM и CRM' );

insert into admin(email, name, password, role, status)
values ('admin@admin.ru', 'admin', 'admin', 'ADMIN', 'ACTIVE');

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