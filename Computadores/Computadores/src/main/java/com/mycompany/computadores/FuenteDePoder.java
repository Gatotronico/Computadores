/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores;

/**
 *
 * @author gabri_oq3uzky
 */
public class FuenteDePoder extends Pieza{
    private int potenciaWatts;
    private String certificacion;

    public FuenteDePoder(int potenciaWatts, String certificacion, int id, String nombre, int stock) {
        super(id, nombre, stock);
        this.potenciaWatts = potenciaWatts;
        this.certificacion = certificacion;
    }

    public int getPotenciaWatts() {
        return potenciaWatts;
    }

    public void setPotenciaWatts(int potenciaWatts) {
        this.potenciaWatts = potenciaWatts;
    }

    public String getCertificacion() {
        return certificacion;
    }

    public void setCertificacion(String certificacion) {
        this.certificacion = certificacion;
    }

    @Override
    public String toString() {
        return "FuenteDePoder{" + "potenciaWatts=" + potenciaWatts + ", certificacion=" + certificacion + '}';
    }
    
    
}
