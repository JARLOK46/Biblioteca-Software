package Clases;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VentanaPrincipal extends JFrame implements ActionListener {
    private static final String[] COLUMNAS = {"Título", "Autor", "ISBN", "Género", "Año", "Copias"};
    private final Biblioteca biblioteca;
    private final JTextField titulo = new JTextField();
    private final JTextField autor = new JTextField();
    private final JTextField isbn = new JTextField();
    private final JTextField genero = new JTextField();
    private final JTextField anio = new JTextField();
    private final JTextField copias = new JTextField();
    private final JTextField filtroAutor = new JTextField();
    private final JTable tabla = new JTable(new DefaultTableModel(COLUMNAS, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    });
    private final JButton crear = new JButton("Crear libro");
    private final JButton filtrar = new JButton("Filtrar autor");
    private final JButton mostrarTodos = new JButton("Mostrar todos");
    private final JButton eliminar = new JButton("Eliminar seleccionado");

    public VentanaPrincipal() { this(new Biblioteca()); }

    public VentanaPrincipal(Biblioteca biblioteca) {
        super("Catálogo de biblioteca");
        this.biblioteca = biblioteca;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        add(formulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(controles(), BorderLayout.SOUTH);
        pack();
        setSize(850, 500);
        setLocationRelativeTo(null);
        refrescar(biblioteca.obtenerTodos());
    }

    private JPanel formulario() {
        JPanel panel = new JPanel(new GridLayout(2, 6, 5, 3));
        String[] etiquetas = {"Título", "Autor", "ISBN", "Género", "Año", "Copias"};
        JTextField[] campos = {titulo, autor, isbn, genero, anio, copias};
        for (String etiqueta : etiquetas) panel.add(new JLabel(etiqueta));
        for (JTextField campo : campos) panel.add(campo);
        return panel;
    }

    private JPanel controles() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtroAutor.setColumns(25);
        panel.add(new JLabel("Autor:")); panel.add(filtroAutor);
        panel.add(filtrar); panel.add(mostrarTodos); panel.add(crear); panel.add(eliminar);
        crear.addActionListener(this); filtrar.addActionListener(this);
        mostrarTodos.addActionListener(this); eliminar.addActionListener(this);
        return panel;
    }

    @Override public void actionPerformed(ActionEvent event) {
        try {
            if (event.getSource() == crear) crearLibro();
            else if (event.getSource() == filtrar) refrescar(biblioteca.filtrarPorAutor(filtroAutor.getText()));
            else if (event.getSource() == mostrarTodos) refrescar(biblioteca.obtenerTodos());
            else eliminarLibro();
        } catch (IllegalArgumentException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "Datos no válidos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearLibro() {
        Libro libro = new Libro(titulo.getText(), autor.getText(), isbn.getText(), genero.getText(),
                entero(anio, "El año debe ser un número entero"), entero(copias, "Las copias deben ser un número entero"));
        biblioteca.agregarLibro(libro);
        limpiarFormulario();
        refrescar(biblioteca.obtenerTodos());
    }

    private void eliminarLibro() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) throw new IllegalArgumentException("Selecciona un libro para eliminar");
        String codigo = tabla.getValueAt(fila, 2).toString();
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Eliminar el libro seleccionado?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) { biblioteca.eliminarLibro(codigo); refrescar(biblioteca.filtrarPorAutor(filtroAutor.getText())); }
    }

    private int entero(JTextField campo, String mensaje) {
        try { return Integer.parseInt(campo.getText().trim()); }
        catch (NumberFormatException error) { throw new IllegalArgumentException(mensaje); }
    }

    private void refrescar(ArrayList<Libro> libros) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        for (Libro libro : libros) modelo.addRow(new Object[]{libro.getTitulo(), libro.getAutor(), libro.getIsbn(), libro.getGenero(), libro.getAnioPublicacion(), libro.getCopiasDisponibles()});
    }

    private void limpiarFormulario() { titulo.setText(""); autor.setText(""); isbn.setText(""); genero.setText(""); anio.setText(""); copias.setText(""); }
}
