create table if not exists authors
(
    author_id bigint generated by default as identity primary key,
    name      varchar(50),
    surname   varchar(50)
);

create table if not exists genres
(
    genre_id   bigint generated by default as identity primary key,
    genre_name varchar(50)
);

create table if not exists books
(
    book_id          bigint generated by default as identity primary key,
    title            varchar(255),
    publication_year INT,
    author_id        BIGINT REFERENCES authors (author_id) on delete cascade,
    genre_id         BIGINT REFERENCES genres (genre_id) on delete cascade
);

create table if not exists comments
(
    comment_id bigint generated by default as identity primary key,
    text       varchar,
    book_id    BIGINT REFERENCES books (book_id) on delete cascade
);

create table user_info
(
    id bigint generated by default as identity primary key,
    username varchar(255),
    password varchar(255),
    constraint user_info_pkey primary key (id),
    constraint user_info_username_unq unique (username)
);