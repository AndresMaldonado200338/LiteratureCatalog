package com.alura.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookData(
        @JsonAlias("book_title") String titulo,
        @JsonAlias("book_authors") List<AuthorData> autores,
        @JsonAlias("book_languages") List<String> lenguajes,
        @JsonAlias("book_copyright") String copyright,
        @JsonAlias("book_download_count") Integer descarga) {
}
