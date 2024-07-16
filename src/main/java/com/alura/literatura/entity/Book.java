package com.alura.literatura.entity;

import com.alura.literatura.model.BookData;
import com.alura.literatura.model.Language;
import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String bookTitle;
    @Enumerated(EnumType.STRING)
    private Language bookLanguage;
    private String bookCopyright;
    private Integer bookDownload;
    @ManyToOne
    private Author bookAuthor;

    public Book() {
    }

    public Book(BookData libro) {
        this.bookTitle = libro.titulo();
        this.bookLanguage = Language.fromString(libro.lenguajes().stream()
                .findFirst()
                .orElse(""));
        this.bookCopyright = libro.copyright();
        this.bookDownload = libro.descarga();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Language getBookLanguage() {
        return bookLanguage;
    }

    public void setBookLanguage(Language bookLanguage) {
        this.bookLanguage = bookLanguage;
    }

    public String getBookCopyright() {
        return bookCopyright;
    }

    public void setBookCopyright(String bookCopyright) {
        this.bookCopyright = bookCopyright;
    }

    public Integer getBookDownload() {
        return bookDownload;
    }

    public void setBookDownload(Integer bookDownload) {
        this.bookDownload = bookDownload;
    }

    public Author getBookAutor() {
        return bookAuthor;
    }

    public void setBookAuthor(Author bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    @Override
    public String toString() {
        return "id=" + id +
                "\nTitulo='" + bookTitle + '\'' +
                "\nAutor=" + bookAuthor.getAuthorName() +
                "\nLenguaje=" + bookLanguage +
                "\nCopyright='" + bookCopyright + '\'' +
                "\nDescarga=" + bookDownload +
                "\n-----------";
    }
}
