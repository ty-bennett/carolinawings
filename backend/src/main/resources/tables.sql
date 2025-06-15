-- Enable UUID extension (PostgreSQL)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE companies (
                           id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                           name VARCHAR(255) NOT NULL
);

CREATE TABLE restaurants (
                             id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                             name VARCHAR(255) NOT NULL
);

CREATE TABLE company_restaurants (
                                     company_id BIGINT NOT NULL,
                                     restaurant_id UUID NOT NULL,
                                     PRIMARY KEY (company_id, restaurant_id),
                                     FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE,
                                     FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

CREATE TABLE menus (
                       id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                       name VARCHAR(255) NOT NULL
);

CREATE TABLE restaurant_menus (
                                  restaurant_id UUID NOT NULL,
                                  menu_id BIGINT NOT NULL,
                                  PRIMARY KEY (restaurant_id, menu_id),
                                  FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE,
                                  FOREIGN KEY (menu_id) REFERENCES menus(id) ON DELETE CASCADE
);

CREATE TABLE menu_items (
                            id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                            name VARCHAR(255) NOT NULL,
                            price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE menu_menuitems (
                                menu_id BIGINT NOT NULL,
                                menu_item_id BIGINT NOT NULL,
                                PRIMARY KEY (menu_id, menu_item_id),
                                FOREIGN KEY (menu_id) REFERENCES menus(id) ON DELETE CASCADE,
                                FOREIGN KEY (menu_item_id) REFERENCES menu_items(id) ON DELETE CASCADE
);

CREATE TABLE orders (
                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                        order_date TIMESTAMP DEFAULT NOW()
);

CREATE TABLE order_menuitems (
                                 order_id UUID NOT NULL,
                                 menu_item_id BIGINT NOT NULL,
                                 quantity INT NOT NULL DEFAULT 1,
                                 PRIMARY KEY (order_id, menu_item_id),
                                 FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
                                 FOREIGN KEY (menu_item_id) REFERENCES menu_items(id) ON DELETE CASCADE
);

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       username VARCHAR(255) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE user_orders (
                             user_id UUID NOT NULL,
                             order_id UUID NOT NULL,
                             PRIMARY KEY (user_id, order_id),
                             FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                             FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE TABLE roles (
                       id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                       name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
                            user_id UUID NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE permissions (
                             id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                             name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE role_permissions (
                                  role_id BIGINT NOT NULL,
                                  permission_id BIGINT NOT NULL,
                                  PRIMARY KEY (role_id, permission_id),
                                  FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
                                  FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
);
