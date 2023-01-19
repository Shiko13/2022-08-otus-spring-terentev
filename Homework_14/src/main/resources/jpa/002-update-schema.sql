--changeset sergey:2023-16-01-007-update-object-id
update author set id = nextval('author_sequence')::varchar;
update genre set id = nextval('genre_sequence')::varchar;
update book set id = nextval('book_sequence')::varchar;
update book_comment set id = nextval('book_comment_sequence')::varchar;

--changeset sergey:2023-16-01-008-drop-foreign-keys
alter table book drop constraint book_author_id_fkey restrict;
alter table book_genre drop constraint book_genre_book_id_fkey restrict;
alter table book_genre drop constraint book_genre_genre_id_fkey restrict;
alter table book_comment drop constraint book_comment_book_id_fkey restrict;

--changeset sergey:2023-16-01-009-change-column-types
alter table author alter column id type bigint;
alter table genre alter column id type bigint;
alter table book alter column id type bigint;
alter table book alter column author_id type bigint;
alter table book_genre alter column book_id type bigint;
alter table book_genre alter column genre_id type bigint;
alter table book_comment alter column id type bigint;
alter table book_comment alter column book_id type bigint;

--changeset sergey:2023-16-01-010-alter-default-values-to-nextval
alter table author alter column id set default nextval('author_sequence');
alter table genre alter column id set default nextval('genre_sequence');
alter table book alter column id set default nextval('book_sequence');
alter table book_comment alter column id set default nextval('book_comment_sequence');

--changeset sergey:2023-16-01-011-add-foreign-keys
alter table book add constraint book_author_id_fkey foreign key (author_id) references author (id) on update cascade on delete set null;
alter table book_genre add constraint book_genre_book_id_fkey foreign key (book_id) references book (id) on update cascade on delete cascade;
alter table book_genre add constraint book_genre_genre_id_fkey foreign key (genre_id) references genre (id) on update cascade on delete cascade;
alter table book_comment add constraint book_comment_book_id_fkey foreign key (book_id) references book (id) on update cascade on delete cascade;