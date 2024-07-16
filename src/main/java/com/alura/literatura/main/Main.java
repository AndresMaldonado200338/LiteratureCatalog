package com.alura.literatura.main;

import com.alura.literatura.entity.Author;
import com.alura.literatura.entity.Book;
import com.alura.literatura.model.BookData;
import com.alura.literatura.model.Data;
import com.alura.literatura.model.Language;
import com.alura.literatura.repository.AuthorRepository;
import com.alura.literatura.repository.BookRepository;
import com.alura.literatura.service.APIConsumption;
import com.alura.literatura.service.DataConvertion;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private final String URL_BASE = "https://gutendex.com/books/";
    private Scanner scanner = new Scanner(System.in);

    private APIConsumption consumoAPI = new APIConsumption();
    private DataConvertion conversor = new DataConvertion();

    private AuthorRepository authorRepository;
    private BookRepository bookRepository;

    public Main(AuthorRepository a, BookRepository b) {
        this.authorRepository = a;
        this.bookRepository = b;
    }

    public void showMenu() {
        int option = -1;
        String menu = """
                -------Bienvenido a Literalura-------
                ELige la opción deseada
                1) Buscar libro por titulo
                2) Listar libros registrados
                3) Listar autores registrados
                4) Listar autores vivos en un determinado año
                5) Listar libros por idioma
                6) Generar estadisticas
                7) Top 10 libros
                8) Buscar autor por nombre
                9) Listar autores con otras consultas
                10) Salir
                """;

        while (option != 0) {
            System.out.println(menu);
            try {
                option = Integer.parseInt(scanner.nextLine());
                switch (option) {
                    case 1:
                        searchBookByTitle();
                        break;
                    case 2:
                        listRegisteredBooks();
                        break;
                    case 3:
                        listRegisteredAuthors();
                        break;
                    case 4:
                        listLivingAuthors();
                        break;
                    case 5:
                        listBooksByLanguage();
                        break;
                    case 6:
                        generateStatistics();
                        break;
                    case 7:
                        top10Books();
                        break;
                    case 8:
                        searchAuthorByName();
                        break;
                    case 9:
                        ListAuthorsByYear();
                        break;
                    case 10:
                        System.out.println("Gracias por usar Literalura");
                        System.out.println("Cerrando la aplicacion...");
                        break;
                    default:
                        System.out.println("¡Opción no valida!");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Opcion no valida: " + e.getMessage());
            }
        }
    }

    public void searchBookByTitle() {
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        String nombre = scanner.nextLine();
        String json = consumoAPI.getData(URL_BASE + "?search=" + nombre.replace(" ", "%20"));
        var datos = conversor.getData(json, Data.class);
        Optional<BookData> libroAPI = datos.libros().stream()
                .filter(l -> l.titulo()
                        .toUpperCase()
                        .contains(nombre.toUpperCase()))
                .findFirst();
        if (libroAPI.isPresent()) {
            Author autor = new Author(libroAPI.get().autores().get(0));
            Book libro = new Book(libroAPI.get());
            try {
                Optional<Book> libroDB = bookRepository.findByBookTitleContainsIgnoreCase(libro.getBookTitle());
                if (libroDB.isPresent()) {
                    System.out.println("El libro ya está guardado en la BD.");
                    System.out.println(libroDB.get());
                } else {
                    Optional<Author> autorDB = authorRepository.findByAuthorNameContainsIgnoreCase(autor.getAuthorName());
                    if (autorDB.isPresent()) {
                        autor = autorDB.get();
                        autor.setAuthorBooks(libro);
                        System.out.println("El autor ya esta guardado en la BD!");
                    } else {
                        autor.setAuthorBooks(Collections.singletonList(libro));
                    }
                    authorRepository.save(autor);
                    System.out.println(libro);
                }
            } catch (Exception e) {
                System.out.println("Advertencia! " + e.getMessage());
            }
        } else
            System.out.println("Libro no encontrado!");
    }

    public void listRegisteredBooks() {
        List<Book> libros = bookRepository.findAll();
        libros.forEach(System.out::println);
    }

    public void listRegisteredAuthors() {
        List<Author> autores = authorRepository.findAll();
        autores.forEach(System.out::println);
    }

    public void listLivingAuthors() {
        System.out.println("Introduzca el año que desea buscar:");
        try {
            var fecha = Integer.valueOf(scanner.nextLine());
            List<Author> autores = authorRepository.findByAuthorBirthLessThanEqualAndAuthorDeathIsGreaterThanEqual(
                    fecha,
                    fecha
            );
            if (!autores.isEmpty()) {
                autores.forEach(a -> System.out.println(
                        "Autor: " + a.getAuthorName() + "\nFecha de nacimiento: " + a.getAuthorBirth() + "\nFecha de fallecimiento: " + a.getAuthorDeath()
                ));
            } else
                System.out.println("No hay autores vivos en ese año registrado en la BD!");
        } catch (NumberFormatException e) {
            System.out.println("introduce un año valido " + e.getMessage());
        }
    }

    public void listBooksByLanguage() {
        System.out.println("""
                Ingrese el idioma para buscar libros:
                es - español
                en - inglés
                fr - francés
                pt - portugués
                """);
        String idioma = scanner.nextLine();
        if (idioma.equalsIgnoreCase("es") ||
                idioma.equalsIgnoreCase("en") ||
                idioma.equalsIgnoreCase("fr") ||
                idioma.equalsIgnoreCase("pt")) {
            Language lenguaje = Language.fromString(idioma);
            List<Book> libros = bookRepository.findByBookLanguage(lenguaje);
            if (libros.isEmpty())
                System.out.println("No hay libros registrados");
            else
                libros.forEach(System.out::println);
        } else
            System.out.println("Introduce un idioma valido");
    }

    public void generateStatistics() {
        var json = consumoAPI.getData(URL_BASE);
        var datos = conversor.getData(json, Data.class);
        IntSummaryStatistics est = datos.libros().stream()
                .filter(l -> l.descarga() > 0)
                .collect(Collectors.summarizingInt(BookData::descarga));
        System.out.println("Cantidad media de descargas: " + est.getAverage());
        System.out.println("Cantidad maxima de descargas: " + est.getMax());
        System.out.println("Cantidad minima de descargas: " + est.getMin());
        System.out.println("Cantidad de registros evaluados para calcular las estadisticas: " +
                est.getCount());
    }

    public void top10Books() {
        List<Book> libros = bookRepository.top10Books();
        libros.forEach(System.out::println);
    }

    public void searchAuthorByName() {
        System.out.println("Ingrese el nombre del autor a buscar:");
        String nombre = scanner.nextLine();
        Optional<Author> autor = authorRepository.findByAuthorNameContainsIgnoreCase(nombre);
        if (autor.isPresent())
            System.out.println(autor.get());
        else
            System.out.println("El autor no existe en la BD");
    }

    public void ListAuthorsByYear() {
        System.out.println("""
                1 - Listar autor por año de nacimiento
                2 - Listar autor por año de fallecimiento
                Ingrese la opcion por la cual desea listar a los autores:
                """);
        try {
            var opcion = Integer.valueOf(scanner.nextLine());
            switch (opcion) {
                case 1:
                    listAuthorsByBirth();
                    break;
                case 2:
                    listAuthorsByDeath();
                    break;
                default:
                    System.out.println("Opcion invalida!");
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Opcion no valida: " + e.getMessage());
        }
    }

    public void listAuthorsByBirth() {
        System.out.println("Introduce el año de nacimiento a buscar:");
        try {
            var fecha = Integer.valueOf(scanner.nextLine());
            List<Author> autores = authorRepository.findByAuthorBirthEquals(fecha);
            if (autores.isEmpty())
                System.out.println("No existen autores con este año de nacimiento");
            else
                autores.forEach(System.out::println);
        } catch (NumberFormatException e) {
            System.out.println("Año no valido: " + e.getMessage());
        }
    }

    public void listAuthorsByDeath() {
        System.out.println("Introduce el año de fallecimiento a buscar:");
        try {
            var fallecimiento = Integer.valueOf(scanner.nextLine());
            List<Author> autores = authorRepository.findByAuthorDeathEquals(fallecimiento);
            if (autores.isEmpty())
                System.out.println("No existen autores con año de fallecimiento");
            else
                autores.forEach(System.out::println);
        } catch (NumberFormatException e) {
            System.out.println("Año no valido: " + e.getMessage());
        }
    }
}
