package Clases;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Ventana principal de la aplicación (interfaz Swing).
 *
 * Estructura de la pantalla (BorderLayout):
 * - Norte:   formulario de alta de libros.
 * - Centro:  tabla con el listado de libros.
 * - Sur:     botones de acción y filtro por autor.
 *
 * No contiene lógica de negocio: delega todo en Biblioteca.
 * Implementa ActionListener para atender los eventos de los botones.
 */
public class VentanaPrincipal extends JFrame implements ActionListener {
    // Encabezados de la tabla (el índice 2 es el ISBN, usado para eliminar).
    private static final String[] COLUMNAS = {"Título", "Autor", "ISBN", "Género", "Año", "Copias"};

    // Modelo de datos que la ventana consulta y actualiza.
    private final Biblioteca biblioteca;

    // Campos del formulario de alta.
    private final JTextField titulo = new JTextField();
    private final JTextField autor = new JTextField();
    private final JTextField isbn = new JTextField();
    private final JTextField genero = new JTextField();
    private final JTextField anio = new JTextField();
    private final JTextField copias = new JTextField();

    // Campo del filtro por autor (zona de controles).
    private final JTextField filtroAutor = new JTextField();

    // Tabla de solo lectura: isCellEditable devuelve false en todas las celdas.
    private final JTable tabla = new JTable(new DefaultTableModel(COLUMNAS, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    });

    // Botones de acción.
    private final JButton crear = new JButton("Crear libro");
    private final JButton filtrar = new JButton("Filtrar autor");
    private final JButton mostrarTodos = new JButton("Mostrar todos");
    private final JButton eliminar = new JButton("Eliminar seleccionado");

    // Constructor por defecto: crea su propia biblioteca.
    public VentanaPrincipal() { this(new Biblioteca()); }

    /**
     * Construye la ventana y la deja visible con los datos iniciales.
     * Usa inyección de dependencia: recibe la biblioteca que va a usar.
     */
    public VentanaPrincipal(Biblioteca biblioteca) {
        super("Catálogo de biblioteca");
        this.biblioteca = biblioteca;

        // Configuración básica de la ventana.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // Armar la interfaz: formulario arriba, tabla al centro, botones abajo.
        add(formulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(controles(), BorderLayout.SOUTH);

        // Tamaño y centrado en pantalla.
        pack();
        setSize(850, 500);
        setLocationRelativeTo(null);

        // Cargar el catálogo completo al iniciar.
        refrescar(biblioteca.obtenerTodos());
    }

    /**
     * Panel superior: 6 campos de entrada con sus etiquetas (2 filas x 6 columnas).
     */
    private JPanel formulario() {
        JPanel panel = new JPanel(new GridLayout(2, 6, 5, 3));
        String[] etiquetas = {"Título", "Autor", "ISBN", "Género", "Año", "Copias"};
        JTextField[] campos = {titulo, autor, isbn, genero, anio, copias};
        // Primera fila: etiquetas. Segunda fila: campos de texto.
        for (String etiqueta : etiquetas) panel.add(new JLabel(etiqueta));
        for (JTextField campo : campos) panel.add(campo);
        return panel;
    }

    /**
     * Panel inferior: filtro por autor y botones de acción.
     * Aquí se registran los listeners de todos los botones.
     */
    private JPanel controles() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtroAutor.setColumns(25);
        panel.add(new JLabel("Autor:")); panel.add(filtroAutor);
        panel.add(filtrar); panel.add(mostrarTodos); panel.add(crear); panel.add(eliminar);

        // "this" es el listener: actionPerformed se encarga de todos los botones.
        crear.addActionListener(this); filtrar.addActionListener(this);
        mostrarTodos.addActionListener(this); eliminar.addActionListener(this);
        return panel;
    }

    /**
     * Punto único de entrada de los eventos de los botones.
     * Identifica cuál se presionó y ejecuta la acción correspondiente.
     * Cualquier IllegalArgumentException se muestra como diálogo de error.
     */
    @Override public void actionPerformed(ActionEvent event) {
        try {
            if (event.getSource() == crear) crearLibro();
            else if (event.getSource() == filtrar) refrescar(biblioteca.filtrarPorAutor(filtroAutor.getText()));
            else if (event.getSource() == mostrarTodos) refrescar(biblioteca.obtenerTodos());
            else eliminarLibro(); // Único botón restante: eliminar.
        } catch (IllegalArgumentException error) {
            // Los errores de validación van al usuario, no revientan la app.
            JOptionPane.showMessageDialog(this, error.getMessage(), "Datos no válidos", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Lee el formulario, crea el libro, lo guarda y refresca la tabla.
     * Si la validación falla, lanza IllegalArgumentException (la captura actionPerformed).
     */
    private void crearLibro() {
        Libro libro = new Libro(titulo.getText(), autor.getText(), isbn.getText(), genero.getText(),
                entero(anio, "El año debe ser un número entero"), entero(copias, "Las copias deben ser un número entero"));
        biblioteca.agregarLibro(libro);
        limpiarFormulario();
        refrescar(biblioteca.obtenerTodos());
    }

    /**
     * Elimina el libro seleccionado en la tabla, previa confirmación del usuario.
     * La columna 2 de la tabla contiene el ISBN (clave de eliminación).
     */
    private void eliminarLibro() {
        int fila = tabla.getSelectedRow();
        // Si no hay ninguna fila seleccionada, no hay nada que eliminar.
        if (fila < 0) throw new IllegalArgumentException("Selecciona un libro para eliminar");

        String codigo = tabla.getValueAt(fila, 2).toString();

        // Pedir confirmación antes de borrar (evita eliminaciones accidentales).
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Eliminar el libro seleccionado?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            biblioteca.eliminarLibro(codigo);
            // Conserva el filtro activo después de borrar.
            refrescar(biblioteca.filtrarPorAutor(filtroAutor.getText()));
        }
    }

    /**
     * Convierte el texto de un campo a entero.
     * Si el texto no es un número, lanza IllegalArgumentException con el mensaje dado.
     */
    private int entero(JTextField campo, String mensaje) {
        try { return Integer.parseInt(campo.getText().trim()); }
        catch (NumberFormatException error) { throw new IllegalArgumentException(mensaje); }
    }

    /**
     * Redibuja la tabla con la lista de libros recibida.
     * Primero borra todas las filas y luego las vuelve a cargar.
     */
    private void refrescar(ArrayList<Libro> libros) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0); // Vaciar la tabla antes de recargar.
        for (Libro libro : libros) modelo.addRow(new Object[]{
                libro.getTitulo(), libro.getAutor(), libro.getIsbn(),
                libro.getGenero(), libro.getAnioPublicacion(), libro.getCopiasDisponibles()});
    }

    // Vacía todos los campos del formulario tras un alta exitosa.
    private void limpiarFormulario() { titulo.setText(""); autor.setText(""); isbn.setText(""); genero.setText(""); anio.setText(""); copias.setText(""); }
}
