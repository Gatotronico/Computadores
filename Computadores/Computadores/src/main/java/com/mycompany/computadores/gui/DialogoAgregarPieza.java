/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores.gui;
import com.mycompany.computadores.*; // Importar clases modelo
import com.mycompany.computadores.servicios.ServicioDatos;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.Frame; 
/**
 *
 *
 * @author gabri_oq3uzky
 */
public class DialogoAgregarPieza extends JDialog{
    private final ServicioDatos servicio;
    private final PanelInventario padre;
    
    // Campos Comunes a Todas las Piezas
    private final JTextField txtNombre = new JTextField(20);
    private final JTextField txtStock = new JTextField("1", 20);
    private final JComboBox<String> cmbTipo;

    // Campos Específicos (Manejo con CardLayout)
    private final JPanel panelEspecifico = new JPanel(new CardLayout());
    private final Map<String, JComponent> camposEspecificos = new HashMap<>();

    // Campos para Procesador
    private final JTextField txtProcesadorSocket = new JTextField(15);
    private final JTextField txtProcesadorCores = new JTextField(15);
    
    // Campos para MemoriaRam
    private final JTextField txtRamTecnologia = new JTextField(15);
    private final JTextField txtRamVelocidad = new JTextField(15);
    private String familia;


    public DialogoAgregarPieza(Frame owner, ServicioDatos servicio, PanelInventario padre) {
        super(owner, "Agregar Nueva Pieza al Inventario", true);
        this.servicio = servicio;
        this.padre = padre;
        
        // Configurar ComboBox de Tipos
        String[] tipos = {"Pieza (Base)", "Procesador", "MemoriaRam"};
        cmbTipo = new JComboBox<>(tipos);

        setSize(450, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // --- 1. Panel de Campos Comunes ---
        JPanel panelComun = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addFormField(panelComun, gbc, 0, row++, "Tipo de Pieza:", cmbTipo);
        addFormField(panelComun, gbc, 0, row++, "Nombre:", txtNombre);
        addFormField(panelComun, gbc, 0, row++, "Stock Inicial:", txtStock);
        
        add(panelComun, BorderLayout.NORTH);

        // --- 2. Configurar CardLayout para Campos Específicos ---
        // 2.1. Panel Base (Vacío o campos Pieza genérica)
        JPanel panelBase = new JPanel();
        panelBase.add(new JLabel("No hay campos adicionales para Pieza Base."));
        panelEspecifico.add(panelBase, "Pieza (Base)");
        
        // 2.2. Panel Procesador
        JPanel panelProc = createSpecificPanel("Socket:", txtProcesadorSocket, "Núcleos (Cores):", txtProcesadorCores);
        panelEspecifico.add(panelProc, "Procesador");
        
        // 2.3. Panel MemoriaRam
        JPanel panelRam = createSpecificPanel("Tecnología (DDR3/4/5):", txtRamTecnologia, "Stock:", txtRamVelocidad);
        panelEspecifico.add(panelRam, "MemoriaRam");

        add(panelEspecifico, BorderLayout.CENTER);

        // --- 3. Panel de Botones ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar Pieza");
        btnGuardar.addActionListener(e -> guardarPieza());

        panelBotones.add(btnGuardar);
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
        
        // Listener para cambiar el panel específico
        cmbTipo.addActionListener(e -> {
            CardLayout cl = (CardLayout) (panelEspecifico.getLayout());
            cl.show(panelEspecifico, (String) cmbTipo.getSelectedItem());
        });
    }
    
    // Métodos auxiliares de diseño (similares a los de DialogoAgregarOrden)
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
    
    private JPanel createSpecificPanel(String label1, JTextField field1, String label2, JTextField field2) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormField(panel, gbc, 0, 0, label1, field1);
        addFormField(panel, gbc, 0, 1, label2, field2);
        
        return panel;
    }


    /** Lógica de Negocio al Guardar */
    private void guardarPieza() {
        String tipo = (String) cmbTipo.getSelectedItem();
        String nombre = txtNombre.getText().trim();
        int stock;
        
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre de la pieza.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            stock = Integer.parseInt(txtStock.getText().trim());
            if (stock < 0) {
                JOptionPane.showMessageDialog(this, "El stock no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 1. Crear el objeto Pieza (la lógica de ID la maneja el servicio)
            Pieza nuevaPieza = crearObjetoPieza(tipo, nombre, stock);
            
            // 2. Llamar al servicio
            servicio.agregarPieza(nuevaPieza);
            
            JOptionPane.showMessageDialog(this, "Pieza '" + nombre + "' agregada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            // 3. Notificar a la tabla principal y cerrar
            padre.refrescarTabla();
            dispose();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Stock o campos numéricos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la pieza: " + ex.getMessage(), "Error de Servicio", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /** Factory Method simple para crear la instancia de Pieza o Subclase */
    private Pieza crearObjetoPieza(String tipo, String nombre, int stock) throws NumberFormatException, Exception {
        // La ID se deja en 0, el servicio se encargará de asignarle el ID secuencial.
        switch (tipo) {
            case "Procesador":
                String socket = txtProcesadorSocket.getText().trim();
                int cores = Integer.parseInt(txtProcesadorCores.getText().trim());
                if (socket.isEmpty() || cores <= 0) throw new Exception("Faltan datos de Procesador.");
                return new Procesador(0, nombre, socket,familia , stock ); 
                //(int id,String nombre, String socket, String familia, int stock)

            case "MemoriaRam":
                String tecnologia = txtRamTecnologia.getText().trim();
                int velocidad = Integer.parseInt(txtRamVelocidad.getText().trim());
                if (tecnologia.isEmpty() || velocidad <= 0) throw new Exception("Faltan datos de Memoria RAM.");
                return new Ram(0, nombre, stock, tecnologia, velocidad);
                //(int id, String nombre,int capacidadGb , String tipoDDR, int stock)
                
            case "Pieza (Base)":
            default:
                return new Pieza(0, nombre, stock);
        }
    }
}
