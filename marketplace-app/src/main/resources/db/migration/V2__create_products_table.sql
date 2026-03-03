CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          description TEXT,
                          price NUMERIC(12,2) NOT NULL CHECK (price >= 0),
                          owner_id BIGINT NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_product_owner
                              FOREIGN KEY (owner_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE
);

CREATE INDEX idx_products_owner ON products(owner_id);
CREATE INDEX idx_products_created_at ON products(created_at);