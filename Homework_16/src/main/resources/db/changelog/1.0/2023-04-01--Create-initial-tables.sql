--liquibase formatted sql

--changeset sergey:2023-04-01-001-author
create table author (
                        id bigserial,
                        name varchar(255),
                        constraint author_pkey primary key (id),
                        constraint author_name_unq unique (name)
);

--changeset sergey:2023-04-01-002-genre
create table genre (
                       id bigserial,
                       name varchar(255),
                       constraint genre_pkey primary key (id),
                       constraint genre_name_unq unique (name)
);

--changeset sergey:2023-04-01-003-book
create table book (
                      id bigserial,
                      title varchar(255),
                      author_id bigint,
                      genre_id bigint,
                      constraint book_pkey primary key (id),
                      constraint book_author_id_fkey foreign key (author_id) references author (id) on delete set null,
                      constraint book_genre_id_fkey foreign key (genre_id) references genre (id) on delete set null
);

--changeset sergey:2023-04-01-004-book-comment
create table book_comment (
                              id bigserial,
                              comment_text varchar(255),
                              book_id bigint,
                              constraint book_comment_pkey primary key (id),
                              constraint book_comment_book_id_fkey foreign key (book_id) references book (id) on delete cascade
);