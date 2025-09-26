/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores;

/**
 *
 * @author gabri_oq3uzky
 */
public class Procesador extends Pieza {
    private String socket;
    private String familia;
                    //1001, "Procesador Intel Core i3 10100" "LGA1200", "Core i3, 2"
    public Procesador(int id,String nombre, String socket, String familia, int stock) {
        super(id, nombre, stock);
        this.socket = socket;
        this.familia = familia;
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    @Override
    public String toString() {
        return "Procesador{" + "socket=" + socket + ", familia=" + familia + '}';
    }
    
    
}
