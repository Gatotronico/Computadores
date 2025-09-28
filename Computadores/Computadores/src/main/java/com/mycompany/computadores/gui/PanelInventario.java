/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores.gui;
import com.mycompany.computadores.servicios.ServicioDatos;
import com.mycompany.computadores.Pieza;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author gabri_oq3uzky
 */
public class PanelInventario extends JPanel {
    private final ServicioDatos servicio;
    private final JTable tablaInventario;
    private final InventarioTableModel modeloTabla;


    public PanelInventario(ServicioDatos servicio) {
        this.servicio = servicio;
        
        // 1. Inicializar Modelo y Tabla (Reutilizando el modelo)
        this.modeloTabla = new InventarioTableModel(servicio.getListaInventario()); // Utiliza el getter creado
        this.tablaInventario = new JTable(modeloTabla);
        
        setLayout(new BorderLayout(10, 10));

        // 2. Título
        JLabel lblTitulo = new JLabel("Inventario de Piezas y Componentes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.NORTH);

        // 3. Tabla
        add(new JScrollPane(tablaInventario), BorderLayout.CENTER);

        // 4. Panel de Botones (Gestión SIA 2.9)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton btnAgregar = new JButton("➕ Agregar Nueva Pieza");
        JButton btnModificar = new JButton("✍️ Modificar Pieza Seleccionada"); // SIA 2.9
        JButton btnEliminar = new JButton("🗑️ Eliminar Pieza"); // SIA 2.9
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        
        add(panelBotones, BorderLayout.SOUTH);

        // 5. Action Listeners
        btnAgregar.addActionListener(e -> mostrarDialogoAgregarPieza());
        btnModificar.addActionListener(e -> mostrarDialogoModificarPieza());
        btnEliminar.addActionListener(e -> eliminarPiezaSeleccionada());
    }
    
    // Método para refrescar la tabla del inventario
    public void refrescarTabla() {
        var listaActualizada = servicio.getListaInventario();
        modeloTabla.setPiezas(listaActualizada); 
        // Necesitas recrear el modelo si la lista de piezas cambió (ej. se agregó una pieza)
        modeloTabla.fireTableDataChanged(); 
    }
    
    // --- Métodos de Gestión ---
    


    private void mostrarDialogoModificarPieza() {
        int filaSeleccionada = tablaInventario.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione la pieza que desea modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Pieza piezaSeleccionada = modeloTabla.getPiezaAt(filaSeleccionada);
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);

        // Deberás crear DialogoModificarPieza.java
        // DialogoModificarPieza dialogo = new DialogoModificarPieza(owner, servicio, this, piezaSeleccionada);
        // dialogo.setVisible(true);
        DialogoModificarPieza dialogo = new DialogoModificarPieza(owner, servicio, this, piezaSeleccionada);
        dialogo.setVisible(true);
    }
 
    
    private void mostrarDialogoAgregarPieza() {
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        // AHORA SÍ USA EL DIÁLOGO CREADO:
        DialogoAgregarPieza dialogo = new DialogoAgregarPieza(owner, servicio, this);
        dialogo.setVisible(true);
    }
    
    private void eliminarPiezaSeleccionada() {
        int filaSeleccionada = tablaInventario.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione la pieza que desea eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Pieza pieza = modeloTabla.getPiezaAt(filaSeleccionada);

        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar la pieza '" + pieza.getNombre() + "' (ID: " + pieza.getId() + ")?", 
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                servicio.eliminarPieza(pieza.getId()); // Llamada a la lógica del servicio
                refrescarTabla();
                JOptionPane.showMessageDialog(this, "Pieza eliminada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                // Aquí podrías manejar una excepción si la pieza está en uso en alguna orden
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error de Servicio", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
