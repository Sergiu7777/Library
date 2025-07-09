-- Insert users
INSERT INTO users (first_name, last_name, email)
VALUES ('Alice', 'Smith', 'alice.smith@example.com'),
       ('Bob', 'Johnson', 'bob.johnson@example.com'),
       ('Carol', 'Williams', 'carol.williams@example.com');

-- Insert books (some borrowed by users, some not)
INSERT INTO books (title, author, isbn, amount, user_id)
VALUES ('Clean Code', 'Robert C. Martin', '9780132350884', 3, 1),        -- Borrowed by Alice
       ('Effective Java', 'Joshua Bloch', '9780134685991', 2, 2),        -- Borrowed by Bob
       ('Design Patterns', 'Erich Gamma', '9780201633610', 5, NULL),     -- Not borrowed
       ('The Pragmatic Programmer', 'Andy Hunt', '9780201616224', 4, 1), -- Borrowed by Alice
       ('Refactoring', 'Martin Fowler', '9780201485677', 1, 3); -- Borrowed by Carol