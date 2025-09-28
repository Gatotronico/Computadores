/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores.gui;
import com.mycompany.computadores.Pieza;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author gabri_oq3uzky
 */
public class InventarioTableModel extends AbstractTableModel{
    private List<Pieza> listaInventario;
    private final String[] nombresColumnas = {"ID", "Nombre", "Clase", "Stock"};

    public InventarioTableModel(List<Pieza> listaInventario) {
        // Filtramos para mostrar solo piezas con stock positivo
        this.listaInventario = listaInventario.stream()
                                              .filter(p -> p.getStock() > 0)
                                              .collect(Collectors.toList());
    }

    @Override
    public int getRowCount() {
        return listaInventario.size();
    }

    @Override
    public int getColumnCount() {
        return nombresColumnas.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return nombresColumnas[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pieza pieza = listaInventario.get(rowIndex);

        switch (columnIndex) {
            case 0: return pieza.getId();
            case 1: return pieza.getNombre();
            case 2: return pieza.getClass().getSimpleName(); // Muestra si es Procesador, Ram, o Pieza
            case 3: return pieza.getStock();
            default: return null;
        }
    }
    public void setPiezas(List<Pieza> nuevasPiezas) {
        // Filtramos para mostrar solo piezas con stock positivo
        this.listaInventario = nuevasPiezas.stream()
                                          .filter(p -> p.getStock() > 0)
                                          .collect(Collectors.toList());
        // NO llamas a fireTableDataChanged() aquí, lo hace el PanelInventario
    }
    
    // Método auxiliar para obtener el objeto completo
    public Pieza getPiezaAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < listaInventario.size()) {
            return listaInventario.get(rowIndex);
        }
        return null;
    }
}
