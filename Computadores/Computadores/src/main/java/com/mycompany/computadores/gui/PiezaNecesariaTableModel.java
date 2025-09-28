/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores.gui;
import com.mycompany.computadores.PiezaNecesariaComputador;
import javax.swing.table.AbstractTableModel;
import java.util.List;
/**
 *
 * @author gabri_oq3uzky
 */
public class PiezaNecesariaTableModel extends AbstractTableModel {
    private final List<PiezaNecesariaComputador> listaPiezas;
    private final String[] nombresColumnas = {"ID Pieza", "Nombre", "Cantidad Req.", "Stock Disponible"};

    public PiezaNecesariaTableModel(List<PiezaNecesariaComputador> listaPiezas) {
        this.listaPiezas = listaPiezas;
    }

    @Override
    public int getRowCount() {
        return listaPiezas.size();
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
        PiezaNecesariaComputador pnc = listaPiezas.get(rowIndex);

        switch (columnIndex) {
            case 0: return pnc.getPieza().getId();
            case 1: return pnc.getPieza().getNombre();
            case 2: return pnc.getCantidad();
            case 3: return pnc.getPieza().getStock(); // Muestra el stock actual del inventario
            default: return null;
        }
    }
    
    // Método auxiliar para obtener el objeto completo
    public PiezaNecesariaComputador getPiezaNecesariaAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < listaPiezas.size()) {
            return listaPiezas.get(rowIndex);
        }
        return null;
    }
}
