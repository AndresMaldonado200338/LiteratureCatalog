package com.alura.literatura.model;

public enum Language {
    ES("es"),
    EN("en"),
    FR("fr"),
    PT("pt"),
    UNKNOWN("");

    private String language;

    Language(String language) {
        this.language = language;
    }

    public static Language fromString(String text) {
        for (Language idioma : Language.values()) {
            if (idioma.language.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Idioma no encontrado: " + text);
    }

    public String getLanguage() {
        return this.language;
    }
}
