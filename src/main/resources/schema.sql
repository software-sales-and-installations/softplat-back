DROP TABLE IF EXISTS buyer, seller, admin, category, vendor, image, basket, "order", statistic, product, favorite, requisite CASCADE;

CREATE TABLE IF NOT EXISTS image
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    name
    VARCHAR
(
    254
) NOT NULL,
    type VARCHAR
(
    254
) NOT NULL,
    size FLOAT NOT NULL,
    data BYTEA NOT NULL,
    CONSTRAINT pk_image PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS buyer
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    email
    VARCHAR
(
    30
) NOT NULL,
    first_name VARCHAR
(
    20
) NOT NULL,
    last_name VARCHAR
(
    20
) NOT NULL,
    number VARCHAR
(
    10
) NOT NULL,
    registration_time TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_buyer PRIMARY KEY
(
    id
),
    CONSTRAINT uq_buyer_email UNIQUE
(
    email
),
    CONSTRAINT uq_buyer_number UNIQUE
(
    number
)
    );

CREATE TABLE IF NOT EXISTS requisite
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    account
    VARCHAR
(
    16
) NOT NULL,
    CONSTRAINT pk_requisite PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS seller
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    email
    VARCHAR
(
    30
) NOT NULL,
    name VARCHAR
(
    20
) NOT NULL,
    description VARCHAR
(
    500
),
    number VARCHAR
(
    10
) NOT NULL,
    registration_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    image_id BIGINT,
    requisites_id BIGINT,
    CONSTRAINT pk_seller PRIMARY KEY
(
    id
),
    CONSTRAINT uq_seller_email UNIQUE
(
    email
),
    CONSTRAINT uq_seller_number UNIQUE
(
    number
),
    CONSTRAINT fk_seller_image FOREIGN KEY
(
    image_id
) REFERENCES image
(
    id
),
    CONSTRAINT fk_requisites FOREIGN KEY
(
    requisites_id
) REFERENCES requisite
(
    id
)
    );

CREATE TABLE IF NOT EXISTS admin
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    email
    VARCHAR
(
    254
) NOT NULL,
    name VARCHAR
(
    254
) NOT NULL,
    CONSTRAINT pk_admin PRIMARY KEY
(
    id
),
    CONSTRAINT uq_admin_email UNIQUE
(
    email
)
    );

CREATE TABLE IF NOT EXISTS category
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    name
    VARCHAR
(
    254
) NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS vendor
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    name
    VARCHAR
(
    20
) NOT NULL,
    description VARCHAR
(
    500
) NOT NULL,
    image_id BIGINT NOT NULL,
    country VARCHAR
(
    20
) NOT NULL,
    CONSTRAINT pk_vendor PRIMARY KEY
(
    id
),
    CONSTRAINT fk_image FOREIGN KEY
(
    image_id
) REFERENCES image
(
    id
)
    );

CREATE TABLE IF NOT EXISTS product
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    name
    VARCHAR
(
    255
) NOT NULL,
    description VARCHAR
(
    1024
) NOT NULL,
    version VARCHAR
(
    30
) NOT NULL,
    production_time TIMESTAMP WITHOUT TIME ZONE,
    image_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    license_id VARCHAR NOT NULL,
    vendor_id BIGINT NOT NULL,
    seller_id BIGINT NOT NULL,
    price FLOAT NOT NULL,
    quantity INT NOT NULL,
    installation BOOLEAN DEFAULT 'FALSE' NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY
(
    id
),
    CONSTRAINT fk_product_image FOREIGN KEY
(
    image_id
) REFERENCES image
(
    id
),
    CONSTRAINT fk_category FOREIGN KEY
(
    category_id
) REFERENCES category
(
    id
),
    CONSTRAINT fk_vendor FOREIGN KEY
(
    vendor_id
) REFERENCES vendor
(
    id
),
    CONSTRAINT fk_seller FOREIGN KEY
(
    seller_id
) REFERENCES seller
(
    id
)
    );

CREATE TABLE IF NOT EXISTS basket
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    buyer_id
    BIGINT
    NOT
    NULL,
    CONSTRAINT
    pk_basket
    PRIMARY
    KEY
(
    id
),
    CONSTRAINT fk_buyer FOREIGN KEY
(
    buyer_id
) REFERENCES buyer
(
    id
) on delete cascade
    );

CREATE TABLE IF NOT EXISTS "order"
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    production_time
    TIMESTAMP
    WITHOUT
    TIME
    ZONE,
    amount
    FLOAT,
    buyer_id
    BIGINT
    NOT
    NULL,
    CONSTRAINT
    pk_order
    PRIMARY
    KEY
(
    id
),
    CONSTRAINT fk_order_buyer FOREIGN KEY
(
    buyer_id
) REFERENCES buyer
(
    id
)
    );

CREATE TABLE IF NOT EXISTS statistic
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    buyer_id
    BIGINT
    NOT
    NULL,
    product_id
    BIGINT
    NOT
    NULL,
    date_buy
    TIMESTAMP
    WITHOUT
    TIME
    ZONE,
    quantity
    INT
    NOT
    NULL,
    amount
    FLOAT
    NOT
    NULL,
    CONSTRAINT
    pk_statistic
    PRIMARY
    KEY
(
    id
),
    CONSTRAINT fk_statistic_buyer FOREIGN KEY
(
    buyer_id
) REFERENCES buyer
(
    id
),
    CONSTRAINT fk_statistic_product FOREIGN KEY
(
    product_id
) REFERENCES product
(
    id
)
    );

CREATE TABLE IF NOT EXISTS product_order
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    order_id
    BIGINT
    NOT
    NULL,
    product_id
    BIGINT
    NOT
    NULL,
    quantity
    INT
    NOT
    NULL,
    amount
    FLOAT
    NOT
    NULL,
    CONSTRAINT
    pk_product_order
    PRIMARY
    KEY
(
    id
),
    CONSTRAINT fk_order FOREIGN KEY
(
    order_id
) REFERENCES "order"
(
    id
) on update cascade
  on delete cascade,
    CONSTRAINT fk_product_order FOREIGN KEY
(
    product_id
) REFERENCES product
(
    id
)
  on update cascade
  on delete cascade
    );

CREATE TABLE IF NOT EXISTS product_basket
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    basket_id
    BIGINT
    NOT
    NULL,
    product_id
    BIGINT
    NOT
    NULL,
    quantity
    INT
    NOT
    NULL,
    CONSTRAINT
    pk_product_basket
    PRIMARY
    KEY
(
    id
),
    CONSTRAINT fk_basket FOREIGN KEY
(
    basket_id
) REFERENCES basket
(
    id
) on update cascade
  on delete cascade,
    CONSTRAINT fk_product_basket FOREIGN KEY
(
    product_id
) REFERENCES product
(
    id
)
  on update cascade
  on delete cascade
    );

CREATE TABLE IF NOT EXISTS favorite
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    buyer_id
    BIGINT
    NOT
    NULL,
    product_id
    BIGINT
    NOT
    NULL,
    CONSTRAINT
    pk_favorite
    PRIMARY
    KEY
(
    id
),
    CONSTRAINT fk_buyer_fav FOREIGN KEY
(
    buyer_id
) REFERENCES buyer
(
    id
) on delete cascade,
    CONSTRAINT fk_product_fav FOREIGN KEY
(
    product_id
) REFERENCES product
(
    id
)
  on delete cascade
    );