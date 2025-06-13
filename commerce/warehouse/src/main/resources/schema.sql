CREATE SCHEMA IF NOT EXISTS warehouse;
SET search_path TO warehouse;

CREATE TABLE IF NOT EXISTS product (
     product_id UUID PRIMARY KEY,
     fragile BOOLEAN NOT NULL,
     weight DOUBLE PRECISION NOT NULL CHECK (weight > 0),
     width DOUBLE PRECISION NOT NULL CHECK (width > 0),
     height DOUBLE PRECISION NOT NULL CHECK (height > 0),
     depth DOUBLE PRECISION NOT NULL CHECK (depth > 0),
     quantity BIGINT NOT NULL CHECK (quantity >= 0)
);

CREATE INDEX IF NOT EXISTS idx_product_quantity ON product(quantity);