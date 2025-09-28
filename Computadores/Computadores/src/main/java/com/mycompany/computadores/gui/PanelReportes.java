/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores.gui;
import com.mycompany.computadores.servicios.ServicioDatos;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
/**
 *
 * @author gabri_oq3uzky
 */
public class PanelReportes extends JPanel {
    private final ServicioDatos servicio;
    private final JFileChooser fileChooser;
    private final JLabel lblRutaGuardado;

    public PanelReportes(ServicioDatos servicio) {
        this.servicio = servicio;
        this.fileChooser = new JFileChooser();
        this.lblRutaGuardado = new JLabel("Ruta del Archivo Generado: ", SwingConstants.CENTER);

        setLayout(new BorderLayout(10, 10));

        // 1. Título
        JLabel lblTitulo = new JLabel("Generación de Reportes y Estadísticas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.NORTH);

        // 2. Panel Central de Controles
        JPanel panelCentral = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        JButton btnGenerar = new JButton("💾 Generar Reporte TXT de Órdenes");
        btnGenerar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnGenerar.setPreferredSize(new Dimension(300, 40));
        
        btnGenerar.addActionListener(e -> generarReporte());
        
        panelCentral.add(btnGenerar, gbc);

        gbc.gridy = 1;
        panelCentral.add(lblRutaGuardado, gbc);
        
        add(panelCentral, BorderLayout.CENTER);
    }
    
    private void generarReporte() {
        // Configura el selector de archivos para guardar
        fileChooser.setDialogTitle("Guardar Reporte de Órdenes");
        fileChooser.setSelectedFile(new File("ReporteOrdenes.txt")); 
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            // Asegurarse de que tenga la extensión .txt
            if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }
            
            try {
                // Llamada a la lógica del servicio
                servicio.generarReporteTxt(fileToSave.getAbsolutePath());
                
                lblRutaGuardado.setText("Ruta del Archivo Generado: " + fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Reporte generado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + ex.getMessage(), "Error de I/O", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
