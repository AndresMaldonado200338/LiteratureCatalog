package com.alura.literatura.entity;

import com.alura.literatura.model.AuthorData;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String authorName;
    private Integer authorBirth;
    private Integer authorDeath;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> authorBooks;

    public Author() {}

    public Author(AuthorData autor){
        this.authorName = autor.nombre();
        this.authorBirth = autor.nacimiento();
        this.authorDeath = autor.fallecimiento();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getAuthorBirth() {
        return authorBirth;
    }

    public void setAuthorBirth(Integer authorBirth) {
        this.authorBirth = authorBirth;
    }

    public Integer getAuthorDeath() {
        return authorDeath;
    }

    public void setAuthorDeath(Integer authorDeath) {
        this.authorDeath = authorDeath;
    }

    public List<Book> getAuthorBooks() {
        return authorBooks;
    }

    public void setAuthorBooks(List<Book> authorBooks) {
        authorBooks.forEach(l -> l.setBookAuthor(this));
        this.authorBooks = authorBooks;
    }

    public void setAuthorBooks(Book book) {
        book.setBookAuthor(this);
        if (authorBooks == null)
            authorBooks = new ArrayList<>();
        authorBooks.add(book);
    }

    @Override
    public String toString() {
        return "id=" + id +
                "\nNombre='" + authorName + '\'' +
                "\nNacimiento=" + authorBirth +
                "\nFallecimiento=" + authorDeath +
                "\nLibros=" + authorBooks.stream()
                .map(Book::getBookTitle)
                .toList() +
                "\n-----------";
    }
}
