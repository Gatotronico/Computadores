/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores.gui;
import com.mycompany.computadores.servicios.ServicioDatos;
import com.mycompany.computadores.*; 
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author gabri_oq3uzky
 */
public class DialogoAgregarOrden extends JDialog {
    private final ServicioDatos servicio;
    private final PanelGestionOrdenes padre; // Referencia para actualizar la tabla principal

    // Campos del Formulario
    private final JTextField txtClienteNombre = new JTextField(20);
    private final JTextField txtClienteRut = new JTextField(20);
    private final JTextField txtClienteTel = new JTextField(20);
    private final JTextField txtEquipoModelo = new JTextField(20);
    private final JTextArea txtProblema = new JTextArea(4, 20);
    
    public DialogoAgregarOrden(Frame owner, ServicioDatos servicio, PanelGestionOrdenes padre) {
        super(owner, "Agregar Nueva Orden de Trabajo", true); // 'true' para hacerlo modal
        this.servicio = servicio;
        this.padre = padre;
        
        setSize(450, 450);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // --- Panel Principal del Formulario ---
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configuración inicial
        int row = 0;

        // --- Cliente ---
        panelFormulario.add(new JLabel("--- Datos del Cliente ---"), getHeaderGBC(0, row++));
        addFormField(panelFormulario, gbc, 0, row++, "Nombre Cliente:", txtClienteNombre);
        addFormField(panelFormulario, gbc, 0, row++, "RUT Cliente:", txtClienteRut);
        addFormField(panelFormulario, gbc, 0, row++, "Teléfono Cliente:", txtClienteTel);

        // --- Equipo ---
        panelFormulario.add(new JLabel("--- Datos del Equipo ---"), getHeaderGBC(0, row++));
        addFormField(panelFormulario, gbc, 0, row++, "Modelo Equipo:", txtEquipoModelo);

        // --- Problema ---
        panelFormulario.add(new JLabel("--- Descripción del Problema ---"), getHeaderGBC(0, row++));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        panelFormulario.add(new JScrollPane(txtProblema), gbc);


        add(panelFormulario, BorderLayout.CENTER);

        // --- Panel de Botones (Guardar y Cancelar) ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar Orden");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardarOrden());
        btnCancelar.addActionListener(e -> dispose()); // Cierra el diálogo

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);
    }
    
    /** Método auxiliar para simplificar la adición de campos */
    private void addFormField(JPanel panel, GridBagConstraints gbc, int x, int y, String labelText, JComponent component) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = x + 1;
        gbc.weightx = 1.0;
        panel.add(component, gbc);
    }
    
    /** Método auxiliar para estilos de encabezado */
    private GridBagConstraints getHeaderGBC(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 5, 5, 5);
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    /** Lógica de Negocio al Guardar */
    private void guardarOrden() {
        // Validación básica (solo campos no vacíos)
        if (txtClienteNombre.getText().trim().isEmpty() || txtClienteRut.getText().trim().isEmpty() ||
            txtEquipoModelo.getText().trim().isEmpty() || txtProblema.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 1. Crear los objetos modelo
            Cliente cliente = new Cliente(
                txtClienteNombre.getText().trim(), 
                txtClienteRut.getText().trim(), 
                txtClienteTel.getText().trim() // Puede ser vacío, no se valida
            );
            
            EquipoCliente equipo = new EquipoCliente(
                txtEquipoModelo.getText().trim(), 
                "N/A" // Serie por defecto si no se pide
            );
            
            // 2. Llamar al método del servicio para agregar la orden
            // NOTA: Usaremos un método de ServicioDatos que reciba todos los componentes. 
            // ASUME que en tu servicio existe:
            // public void agregarOrden(String problema, Cliente cliente, EquipoCliente equipo)
            
            servicio.agregarOrden(txtProblema.getText(), cliente, equipo);
            
            JOptionPane.showMessageDialog(this, "Orden agregada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            // 3. Notificar a la tabla principal y cerrar el diálogo
            padre.refrescarTabla(); // Llama al método que creamos en PanelGestionOrdenes
            dispose();
            
        } catch (Exception ex) {
            // Captura errores como ID duplicado, etc.
            JOptionPane.showMessageDialog(this, "Error al guardar la orden: " + ex.getMessage(), "Error de Servicio", JOptionPane.ERROR_MESSAGE);
        }
    }
}
