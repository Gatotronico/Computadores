/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores.gui;
import com.mycompany.computadores.Pieza;
import com.mycompany.computadores.servicios.ServicioDatos;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author gabri_oq3uzky
 */
public class DialogoSeleccionarPieza extends JDialog{
    private final ServicioDatos servicio;
    private final int idOrden;
    private final JTable tablaInventario;
    private final InventarioTableModel modeloInventario;
    private final DialogoGestionPiezas padreGestion; // Para refrescar la tabla de piezas de la orden

    public DialogoSeleccionarPieza(Frame owner, ServicioDatos servicio, int idOrden, DialogoGestionPiezas padreGestion) {
        super(owner, "Seleccionar Pieza del Inventario (Orden #" + idOrden + ")", true);
        this.servicio = servicio;
        this.idOrden = idOrden;
        this.padreGestion = padreGestion;
        
        // El modelo usa la lista completa del inventario
        this.modeloInventario = new InventarioTableModel(servicio.getListaInventario());
        
        this.tablaInventario = new JTable(modeloInventario);

        setSize(600, 450);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        add(new JScrollPane(tablaInventario), BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAsignar = new JButton("Asignar Pieza Seleccionada");
        JButton btnCancelar = new JButton("Cancelar");

        btnAsignar.addActionListener(e -> asignarPieza());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnAsignar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void asignarPieza() {
        int filaSeleccionada = tablaInventario.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una pieza del inventario.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pieza piezaSeleccionada = modeloInventario.getPiezaAt(filaSeleccionada);
        
        String cantidadStr = JOptionPane.showInputDialog(this, 
            "Ingrese la cantidad a requerir (Stock actual: " + piezaSeleccionada.getStock() + "):", 
            "1");

        if (cantidadStr == null) return; // Cancelado
        
        try {
            int cantidad = Integer.parseInt(cantidadStr.trim());
            if (cantidad <= 0 || cantidad > piezaSeleccionada.getStock()) {
                JOptionPane.showMessageDialog(this, 
                    "Cantidad inválida. Debe ser un número positivo y no superar el stock disponible.", 
                    "Error de Stock", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Llama al método de lógica central
            servicio.agregarPiezaAOrden(idOrden, piezaSeleccionada.getId(), cantidad);
            
            JOptionPane.showMessageDialog(this, 
                "Pieza '" + piezaSeleccionada.getNombre() + "' asignada a la orden #" + idOrden + ".", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            // Refrescar la tabla del diálogo padre y cerrar este diálogo
            padreGestion.refrescarTablaPiezas();
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Ingrese un número válido para la cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de servicio al asignar pieza: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
