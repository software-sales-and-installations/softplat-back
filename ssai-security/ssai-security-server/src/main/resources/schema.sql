drop table if exists users, confirmation_token cascade;

CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(20)  NOT NULL,
    status   VARCHAR(20)  NOT NULL,
    id_main  BIGINT       NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS reset_token
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY,
    confirmation_token    VARCHAR(255)                 NOT NULL,
    user_id               BIGINT                       NOT NULL,
    created_on            TIMESTAMP WITHOUT TIME ZONE  NOT NULL,
    CONSTRAINT pk_token PRIMARY KEY (id),
    CONSTRAINT uq_token UNIQUE (confirmation_token),
    CONSTRAINT fk_user_token FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

insert into users(email, password, role, status, id_main)
values ('admin@admin.ru', '$2y$12$rm81Q25xrn1KmDHz2dsAq.lE5yLGXQxi/Zly/TBytl4S.Ge.zSup.', 'ADMIN', 'ACTIVE', 1);
