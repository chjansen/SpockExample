-- Create ordering schema and core tables for a simple product ordering domain
CREATE SCHEMA IF NOT EXISTS ordering;

-- Products table
CREATE TABLE IF NOT EXISTS ordering.products (
    id              BIGSERIAL PRIMARY KEY,
    sku             VARCHAR(64) UNIQUE NOT NULL,
    name            VARCHAR(255)       NOT NULL,
    description     TEXT,
    unit_price_cents BIGINT            NOT NULL CHECK (unit_price_cents >= 0),
    active          BOOLEAN            NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ        NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ        NOT NULL DEFAULT now()
);

-- Orders table
CREATE TABLE IF NOT EXISTS ordering.orders (
    id              BIGSERIAL PRIMARY KEY,
    customer_name   VARCHAR(255)       NOT NULL,
    status          VARCHAR(32)        NOT NULL DEFAULT 'NEW',
    created_at      TIMESTAMPTZ        NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ        NOT NULL DEFAULT now()
);

-- Order items table
CREATE TABLE IF NOT EXISTS ordering.order_items (
    id              BIGSERIAL PRIMARY KEY,
    order_id        BIGINT             NOT NULL REFERENCES ordering.orders(id) ON DELETE CASCADE,
    product_id      BIGINT             NOT NULL REFERENCES ordering.products(id),
    quantity        INTEGER            NOT NULL CHECK (quantity > 0),
    unit_price_cents BIGINT            NOT NULL CHECK (unit_price_cents >= 0)
);

CREATE INDEX IF NOT EXISTS idx_order_items_order ON ordering.order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_product ON ordering.order_items(product_id);

-- Trigger to keep updated_at fresh
CREATE OR REPLACE FUNCTION ordering.touch_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = now();
  RETURN NEW;
END; $$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_products_touch ON ordering.products;
CREATE TRIGGER trg_products_touch BEFORE UPDATE ON ordering.products
FOR EACH ROW EXECUTE FUNCTION ordering.touch_updated_at();

DROP TRIGGER IF EXISTS trg_orders_touch ON ordering.orders;
CREATE TRIGGER trg_orders_touch BEFORE UPDATE ON ordering.orders
FOR EACH ROW EXECUTE FUNCTION ordering.touch_updated_at();
