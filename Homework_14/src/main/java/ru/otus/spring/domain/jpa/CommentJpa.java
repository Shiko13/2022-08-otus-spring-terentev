package ru.otus.spring.domain.jpa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book_comment")
public class CommentJpa {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "comment_text")
    private String text;
}
