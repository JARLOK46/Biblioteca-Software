package Clases;

public class Libro {
    private String titulo;
    private String autor;
    private String isbn;
    private String genero;
    private int anioPublicacion;
    private int copiasDisponibles;

    public Libro(
            String titulo,
            String autor,
            String isbn,
            String genero,
            int anioPublicacion,
            int copiasDisponibles) {

        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }

        if (autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("El autor es obligatorio");
        }

        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("El ISBN es obligatorio");
        }

        if (genero == null || genero.isBlank()) {
            throw new IllegalArgumentException("El género es obligatorio");
        }

        if (anioPublicacion < 0 || anioPublicacion > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("El año de publicación no es válido");
        }

        if (copiasDisponibles < 0) {
            throw new IllegalArgumentException("Las copias disponibles no pueden ser negativas");
        }

        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.genero = genero;
        this.anioPublicacion = anioPublicacion;
        this.copiasDisponibles = copiasDisponibles;
    }

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
