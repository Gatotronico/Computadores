/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores.gui;
import com.mycompany.computadores.OrdenDeTrabajo;
import com.mycompany.computadores.servicios.ServicioDatos;
import com.mycompany.computadores.PiezaNecesariaComputador;
import javax.swing.*;
import java.awt.*;
import java.util.Optional;
/**
 *
 * @author gabri_oq3uzky
 */
public class DialogoGestionPiezas extends JDialog{
    private final ServicioDatos servicio;
    private final OrdenDeTrabajo orden;
    private final JTable tablaPiezas;
    private final PiezaNecesariaTableModel modeloTabla;

    public DialogoGestionPiezas(Frame owner, ServicioDatos servicio, OrdenDeTrabajo orden) {
        super(owner, "Gestión de Piezas para Orden #" + orden.getNumeroOrden(), true);
        this.servicio = servicio;
        this.orden = orden;
        
        // El modelo usa la lista DE PIEZAS de esta orden específica
        this.modeloTabla = new PiezaNecesariaTableModel(orden.getpiezasNecesariasComputador());
        this.tablaPiezas = new JTable(modeloTabla);

        setSize(700, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Título con detalles de la orden
        JLabel lblTitulo = new JLabel("Orden #" + orden.getNumeroOrden() + 
                                     " | Cliente: " + orden.getCliente().getNombre() + 
                                     " | Estado: " + orden.getEstado(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        add(lblTitulo, BorderLayout.NORTH);

        // Tabla de Piezas Requeridas
        add(new JScrollPane(tablaPiezas), BorderLayout.CENTER);

        // --- Panel de Botones SIA 2.4 ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton btnAgregar = new JButton("➕ Asignar Pieza del Inventario");
        JButton btnEditar = new JButton("✍️ Editar Cantidad Requerida"); // SIA2.4 (Edición)
        JButton btnEliminar = new JButton("🗑️ Eliminar Pieza de Orden"); // SIA2.4 (Eliminación)
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        
        add(panelBotones, BorderLayout.SOUTH);

        // --- Action Listeners ---
        btnAgregar.addActionListener(e -> mostrarDialogoSeleccionPieza());
        btnEditar.addActionListener(e -> editarCantidadPieza());
        btnEliminar.addActionListener(e -> eliminarPiezaDeOrden());
        // btnAgregar.addActionListener(e -> mostrarDialogoSeleccionPieza()); // Tarea futura
    }
    private void mostrarDialogoSeleccionPieza() {
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        DialogoSeleccionarPieza dialogo = new DialogoSeleccionarPieza(owner, servicio, orden.getNumeroOrden(), this);
        dialogo.setVisible(true);
    }
    /** Implementación SIA2.4: Edición de la colección anidada */
    private void editarCantidadPieza() {
        int filaSeleccionada = tablaPiezas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione la pieza cuya cantidad desea modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PiezaNecesariaComputador pnc = modeloTabla.getPiezaNecesariaAt(filaSeleccionada);
        
        String nuevaCantidadStr = JOptionPane.showInputDialog(this, 
            "Ingrese la nueva cantidad requerida para " + pnc.getPieza().getNombre() + 
            " (Stock actual: " + pnc.getPieza().getStock() + "):", 
            pnc.getCantidad());

        if (nuevaCantidadStr == null) return; // Cancelado
        
        try {
            int nuevaCantidad = Integer.parseInt(nuevaCantidadStr.trim());
            if (nuevaCantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Llama al método de lógica central (¡Asegúrate que existe en ServicioDatos!)
            servicio.editarCantidadPieza(orden.getNumeroOrden(), pnc.getPieza().getId(), nuevaCantidad);
            
            // Refrescar la tabla y notificar al usuario
            modeloTabla.fireTableDataChanged();
            JOptionPane.showMessageDialog(this, "Cantidad actualizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Ingrese un número válido para la cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de servicio: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Implementación SIA2.4: Eliminación de la colección anidada */
    private void eliminarPiezaDeOrden() {
        int filaSeleccionada = tablaPiezas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione la pieza a eliminar de la orden.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PiezaNecesariaComputador pnc = modeloTabla.getPiezaNecesariaAt(filaSeleccionada);

        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar la pieza '" + pnc.getPieza().getNombre() + "' de esta orden?", 
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                // Llama al método de lógica central (¡Debes crearlo en ServicioDatos!)
                servicio.eliminarPiezaDeOrden(orden.getNumeroOrden(), pnc.getPieza().getId());
                
                // Refrescar la tabla
                modeloTabla.fireTableDataChanged();
                JOptionPane.showMessageDialog(this, "Pieza eliminada de la orden.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error de servicio: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void refrescarTablaPiezas() {
        modeloTabla.fireTableDataChanged();
    }
}
