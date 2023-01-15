--changeset sergey:2023-01-12-005-user-info-data
insert into user_info (username, password)
values ('admin', '$2a$12$StGhios9ftvmhCkY3Z9VO.6/oQi2EG/UbskKGI/9V9EIGpH6YzwpO'),
       ('user', '$$2a$12$StGhios9ftvmhCkY3Z9VO.6/oQi2EG/UbskKGI/9V9EIGpH6YzwpO'),
       ('anonymous', '$2a$12$StGhios9ftvmhCkY3Z9VO.6/oQi2EG/UbskKGI/9V9EIGpH6YzwpO')
;

--changeset sergey:2023-01-12-006-user-role-data
insert into user_role (user_id, authority)
values (1, 'ADMIN'),
       (2, 'USER'),
       (3, 'ANONYMOUS')
;

--changeset sergey:2023-01-12-007-acl-data
insert into acl_sid (principal, sid)
values (1, 'admin'), (1, 'user');

insert into acl_class (class)
values ('ru.otus.spring.dto.CommentDto');

insert into acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
values (1, 1, null, 1, 0),
       (1, 2, null, 1, 0),
       (1, 3, null, 1, 0),
       (1, 4, null, 1, 0),
       (1, 5, null, 1, 0),
       (1, 6, null, 1, 0),
       (1, 7, null, 1, 0),
       (1, 8, null, 1, 0)
;

insert into acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
values (1, 1, 1, 31, 1, 1, 1),
       (2, 1, 1, 31, 1, 1, 1),
       (3, 1, 1, 31, 1, 1, 1),
       (4, 1, 1, 31, 1, 1, 1),
       (5, 1, 1, 31, 1, 1, 1),
       (6, 1, 1, 31, 1, 1, 1),
       (7, 1, 1, 31, 1, 1, 1),
       (8, 1, 1, 31, 1, 1, 1),
       (1, 2, 2, 15, 1, 1, 1),
       (2, 2, 2, 1, 1, 1, 1),
       (3, 2, 2, 15, 1, 1, 1),
       (4, 2, 2, 1, 1, 1, 1),
       (5, 2, 2, 15, 1, 1, 1),
       (6, 2, 2, 0, 1, 1, 1),
       (7, 2, 2, 15, 1, 1, 1),
       (8, 2, 2, 0, 1, 1, 1)
;
