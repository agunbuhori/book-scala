# --- !Ups

CREATE TABLE IF NOT EXISTS book_orders (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  customer_name VARCHAR(100) NOT NULL,
  book_title VARCHAR(150) NOT NULL,
  quantity INT NOT NULL,
  order_date DATETIME NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL
);

# --- !Downs

DROP TABLE IF EXISTS book_orders;
