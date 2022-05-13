CREATE TABLE book (
    isbn   VARCHAR(255) PRIMARY KEY,
    title  VARCHAR(255) NOT NULL,
    author VARCHAR(255) NULL
);

CREATE TABLE user (
    id       INT          PRIMARY KEY AUTO_INCREMENT,
    login    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NOT NULL
);

CREATE TABLE permission (
    id   INT          PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE user_favourite (
    user_id    INT          NOT NULL,
    book_isbn  VARCHAR(255) NOT NULL,

    FOREIGN KEY (user_id)   REFERENCES user(id),
    FOREIGN KEY (book_isbn) REFERENCES book(isbn)
);

CREATE TABLE user_permission (
    user_id       INT NOT NULL,
    permission_id INT NOT NULL,

    FOREIGN KEY (user_id)       REFERENCES user(id),
    FOREIGN KEY (permission_id) REFERENCES permission(id)
);

INSERT INTO book(isbn, title, author)
VALUES ('isbn-1', 'book-1', 'author-1'),
       ('isbn-2', 'book-2', 'author-2'),
       ('isbn-3', 'book-3', 'author-3');

INSERT INTO user(login, password, name)
VALUES ('admin-1', 'd916ee0ccdd89f26b376782e3d9b07c7', 'a-name-1'), -- apass1
       ('admin-2', '2f60e01d27d31301340cc72718cb916c', 'a-name-2'), -- apass2
       ('user-1', '57612457cc880df13b661c3480c8ee4b', 'u-name-1'), -- upass1
       ('user-2', '15ce82ef1f545a2d7f42188fbc5a81d9', 'u-name-2'); -- upass2

INSERT INTO permission(name)
VALUES ('CREATE_BOOK');

INSERT INTO user_favourite(user_id, book_isbn)
VALUES ((SELECT id FROM user WHERE login = 'user-1'), 'isbn-1'),
       ((SELECT id FROM user WHERE login = 'user-1'), 'isbn-2');

INSERT INTO user_permission(user_id, permission_id)
VALUES ((SELECT id FROM user WHERE login = 'admin-1'), (SELECT id FROM permission WHERE name = 'CREATE_BOOK')),
       ((SELECT id FROM user WHERE login = 'admin-2'), (SELECT id FROM permission WHERE name = 'CREATE_BOOK'));