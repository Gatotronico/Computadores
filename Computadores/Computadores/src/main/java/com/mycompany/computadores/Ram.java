/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores;

/**
 *
 * @author gabri_oq3uzky
 */
public class Ram extends Pieza {
    private int capacidadGb;
    private String tipoDDR;

    public Ram(int id, String nombre,int capacidadGb , String tipoDDR, int stock) {
        super(id, nombre, stock);
        this.capacidadGb = capacidadGb;
        this.tipoDDR = tipoDDR;
    }

    public int getCapacidadGb() {
        return capacidadGb;
    }

    public void setCapacidadGb(int capacidadGb) {
        this.capacidadGb = capacidadGb;
    }

    public String getTipoDDR() {
        return tipoDDR;
    }

    public void setTipoDDR(String tipoDDR) {
        this.tipoDDR = tipoDDR;
    }

    @Override
    public String toString() {
        return "Ram{" + "capacidadGb=" + capacidadGb + ", tipoDDR=" + tipoDDR + '}';
    }
    
                    
}
