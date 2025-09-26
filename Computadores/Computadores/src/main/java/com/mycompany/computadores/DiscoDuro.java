/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores;

/**
 *
 * @author gabri_oq3uzky
 */
public class DiscoDuro extends Pieza {
    private int capacidad;
    private int velocidad;

    public DiscoDuro(int capacidad, int velocidad, int id, String nombre, int stock) {
        super(id, nombre, stock);
        this.capacidad = capacidad;
        this.velocidad = velocidad;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    @Override
    public String toString() {
        return "DiscoDuro{" + "capacidad=" + capacidad + ", velocidad=" + velocidad + '}';
    }
    
    
}
