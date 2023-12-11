drop table if exists admin, order_position,
    "order", basket_position, product, seller, requisite,
    category , vendor, image , basket , buyer, favorite, comment, complaint cascade;


CREATE TABLE IF NOT EXISTS image
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    size BIGINT       NOT NULL,
    data BYTEA        NOT NULL,
    CONSTRAINT pk_image PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS buyer
(
    id                BIGINT GENERATED BY DEFAULT AS IDENTITY,
    email             VARCHAR(30) NOT NULL,
    name              VARCHAR(20) NOT NULL,
    number            VARCHAR(10) NOT NULL,
    registration_time TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_buyer PRIMARY KEY (id),
    CONSTRAINT uq_buyer_email UNIQUE (email),
    CONSTRAINT uq_buyer_number UNIQUE (number)
);

CREATE TABLE IF NOT EXISTS requisite
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY,
    inn        VARCHAR(12)  NOT NULL,
    legal_form VARCHAR(5)   NOT NULL,
    account    VARCHAR(20)  NOT NULL,
    bik        VARCHAR(9)   NOT NULL,
    kpp        VARCHAR(10)  NOT NULL,
    ogrn       VARCHAR(13),
    ogrnip     VARCHAR(15),
    address    VARCHAR(500) NOT NULL,
    CONSTRAINT pk_requisite PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS seller
(
    id                BIGINT GENERATED BY DEFAULT AS IDENTITY,
    email             VARCHAR(30)                 NOT NULL,
    name              VARCHAR(20)                 NOT NULL,
    number            VARCHAR(10)                 NOT NULL,
    registration_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    image_id          BIGINT DEFAULT NULL,
    requisites_id     BIGINT,
    CONSTRAINT pk_seller PRIMARY KEY (id),
    CONSTRAINT uq_seller_email UNIQUE (email),
    CONSTRAINT uq_seller_number UNIQUE (number),
    CONSTRAINT fk_seller_image FOREIGN KEY (image_id) REFERENCES image (id) ON DELETE SET NULL,
    CONSTRAINT fk_requisites FOREIGN KEY (requisites_id) REFERENCES requisite (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS admin
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY,
    email VARCHAR(30) NOT NULL,
    name  VARCHAR(20) NOT NULL,
    CONSTRAINT pk_admin PRIMARY KEY (id),
    CONSTRAINT uq_admin_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS category
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(254) NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uq_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS vendor
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(500) NOT NULL,
    image_id    BIGINT DEFAULT NULL,
    country     VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_vendor PRIMARY KEY (id),
    CONSTRAINT uq_vendor_name UNIQUE (name),
    CONSTRAINT fk_image FOREIGN KEY (image_id) REFERENCES image (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS product
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name               VARCHAR(255)            NOT NULL,
    description        VARCHAR(500)            NOT NULL,
    version            VARCHAR(30)             NOT NULL,
    production_time    TIMESTAMP WITHOUT TIME ZONE,
    image_id           BIGINT  DEFAULT NULL,
    category_id        BIGINT                  NOT NULL,
    has_demo           BOOLEAN DEFAULT 'FALSE' NOT NULL,
    vendor_id          BIGINT                  NOT NULL,
    seller_id          BIGINT                  NOT NULL,
    price              FLOAT                   NOT NULL,
    installation_price FLOAT,
    quantity           INT                     NOT NULL,
    installation       BOOLEAN DEFAULT 'FALSE' NOT NULL,
    status             VARCHAR(30)             NOT NULL,
    availability       BOOLEAN DEFAULT 'TRUE'  NOT NULL,
    average_rating     FLOAT   DEFAULT NULL,
    complaint_count    BIGINT  DEFAULT 0       NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (id),
    CONSTRAINT fk_product_image FOREIGN KEY (image_id) REFERENCES image (id) ON DELETE SET NULL,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category (id),
    CONSTRAINT fk_vendor FOREIGN KEY (vendor_id) REFERENCES vendor (id),
    CONSTRAINT fk_seller FOREIGN KEY (seller_id) REFERENCES seller (id)
);

CREATE TABLE IF NOT EXISTS basket
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY,
    buyer_id BIGINT NOT NULL,
    CONSTRAINT pk_basket PRIMARY KEY (id),
    CONSTRAINT fk_buyer FOREIGN KEY (buyer_id) REFERENCES buyer (id) on update cascade on delete cascade
);

CREATE TABLE IF NOT EXISTS "order"
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY,
    production_time TIMESTAMP WITHOUT TIME ZONE,
    amount          FLOAT,
    buyer_id        BIGINT NOT NULL,
    CONSTRAINT pk_order PRIMARY KEY (id),
    CONSTRAINT fk_order_buyer FOREIGN KEY (buyer_id) REFERENCES buyer (id) on update cascade on delete cascade
);

CREATE TABLE IF NOT EXISTS order_position
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY,
    order_id     BIGINT                  NOT NULL,
    product_id   BIGINT                  NOT NULL,
    quantity     INT                     NOT NULL,
    amount       FLOAT                   NOT NULL,
    installation boolean DEFAULT 'FALSE' NOT NULL,
    CONSTRAINT pk_order_position PRIMARY KEY (id),
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES "order" (id) on update cascade on delete cascade,
    CONSTRAINT fk_product_position FOREIGN KEY (product_id) REFERENCES product (id) on update cascade on delete cascade
);

CREATE TABLE IF NOT EXISTS basket_position
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY,
    basket_id    BIGINT                  NOT NULL,
    product_id   BIGINT                  NOT NULL,
    quantity     INT                     NOT NULL,
    installation boolean DEFAULT 'FALSE' NOT NULL,
    CONSTRAINT pk_product_position PRIMARY KEY (id),
    CONSTRAINT fk_basket FOREIGN KEY (basket_id) REFERENCES basket (id) on update cascade on delete cascade,
    CONSTRAINT fk_product_basket FOREIGN KEY (product_id) REFERENCES product (id) on update cascade on delete cascade
);

CREATE TABLE IF NOT EXISTS favorite
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    buyer_id   BIGINT NOT NULL REFERENCES buyer (id) on update cascade on delete cascade,
    product_id BIGINT NOT NULL REFERENCES product (id) on update cascade on delete cascade
);

CREATE TABLE IF NOT EXISTS comment
(
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY,
    author_id        BIGINT                      NOT NULL,
    product_id       BIGINT                      NOT NULL,
    text             VARCHAR(600),
    rating           FLOAT                       NOT NULL,
    publication_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT fk_comment_buyer FOREIGN KEY (author_id) REFERENCES buyer (id) on update cascade on delete cascade,
    CONSTRAINT fk_comment_product FOREIGN KEY (product_id) REFERENCES product (id) on update cascade on delete cascade,
    CONSTRAINT uc_comment_buyer_product UNIQUE (author_id, product_id)
);

CREATE TABLE IF NOT EXISTS complaint
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY,
    buyer_id   BIGINT       NOT NULL REFERENCES buyer (id) ON DELETE CASCADE,
    product_id BIGINT       NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    seller_id  BIGINT       NOT NULL REFERENCES seller (id) ON DELETE CASCADE,
    order_id   BIGINT       NOT NULL REFERENCES "order" (id),
    status     VARCHAR(30)  NOT NULL,
    comment    VARCHAR(255),
    reason     VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE
);