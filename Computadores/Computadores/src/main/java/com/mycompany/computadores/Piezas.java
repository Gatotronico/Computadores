/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores;

/**
 *
 * @author gabri_mpagqzf
 */
public class Piezas {
    private String nombre;
    private int id;
    private int stock;

    public Piezas(String nombre, int id, int stock) {
        this.nombre = nombre;
        this.id = id;
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public int getStock() {
        return stock;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
    
}
