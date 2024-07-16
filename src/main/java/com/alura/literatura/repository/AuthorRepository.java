package com.alura.literatura.repository;

import com.alura.literatura.entity.Author;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByAuthorNameContainsIgnoreCase(String nombre);

    List<Author> findByAuthorBirthLessThanEqualAndAuthorDeathIsGreaterThanEqual(Integer nacimiento, Integer fallecimiento);

    List<Author> findByAuthorBirthEquals(Integer fecha);

    List<Author> findByAuthorDeathEquals(Integer fecha);
}
