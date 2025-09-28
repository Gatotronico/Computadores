/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores.gui;
import com.mycompany.computadores.servicios.ServicioDatos;
import com.mycompany.computadores.OrdenDeTrabajo;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author gabri_oq3uzky
 */
public class PanelGestionOrdenes extends JPanel {
    private final ServicioDatos servicio;
    private final JTable tablaOrdenes;
    private final OrdenTableModel modeloTabla;

    public PanelGestionOrdenes(ServicioDatos servicio) {
        this.servicio = servicio;
        setLayout(new BorderLayout(10, 10)); // Layout principal para la tabla y los botones

        // --- 1. Inicializar Tabla y Modelo ---
        modeloTabla = new OrdenTableModel(servicio.getListaOrdenes());
        tablaOrdenes = new JTable(modeloTabla);
        
        // Añadir la tabla al centro, dentro de un ScrollPane (para barras de desplazamiento)
        add(new JScrollPane(tablaOrdenes), BorderLayout.CENTER);

        // --- 2. Crear Panel de Botones ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton btnAgregar = new JButton("➕ Agregar Nueva Orden");
        JButton btnModificar = new JButton("✍️ Modificar Orden");
        JButton btnEliminar = new JButton("🗑️ Eliminar Orden");
        JButton btnGestionPiezas = new JButton("🛠️ Gestionar Piezas (Asignar/Editar)"); 
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnGestionPiezas);
        
        add(panelBotones, BorderLayout.SOUTH);

        // --- 3. Implementar Listener Básico (Eliminar) ---
        btnAgregar.addActionListener(e -> mostrarDialogoAgregarOrden());
        btnEliminar.addActionListener(e -> eliminarOrdenSeleccionada());
        btnGestionPiezas.addActionListener(e -> gestionarPiezasDeOrden());
        // El resto de listeners (Agregar, Modificar, Gestionar Piezas) se implementan después
        // o en diálogos separados para mantener este panel limpio.
    }
    
    private void mostrarDialogoAgregarOrden() {
        // Obtiene la referencia a la ventana principal (JFrame) para centrar el diálogo
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        
        DialogoAgregarOrden dialogo = new DialogoAgregarOrden(owner, servicio, this);
        dialogo.setVisible(true);
    }
        private void gestionarPiezasDeOrden() {
        int filaSeleccionada = tablaOrdenes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una orden de la tabla para gestionar sus piezas.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Obtener la orden seleccionada
        OrdenDeTrabajo ordenSeleccionada = modeloTabla.getOrdenAt(filaSeleccionada);
        
        // 2. Obtener la referencia al JFrame padre
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        
        // 3. Crear y mostrar el nuevo diálogo
        DialogoGestionPiezas dialogo = new DialogoGestionPiezas(owner, servicio, ordenSeleccionada);
        dialogo.setVisible(true);
        
        // No es estrictamente necesario, pero asegura que el estado de la tabla principal se actualice
        modeloTabla.fireTableDataChanged();
    }
    
    
    public void refrescarTabla() {
        modeloTabla.fireTableDataChanged();
    }
    // --- Lógica para Eliminar Orden Seleccionada ---
    private void eliminarOrdenSeleccionada() {
        int filaSeleccionada = tablaOrdenes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una orden de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OrdenDeTrabajo ordenAEliminar = modeloTabla.getOrdenAt(filaSeleccionada);

        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar la Orden #" + ordenAEliminar.getNumeroOrden() + "? Esta acción es irreversible.", 
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                // Llama a la lógica central del servicio
                servicio.eliminarOrden(ordenAEliminar.getNumeroOrden());
                
                // NOTIFICAR al modelo que los datos cambiaron para que la tabla se redibuje
                modeloTabla.fireTableDataChanged(); 
                
                JOptionPane.showMessageDialog(this, "Orden #" + ordenAEliminar.getNumeroOrden() + " eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error de Servicio", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
