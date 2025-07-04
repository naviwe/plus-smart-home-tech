CREATE TABLE IF NOT EXISTS orders (
  order_id VARCHAR DEFAULT gen_random_uuid() PRIMARY KEY,
  shopping_cart_id VARCHAR,
  payment_id VARCHAR,
  delivery_id VARCHAR,
  state VARCHAR,
  delivery_weight DOUBLE PRECISION,
  delivery_volume DOUBLE PRECISION,
  fragile BOOLEAN,
  total_price DOUBLE PRECISION,
  delivery_price DOUBLE PRECISION,
  product_price DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS order_products (
  product_id VARCHAR NOT NULL,
  quantity INTEGER,
  order_id VARCHAR REFERENCES orders(order_id) ON DELETE CASCADE
);