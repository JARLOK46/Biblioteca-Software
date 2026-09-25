package Clases;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Gestiona el catálogo de libros de la biblioteca.
 *
 * Usa dos estructuras para el mismo conjunto de datos:
 * - ArrayList  → listado y filtrado (recorrido secuencial).
 * - HashMap    → búsqueda por ISBN en tiempo O(1).
 *
 * Ambas deben mantenerse sincronizadas: toda modificación
 * se hace obligatoriamente a través de estos métodos.
 */
public class Biblioteca {
    // Listado completo de libros, en orden de inserción.
    private ArrayList<Libro> libros;
    // Índice de acceso rápido: clave = ISBN, valor = libro.
    private HashMap<String, Libro> librosPorIsbn;

    public Biblioteca() {
        libros = new ArrayList<>();
        librosPorIsbn = new HashMap<>();
    }

    /**
     * Agrega un libro validando que no sea nulo ni esté duplicado por ISBN.
     *
     * @throws IllegalArgumentException si el libro es nulo o el ISBN ya existe
     */
    public void agregarLibro(Libro libro) {
        // 1. Validar la entrada antes de modificar el estado.
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser nulo");
        }

        // 2. Normalizar el ISBN: eliminar espacios sobrantes para evitar duplicados.
        String isbn = libro.getIsbn().trim();

        // 3. Verificar que el ISBN no exista ya (búsqueda O(1) en el mapa).
        if (librosPorIsbn.containsKey(isbn)) {
            throw new IllegalArgumentException(
                    "Ya existe un libro registrado con ese ISBN");
        }

        // 4. Guardar en AMBAS estructuras para mantener la sincronización.
        libros.add(libro);
        librosPorIsbn.put(isbn, libro);
    }

    /**
     * Devuelve una copia de todos los libros.
     * La copia protege la lista interna: el caller no puede modificarla por accidente.
     */
    public ArrayList<Libro> obtenerTodos() {
        return new ArrayList<>(libros);
    }

    /**
     * Filtra los libros cuyo autor coincida, sin distinguir mayúsculas de minúsculas.
     * Si el criterio está vacío, devuelve todos los libros.
     */
    public ArrayList<Libro> filtrarPorAutor(String autor) {
        ArrayList<Libro> librosFiltrados = new ArrayList<>();

        // Sin criterio de búsqueda → se muestra el catálogo completo.
        if (autor == null || autor.isBlank()) {
            return obtenerTodos();
        }

        for (Libro libro : libros) {
            // equalsIgnoreCase: "garcía" encuentra "García".
            if (libro.getAutor().equalsIgnoreCase(autor.trim())) {
                librosFiltrados.add(libro);
            }
        }

        return librosFiltrados;
    }

    /**
     * Elimina el libro con el ISBN indicado, en ambas estructuras.
     *
     * @throws IllegalArgumentException si no existe un libro con ese ISBN
     */
    public void eliminarLibro(String isbn) {
        // Buscar primero en el mapa para confirmar que existe (O(1)).
        // El ternario evita un NullPointerException si isbn es null.
        Libro libro = librosPorIsbn.get(isbn == null ? null : isbn.trim());

        // Si no se encontró, no se modifica nada y se informa el error.
        if (libro == null) {
            throw new IllegalArgumentException(
                    "No existe un libro con ese ISBN");
        }

        // Borrar de ambas estructuras: lista y índice.
        libros.remove(libro);
        librosPorIsbn.remove(isbn.trim());
    }
}
