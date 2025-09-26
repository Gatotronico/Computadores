/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores;

/**
 *
 * @author gabri_oq3uzky
 */
public class SSD extends Pieza{
    private int Capacidad;
    private int tipoConector; //sata o nvme

    public SSD(int Capacidad, int tipoConector, int id, String nombre, int stock) {
        super(id, nombre, stock);
        this.Capacidad = Capacidad;
        this.tipoConector = tipoConector;
    }

    public int getCapacidad() {
        return Capacidad;
    }

    public void setCapacidad(int Capacidad) {
        this.Capacidad = Capacidad;
    }

    public int getTipoConector() {
        return tipoConector;
    }

    public void setTipoConector(int tipoConector) {
        this.tipoConector = tipoConector;
    }

    @Override
    public String toString() {
        return "SSD{" + "Capacidad=" + Capacidad + ", tipoConector=" + tipoConector + '}';
    }
    
    
}
