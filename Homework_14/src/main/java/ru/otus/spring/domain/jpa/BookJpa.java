package ru.otus.spring.domain.jpa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book")
public class BookJpa {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "title")
    private String title;

    @JoinColumn(name = "author_id")
    @ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
    private AuthorJpa author;

    @ManyToMany(targetEntity = GenreJpa.class, fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH })
    @JoinTable(name = "book_genre", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<GenreJpa> genres;

    @JoinColumn(name = "book_id", referencedColumnName = "id")
    @OneToMany(targetEntity = CommentJpa.class, cascade = { CascadeType.ALL })
    private List<CommentJpa> comments;
}
