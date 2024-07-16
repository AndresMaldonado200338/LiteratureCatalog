package com.alura.literatura.repository;


import com.alura.literatura.entity.Book;
import com.alura.literatura.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByBookTitleContainsIgnoreCase(String titulo);

    List<Book> findByBookLanguage(Language lenguaje);

    @Query("SELECT l FROM Book l ORDER BY l.bookDownload DESC LIMIT 10")
    List<Book> top10Books();
}
