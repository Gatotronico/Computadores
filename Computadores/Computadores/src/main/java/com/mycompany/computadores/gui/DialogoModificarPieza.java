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
public class DialogoModificarPieza extends JDialog {
    private final ServicioDatos servicio;
    private final PanelInventario padre;
    private final Pieza piezaOriginal;

    private final JTextField txtNombre;
    private final JTextField txtStock;
    private final JTextField txtTipo; // Solo para mostrar, no se puede modificar

    public DialogoModificarPieza(Frame owner, ServicioDatos servicio, PanelInventario padre, Pieza pieza) {
        super(owner, "Modificar Pieza: " + pieza.getId(), true);
        this.servicio = servicio;
        this.padre = padre;
        this.piezaOriginal = pieza;

        // Inicializar campos con datos originales
        this.txtNombre = new JTextField(pieza.getNombre(), 20);
        this.txtStock = new JTextField(String.valueOf(pieza.getStock()), 20);
        this.txtTipo = new JTextField(pieza.getClass().getSimpleName(), 20);
        this.txtTipo.setEditable(false); // No se puede cambiar el tipo

        setSize(350, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // --- Panel Formulario ---
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 5, 5));
        
        panelFormulario.add(new JLabel("ID Pieza:"));
        panelFormulario.add(new JLabel(String.valueOf(pieza.getId()))); // ID no editable

        panelFormulario.add(new JLabel("Tipo de Pieza:"));
        panelFormulario.add(txtTipo);
        
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Stock Actual:"));
        panelFormulario.add(txtStock);

        add(panelFormulario, BorderLayout.CENTER);

        // --- Panel de Botones ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar Cambios");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardarModificaciones());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void guardarModificaciones() {
        // Validación básica
        String nuevoNombre = txtNombre.getText().trim();
        
        if (nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre de la pieza no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int nuevoStock = Integer.parseInt(txtStock.getText().trim());
            
            if (nuevoStock < 0) {
                 JOptionPane.showMessageDialog(this, "El stock no puede ser negativo.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                 return;
            }

            // Llamar al método de servicio que modificaste
            servicio.modificarPieza(piezaOriginal.getId(), nuevoNombre, nuevoStock); 
            
            JOptionPane.showMessageDialog(this, "Pieza modificada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            // Actualizar la tabla del panel padre y cerrar
            padre.refrescarTabla();
            dispose();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: El stock debe ser un número entero válido.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Maneja la PiezaNoEncontradaException, aunque no debería ocurrir aquí.
            JOptionPane.showMessageDialog(this, "Error de servicio: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
