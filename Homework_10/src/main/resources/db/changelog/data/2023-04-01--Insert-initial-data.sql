--changeset sergey:2023-04-01-001-author-data
insert into author (name)
values ('John Tolkien'), ('Lev Tolstoy'), ('Miguel Cervantes');

--changeset sergey:2023-04-01-002-genre-data
insert into genre (name)
values ('Novel'), ('Novella'), ('Fantasy'), ('Poem'), ('Short story');

--changeset sergey:2023-04-01-003-book-data
insert into book (title, author_id, genre_id)
values ('The Silmarillion', 1, 3),
       ('The Kreutzer Sonata', 2, 2),
       ('The Ingenious Gentleman Don Quixote of La Mancha', 3, 1),
       ('The Prisoner of the Caucasus', 2, 5),
       ('The Fall of Arthur', 1, 4)
;

--changeset sergey:2023-04-01-004-book-comment_data
insert into book_comment (book_id, comment_text)
values (1, 'Amazing book!'), (1, 'Is it a book about big number?'),
       (2, 'Is it book about music?'),
       (3, 'Sancho Panza is my crush ^_^'), (3, 'Dulcinea del Toboso is not princess'),
       (4, 'Is it book about Prometheus?'),
       (5, 'Poem is not finished'), (5, 'Poem published in 2013')
;