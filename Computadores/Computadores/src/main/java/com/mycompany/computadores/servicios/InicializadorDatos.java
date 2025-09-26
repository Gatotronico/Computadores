/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores.servicios;
import com.mycompany.computadores.*; 

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class InicializadorDatos {
    

    public static HashMap<Integer, Pieza> crearInventario() {
        HashMap<Integer, Pieza> inventarioStock = new HashMap<>();


        inventarioStock.put(1001, new Procesador(1001, "Procesador Intel Core i3 10100", "LGA1200", "Core i3",2));
        

        inventarioStock.put(3001, new Ram(3001, "Memoria RAM 8GB DDR4", 8, "DDR4", 17));
         
        return inventarioStock;
    }

    public static ArrayList<OrdenDeTrabajo> crearOrdenes(HashMap<Integer, Pieza> inventario) {
        ArrayList<OrdenDeTrabajo> listaOrdenes = new ArrayList<>();

        // Cliente y Equipo inicial 
        Cliente cliente1 = new Cliente("Juan Álvarez", "12345678-9", "981726354");
        EquipoCliente equipo1 = new EquipoCliente("Pavilion", "ABC-12345");
        
        // Orden de Trabajo 1 
        OrdenDeTrabajo orden1 = new OrdenDeTrabajo(
            1, 
            LocalDate.now(), 
            "No enciende. Fallo después de actualizar drivers.", 
            EstadoOrden.EN_ANALISIS, // Usando el ENUM
            cliente1, 
            equipo1
        );
        
        Pieza ram = inventario.get(3001);       
        
        if (ram != null) {
            orden1.agregarPiezasNecesarias(new PiezaNecesariaComputador(ram, 2));
        }

        listaOrdenes.add(orden1);
        
        return listaOrdenes;
    }

}