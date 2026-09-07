package Clases;

import java.util.ArrayList;
import java.util.HashMap;

public class Biblioteca {
    private ArrayList<Libro> libros;
    private HashMap<String, Libro> librosPorIsbn;

    public Biblioteca() {
        libros = new ArrayList<>();
        librosPorIsbn = new HashMap<>();
    }

    public void agregarLibro(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser nulo");
        }

        String isbn = libro.getIsbn().trim();

        if (librosPorIsbn.containsKey(isbn)) {
            throw new IllegalArgumentException(
                    "Ya existe un libro registrado con ese ISBN");
        }

        libros.add(libro);
        librosPorIsbn.put(isbn, libro);
    }

    public ArrayList<Libro> obtenerTodos() {
        return new ArrayList<>(libros);
    }

    public ArrayList<Libro> filtrarPorAutor(String autor) {
        ArrayList<Libro> librosFiltrados = new ArrayList<>();

        if (autor == null || autor.isBlank()) {
            return obtenerTodos();
        }

        for (Libro libro : libros) {
            if (libro.getAutor().equalsIgnoreCase(autor.trim())) {
                librosFiltrados.add(libro);
            }
        }

        return librosFiltrados;
    }

    public void eliminarLibro(String isbn) {
        Libro libro = librosPorIsbn.get(isbn == null ? null : isbn.trim());

        if (libro == null) {
            throw new IllegalArgumentException(
                    "No existe un libro con ese ISBN");
        }

        libros.remove(libro);
        librosPorIsbn.remove(isbn.trim());
    }
}
