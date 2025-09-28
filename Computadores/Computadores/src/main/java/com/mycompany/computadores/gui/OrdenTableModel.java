/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores.gui;
import com.mycompany.computadores.OrdenDeTrabajo;
import javax.swing.table.AbstractTableModel;
import java.util.List;
/**
 *
 * @author gabri_oq3uzky
 */
public class OrdenTableModel extends AbstractTableModel {
    private final List<OrdenDeTrabajo> listaOrdenes;
    private final String[] nombresColumnas = {"ID", "Cliente", "Fecha", "Estado", "Problema"};

    public OrdenTableModel(List<OrdenDeTrabajo> listaOrdenes) {
        this.listaOrdenes = listaOrdenes;
    }

    @Override
    public int getRowCount() {
        return listaOrdenes.size();
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
        OrdenDeTrabajo orden = listaOrdenes.get(rowIndex);

        switch (columnIndex) {
            case 0: return orden.getNumeroOrden();
            case 1: return orden.getCliente().getNombre();
            case 2: return orden.getFechaRecepcion().toString(); // Asume LocalDate
            case 3: return orden.getEstado().toString();
            case 4: return orden.getDescripcionProblema();
            default: return null;
        }
    }
    
    // Método auxiliar para obtener el objeto Orden completo
    public OrdenDeTrabajo getOrdenAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < listaOrdenes.size()) {
            return listaOrdenes.get(rowIndex);
        }
        return null;
    }
    
    // Método para forzar la actualización de la tabla (usado después de agregar/eliminar)
    public void fireTableDataChanged() {
        super.fireTableDataChanged();
    }
}
