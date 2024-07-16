package com.alura.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthorData(
        @JsonAlias("author_name") String nombre,
        @JsonAlias("author_birth_year") Integer nacimiento,
        @JsonAlias("author_death_year") Integer fallecimiento) {
}
