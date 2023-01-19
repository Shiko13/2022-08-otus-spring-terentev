--changeset sergey:2023-16-01-000-drop-all
drop table if exists book_genre;
drop table if exists book_comment;
drop table if exists book;
drop table if exists genre;
drop table if exists author;
drop sequence if exists author_sequence;
drop sequence if exists genre_sequence;
drop sequence if exists book_sequence;
drop sequence if exists book_comment_sequence;

--changeset sergey:2023-16-01-001-author
create table author (
    id varchar(255),
    name varchar(255),
    constraint author_pkey primary key (id)
);

--changeset sergey:2023-16-01-002-genre
create table genre (
    id varchar(255),
    title varchar(255),
    constraint genre_pkey primary key (id)
);

--changeset sergey:2023-16-01-003-book
create table book (
    id varchar(255),
    title varchar(255),
    author_id varchar(255),
    constraint book_pkey primary key (id),
    constraint book_author_id_fkey foreign key (author_id) references author (id) on update cascade on delete set null
);

--changeset sergey:2023-16-01-004-book-genre
create table book_genre (
    book_id varchar(255),
    genre_id varchar(255),
    constraint book_genre_pkey primary key (book_id, genre_id),
    constraint book_genre_book_id_fkey foreign key (book_id) references book (id) on update cascade on delete cascade,
    constraint book_genre_genre_id_fkey foreign key (genre_id) references genre (id) on update cascade on delete cascade
);

--changeset sergey:2023-16-01-005-book-comment
create table book_comment (
    id varchar(255),
    comment_text varchar(255),
    book_id varchar(255),
    constraint book_comment_pkey primary key (id),
    constraint book_comment_book_id_fkey foreign key (book_id) references book (id) on update cascade on delete cascade
);

--changeset sergey:2023-16-01-006-sequences
create sequence author_sequence as bigint increment by 1 start with 1;
create sequence genre_sequence as bigint increment by 1 start with 1;
create sequence book_sequence as bigint increment by 1 start with 1;
create sequence book_comment_sequence as bigint increment by 1 start with 1;