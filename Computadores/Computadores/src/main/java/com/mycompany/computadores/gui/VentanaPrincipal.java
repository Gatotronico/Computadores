/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores.gui;
import com.mycompany.computadores.servicios.ServicioDatos;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
/**
 *
 * @author gabri_oq3uzky
 */
public class VentanaPrincipal extends JFrame {
    private ServicioDatos servicio;
    private JTabbedPane tabbedPane;

    public VentanaPrincipal() {
        this.servicio = new ServicioDatos();
        
        setTitle("Sistema de Gestión de Órdenes");
        // DEBES implementar el guardado de datos aquí al cerrar la ventana
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Cambiado a DO_NOTHING para manejar el cierre
        setSize(1000, 700);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        
        // Pestaña 1: GESTIÓN DE ÓRDENES (USA TU NUEVA CLASE)
        PanelGestionOrdenes panelOrdenes = new PanelGestionOrdenes(this.servicio); 
        tabbedPane.addTab("Gestión de Órdenes", panelOrdenes);
        
        // Pestaña 2: Inventario (AÚN PENDIENTE)
        PanelInventario panelInventario = new PanelInventario(this.servicio);
        tabbedPane.addTab("Inventario y Piezas", panelInventario);
        
        // Pestaña 3: Reportes (AÚN PENDIENTE)
        PanelReportes panelReportes = new PanelReportes(this.servicio);
        tabbedPane.addTab("Reportes y Estadísticas", panelReportes);

        add(tabbedPane, BorderLayout.CENTER);
        
        // Implementar el Listener para el guardado de datos al cerrar (SIA2.2)
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    servicio.guardarDatos();
                    JOptionPane.showMessageDialog(VentanaPrincipal.this, "Datos guardados exitosamente.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(VentanaPrincipal.this, "Error al guardar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    dispose(); // Cierra la ventana
                    System.exit(0); // Termina la aplicación
                }
            }
        });
    }
    
    public void iniciar() {
        setVisible(true);
    }
}
