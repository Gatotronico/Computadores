/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores;

/**
 *
 * @author gabri_mpagqzf
 */
public class PiezaNecesariaComputador {
    private int cantidad;
    private Piezas piezas;

    public PiezaNecesariaComputador( Piezas piezas,int cantidad) {
        this.cantidad = cantidad;
        this.piezas = piezas;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Piezas getPiezas() {
        return piezas;
    }

    public void setPiezas(Piezas piezas) {
        this.piezas = piezas;
    }
    
    
}
