/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores;

/**
 *
 * @author gabri_oq3uzky
 */
public class PlacaMadre extends Pieza {
    private String socket;
    private String formato;

    public PlacaMadre(String socket, String formato, int id, String nombre, int stock) {
        super(id, nombre, stock);
        this.socket = socket;
        this.formato = formato;
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    @Override
    public String toString() {
        return "PlacaMadre{" + "socket=" + socket + ", formato=" + formato + '}';
    }
    
    
    
}
