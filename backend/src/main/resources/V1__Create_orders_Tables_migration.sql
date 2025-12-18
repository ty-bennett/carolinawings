-- V1__create_orders_tables.sql
-- Creates orders, order_items, order_item_options (based on current JPA entities)

-- orders table
CREATE TABLE IF NOT EXISTS orders (
                                      id uuid PRIMARY KEY,
                                      restaurant_id bigint NOT NULL,
                                      user_id uuid,
                                      subtotal numeric(10,2),
                                      total_price numeric(10,2),
                                      total_tax numeric(10,2),
                                      created_at timestamptz NOT NULL,
                                      updated_at timestamptz NOT NULL,
                                      pickup_time timestamptz,
                                      status varchar(255) NOT NULL,
                                      CONSTRAINT chk_orders_status CHECK (status IN ('PENDING','SENT_TO_KITCHEN','RECEIVED','PREPARING','READY','COMPLETED','CANCELLED'))
);

-- order_items table
CREATE TABLE IF NOT EXISTS order_items (
                                           id bigserial PRIMARY KEY,
                                           order_id uuid NOT NULL,
                                           menu_item_id bigint,
                                           menu_item_name varchar(255),
                                           menu_item_price numeric(10,2),
                                           quantity integer NOT NULL CHECK (quantity >= 1),
                                           total_price numeric(10,2)
);

-- order_item_options table
CREATE TABLE IF NOT EXISTS order_item_options (
                                                  id bigserial PRIMARY KEY,
                                                  order_item_id bigint NOT NULL,
                                                  group_name varchar(255),
                                                  option_name varchar(255),
                                                  extra_price numeric(10,2)
);

-- Foreign keys
ALTER TABLE IF EXISTS order_items
    ADD CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id) REFERENCES orders(id);

ALTER TABLE IF EXISTS order_item_options
    ADD CONSTRAINT fk_order_item_options_order_item
        FOREIGN KEY (order_item_id) REFERENCES order_items(id);

-- Optional FKs (you may already have restaurants/users tables in the schema)
-- These will fail if restaurants/users do not exist yet; keep them if you do have those tables.
ALTER TABLE IF EXISTS orders
    ADD CONSTRAINT fk_orders_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurants(id);

ALTER TABLE IF EXISTS orders
    ADD CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id) REFERENCES users(id);

-- Indexes to support common queries
CREATE INDEX IF NOT EXISTS idx_orders_restaurant_created_at ON orders (restaurant_id, created_at);
CREATE INDEX IF NOT EXISTS idx_order_items_order_id ON order_items (order_id);
CREATE INDEX IF NOT EXISTS idx_order_item_options_order_item_id ON order_item_options (order_item_id);

-- Optional: ensure not-null monetary fields if you prefer (uncomment to enable)
-- ALTER TABLE orders ALTER COLUMN subtotal SET NOT NULL;
-- ALTER TABLE orders ALTER COLUMN total_price SET NOT NULL;
-- ALTER TABLE orders ALTER COLUMN total_tax SET NOT NULL;
