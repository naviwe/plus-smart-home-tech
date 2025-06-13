SET search_path TO warehouse;

CREATE TABLE IF NOT EXISTS product (
     product_id UUID PRIMARY KEY,
     fragile BOOLEAN NOT NULL,
     weight DOUBLE PRECISION NOT NULL CHECK (weight > 0),
     width DOUBLE PRECISION NOT NULL CHECK (width > 0),
     height DOUBLE PRECISION NOT NULL CHECK (height > 0),
     depth DOUBLE PRECISION NOT NULL CHECK (depth > 0),
     quantity BIGINT NOT NULL CHECK (quantity >= 0),
     created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

SET search_path TO shopping_store;

CREATE TABLE IF NOT EXISTS products (
    product_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    image_src VARCHAR(512),
    quantity_state VARCHAR(255) NOT NULL,
    product_state VARCHAR(255) NOT NULL,
    product_category VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 1),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

SET search_path TO shopping_cart;

CREATE TABLE IF NOT EXISTS shopping_cart (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS shopping_cart_item (
    id UUID PRIMARY KEY,
    cart_id UUID NOT NULL REFERENCES shopping_cart(id) ON DELETE CASCADE,
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL CHECK (quantity >= 0),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_cart_product UNIQUE (cart_id, product_id)
);