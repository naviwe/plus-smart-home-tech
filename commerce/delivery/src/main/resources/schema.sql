CREATE TABLE IF NOT EXISTS delivery_address (
  address_id VARCHAR DEFAULT gen_random_uuid() PRIMARY KEY,
  country VARCHAR,
  city VARCHAR,
  street VARCHAR,
  house VARCHAR,
  flat VARCHAR
);

CREATE TABLE IF NOT EXISTS deliveries (
  delivery_id VARCHAR DEFAULT gen_random_uuid() PRIMARY KEY,
  from_address_id VARCHAR NOT NULL,
  to_address_id VARCHAR NOT NULL,
  order_id VARCHAR,
  delivery_state VARCHAR,
  CONSTRAINT fk_from_address
      FOREIGN KEY (from_address_id) REFERENCES delivery_address(address_id),
  CONSTRAINT fk_to_address
      FOREIGN KEY (to_address_id) REFERENCES delivery_address(address_id)
);