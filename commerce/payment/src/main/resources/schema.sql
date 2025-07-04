CREATE TABLE IF NOT EXISTS payments (
  payment_id VARCHAR DEFAULT gen_random_uuid() PRIMARY KEY,
  order_id VARCHAR,
  total_payment DOUBLE PRECISION,
  delivery_total DOUBLE PRECISION,
  fee_total DOUBLE PRECISION,
  status VARCHAR
);