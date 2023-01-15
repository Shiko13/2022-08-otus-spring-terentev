package ru.otus.spring.domain;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book {

    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ToString.Exclude
    @JoinColumn(name = "author_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    @ToString.Exclude
    @JoinColumn(name = "genre_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Genre genre;

    @ToString.Exclude
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Comment> comments;


    public Book setId(Long id) {
        this.id = id;
        return this;
    }

    public Book setAuthor(Author author) {
        this.author = author;
        return this;
    }

    public Book setGenre(Genre genre) {
        this.genre = genre;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(title, book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
