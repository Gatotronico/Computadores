/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores;

/**
 *
 * @author gabri_oq3uzky
 */
public class TarjetaGrafica extends Pieza{
    private String Fabricante;
    private int Vram;
    private String Modelo;

    public TarjetaGrafica(String Fabricante, int Vram, String Modelo, int id, String nombre, int stock) {
        super(id, nombre, stock);
        this.Fabricante = Fabricante;
        this.Vram = Vram;
        this.Modelo = Modelo;
    }

    public String getFabricante() {
        return Fabricante;
    }

    public void setFabricante(String Fabricante) {
        this.Fabricante = Fabricante;
    }

    public int getVram() {
        return Vram;
    }

    public void setVram(int Vram) {
        this.Vram = Vram;
    }

    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String Modelo) {
        this.Modelo = Modelo;
    }

    @Override
    public String toString() {
        return "TarjetaGrafica{" + "Fabricante=" + Fabricante + ", Vram=" + Vram + ", Modelo=" + Modelo + '}';
    }
    
}
