DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS users;

-- Create table: users
CREATE TABLE users
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    email      VARCHAR(255) UNIQUE
);

-- Create table: book
CREATE TABLE book
(
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    title   VARCHAR(255),
    author  VARCHAR(255),
    isbn    VARCHAR(255),
    amount  INT,
    user_id BIGINT,
    CONSTRAINT fk_book_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE SET NULL
);