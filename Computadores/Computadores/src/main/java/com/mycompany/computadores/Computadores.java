package com.mycompany.computadores;

import com.mycompany.computadores.gui.VentanaPrincipal; // Importa tu nueva clase
import javax.swing.SwingUtilities;

public class Computadores {

    public static void main(String[] args) {
        
        // Usamos SwingUtilities.invokeLater para iniciar la GUI de forma segura
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.iniciar();
        });
        
        // ¡La lógica de consola (Scanner) ya NO va aquí!
    }
}