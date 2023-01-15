--changeset sergey:2023-01-12-001-authors
create table if not exists authors
(
    author_id bigint generated by default as identity primary key,
    name      varchar(50)
);

--changeset sergey:2023-01-12-002-genres
create table if not exists genres
(
    genre_id   bigint generated by default as identity primary key,
    genre_name varchar(50)
);

--changeset sergey:2023-01-12-003-books
create table if not exists books
(
    book_id          bigint generated by default as identity primary key,
    title            varchar(255),
    author_id        BIGINT REFERENCES authors (author_id) on delete cascade,
    genre_id         BIGINT REFERENCES genres (genre_id) on delete cascade
);

--changeset sergey:2023-01-12-004-comments
create table if not exists comments
(
    comment_id bigint generated by default as identity primary key,
    text       varchar,
    book_id    BIGINT REFERENCES books (book_id) on delete cascade
);