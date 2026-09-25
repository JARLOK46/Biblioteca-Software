package Clases;

/**
 * Modelo de datos que representa un libro del catálogo.
 * Sus atributos son privados y solo se leen mediante getters (encapsulamiento).
 * La validación ocurre en el constructor: no existe un Libro inválido.
 */
public class Libro {
    private String titulo;
    private String autor;
    private String isbn;
    private String genero;
    private int anioPublicacion;
    private int copiasDisponibles;

    /**
     * Crea un libro validando todos sus datos antes de asignarlos.
     *
     * @throws IllegalArgumentException si un campo obligatorio está vacío
     *                                  o si año/copias tienen valores fuera de rango
     */
    public Libro(
            String titulo,
            String autor,
            String isbn,
            String genero,
            int anioPublicacion,
            int copiasDisponibles) {

        // Campos de texto obligatorios: no pueden ser null ni quedar en blanco.
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }

        if (autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("El autor es obligatorio");
        }

        // El ISBN es la clave de identificación única en la biblioteca.
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("El ISBN es obligatorio");
        }

        if (genero == null || genero.isBlank()) {
            throw new IllegalArgumentException("El género es obligatorio");
        }

        // El año debe estar entre 0 y el año actual.
        if (anioPublicacion < 0 || anioPublicacion > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("El año de publicación no es válido");
        }

        // No tiene sentido tener ejemplares negativos.
        if (copiasDisponibles < 0) {
            throw new IllegalArgumentException("Las copias disponibles no pueden ser negativas");
        }

        // Si todas las validaciones pasaron, se guardan los valores.
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.genero = genero;
        this.anioPublicacion = anioPublicacion;
        this.copiasDisponibles = copiasDisponibles;
    }

    // --- Getters: única forma de leer los atributos desde fuera de la clase ---

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getGenero() {
        return genero;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public int getCopiasDisponibles() {
        return copiasDisponibles;
    }
}
