CREATE SCHEMA IF NOT EXISTS shopping_store;
SET search_path TO shopping_store;

CREATE TABLE IF NOT EXISTS products
(
    product_id       UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    product_name     VARCHAR(255)   NOT NULL,
    description      TEXT           NOT NULL,
    image_src        VARCHAR(512),
    quantity_state   VARCHAR(255)   NOT NULL,
    product_state    VARCHAR(255)   NOT NULL,
    product_category VARCHAR(255)   NOT NULL,
    price            DECIMAL(10, 2) NOT NULL CHECK (price >= 1),
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);