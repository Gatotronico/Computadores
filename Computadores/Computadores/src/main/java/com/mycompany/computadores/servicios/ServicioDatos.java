/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores.servicios;
import com.mycompany.computadores.*; 
import com.mycompany.computadores.Exception.*;
import java.io.*;
import java.util.*;
import java.time.LocalDate;

public class ServicioDatos {
    
    // Colecciones que gestiona el Servicio
    // Cambiado 'Pieza' a la clase abstracta base
    private HashMap<Integer, Pieza> inventarioStock;
    private ArrayList<OrdenDeTrabajo> listaOrdenes;
    
    // Archivos para persistencia (SIA2.2 y SIA2.10)
    private static final String ARCHIVO_ORDENES = "ordenes.csv";
    private static final String ARCHIVO_INVENTARIO = "inventario.csv";

    // Constructor: Se ejecuta al inicio de la aplicación.
    public ServicioDatos() {
        this.inventarioStock = new HashMap<>();
        this.listaOrdenes = new ArrayList<>();
        cargarDatos(); 
    }

    // --- MÉTODOS DE GESTIÓN BÁSICA Y ACCESO (GETTERS) ---

    public ArrayList<OrdenDeTrabajo> getListaOrdenes() { 
        return listaOrdenes; 
    }
    
    public HashMap<Integer, Pieza> getInventarioStock() {
        return inventarioStock;
    }
    
    // SIA2.12: Agregar una nueva Orden
    public void agregarOrden(OrdenDeTrabajo orden) {
        this.listaOrdenes.add(orden);
    }
    
    // SIA2.12: Eliminar Orden
    public void eliminarOrden(int id) throws OrdenNoEncontradaException {
        OrdenDeTrabajo ordenAEliminar = buscarOrdenPorId(id); 
        this.listaOrdenes.remove(ordenAEliminar);
    }

    // Búsqueda de un elemento por ID (SIA2.13)
    public OrdenDeTrabajo buscarOrdenPorId(int id) throws OrdenNoEncontradaException {
        for (OrdenDeTrabajo orden : listaOrdenes) {
            if (orden.getNumeroOrden() == id) {
                return orden;
            }
        }
        throw new OrdenNoEncontradaException("La orden con ID " + id + " no existe.");
    }
    
    // Funcionalidad Propia: Subconjunto filtrado por criterio (SIA2.5)
    public List<OrdenDeTrabajo> filtrarPorEstado(EstadoOrden estado) {
        List<OrdenDeTrabajo> filtradas = new ArrayList<>();
        for (OrdenDeTrabajo orden : listaOrdenes) {
            if (orden.getEstado() == estado) {
                filtradas.add(orden);
            }
        }
        return filtradas;
    }
    
    // Lógica para agregar piezas a la colección anidada con validación (SIA2.8, SIA2.9)
    public void agregarPiezaARequerimientos(OrdenDeTrabajo orden, Pieza pieza, int cantidad) 
            throws StockInsuficienteException {
        
        if (pieza.getStock() < cantidad) {
            throw new StockInsuficienteException("No hay stock suficiente de " + pieza.getNombre() + ". Stock: " + pieza.getStock());
        }
        
        orden.agregarPiezasNecesarias(new PiezaNecesariaComputador(pieza, cantidad));
    }
    
// ----------------------------------------------------------------------

    // --- MÉTODOS DE CONSOLA / INTERFAZ ---

    // Nuevo método para manejar la interacción de consola (llamado desde main)
    public void agregarPiezaAOrdenPorConsola(Scanner scanner) throws Exception {
        System.out.print("Ingrese el número de orden de trabajo: ");
        int numOrden = scanner.nextInt();
        scanner.nextLine(); // Consumir
        
        OrdenDeTrabajo ordenAModificar = buscarOrdenPorId(numOrden);

        System.out.print("Ingrese el ID de la pieza a agregar: ");
        int idPieza = scanner.nextInt();
        scanner.nextLine();

        Pieza piezaEnStock = inventarioStock.get(idPieza);

        if (piezaEnStock != null) {
            System.out.print("Ingrese la cantidad requerida: ");
            int cantidad = scanner.nextInt();
            scanner.nextLine();

            // Llama a la lógica central con validación
            agregarPiezaARequerimientos(ordenAModificar, piezaEnStock, cantidad); 

            System.out.println("Pieza " + piezaEnStock.getNombre() + " agregada correctamente a la Orden #" + numOrden);

        } else {
            System.out.println("ID de Pieza no encontrada en inventario.");
        }
    }
    
    // Nuevo método para mostrar todas las órdenes (usado por el main de consola)
    public void mostrarOrdenesYPiezas() {
        System.out.println("\n--- Lista de Órdenes de Trabajo ---");
        if (listaOrdenes.isEmpty()) {
            System.out.println("No hay órdenes ingresadas.");
            return;
        }
        
        for (OrdenDeTrabajo orden : listaOrdenes) {
            System.out.println("----------------------------------------");
            System.out.println("Orden #" + orden.getNumeroOrden() + " (Estado: " + orden.getEstado() + ")");
            System.out.println("Cliente: " + orden.getCliente().getNombre());
            
            System.out.println("Piezas requeridas:");
            if (orden.getpiezasNecesariasComputador().isEmpty()) {
                 System.out.println(" - Ninguna pieza requerida aún.");
            } else {
                for (PiezaNecesariaComputador piezaReq : orden.getpiezasNecesariasComputador()) {
                    System.out.println(" - " + piezaReq.getPieza().getNombre() + 
                                       " (Cant: " + piezaReq.getCantidad() + ")");
                }
            }
        }
        System.out.println("----------------------------------------");
    }

// ----------------------------------------------------------------------

    // --- MÉTODOS DE PERSISTENCIA Y CARGA ---
    
    // (Resto de cargarDatos(), inicializarDatosDePrueba(), guardarDatos(), y generarReporteDetallado() queda igual)
    // ...

    private void cargarDatos() {
        System.out.println("Intentando cargar datos de persistencia...");
        try {
             File archivo = new File(ARCHIVO_ORDENES);
             if (!archivo.exists() || archivo.length() == 0) {
                 inicializarDatosDePrueba();
             } else {
                 System.out.println("Archivos encontrados, inicializando con simulación de datos.");
                 inicializarDatosDePrueba();
             }
        } catch (Exception e) {
             System.err.println("Error al verificar archivos. Inicializando con datos de prueba: " + e.getMessage());
             inicializarDatosDePrueba(); 
        }
    }
    
    private void inicializarDatosDePrueba() {
        // Asegúrate de que InicializadorDatos esté en el paquete servicios
        this.inventarioStock = InicializadorDatos.crearInventario();
        this.listaOrdenes = InicializadorDatos.crearOrdenes(this.inventarioStock);
        System.out.println("Datos de prueba cargados: " + listaOrdenes.size() + " órdenes.");
    }

    public void guardarDatos() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_ORDENES))) {
            for (OrdenDeTrabajo orden : listaOrdenes) {
                writer.println(orden.getNumeroOrden() + "," + orden.getCliente().getNombre() + "," + orden.getFechaRecepcion() + "," + orden.getEstado() + "," + orden.getDescripcionProblema());
            }
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_INVENTARIO))) {
            for (Pieza pieza : inventarioStock.values()) {
                writer.println(pieza.getId() + "," + pieza.getNombre() + "," + pieza.getStock() + "," + pieza.getClass().getSimpleName());
            }
        }
        
        System.out.println("Persistencia completada. Datos guardados en " + ARCHIVO_ORDENES + " y " + ARCHIVO_INVENTARIO);
    }
    
    public void generarReporteDetallado() {
        try (PrintWriter reporteWriter = new PrintWriter(new FileWriter("REPORTE_ORDENES_DETALLE.txt"))) {
            reporteWriter.println("--- REPORTE DETALLADO DE ÓRDENES DE TRABAJO ---");
            reporteWriter.println("Generado el: " + LocalDate.now());
            reporteWriter.println("Total de Órdenes: " + listaOrdenes.size() + "\n");
            
            for (OrdenDeTrabajo orden : listaOrdenes) {
                reporteWriter.println("-------------------------------------------------");
                reporteWriter.println("ORDEN N°: " + orden.getNumeroOrden() + " | ESTADO: " + orden.getEstado());
                reporteWriter.println("Cliente: " + orden.getCliente().getNombre());
                reporteWriter.println("Problema: " + orden.getDescripcionProblema());
                
                // USANDO EL GETTER CORREGIDO: getPiezasNecesariasComputador()
                if (!orden.getpiezasNecesariasComputador().isEmpty()) { 
                    reporteWriter.println("  -> PIEZAS REQUERIDAS:");
                    for (PiezaNecesariaComputador pnc : orden.getpiezasNecesariasComputador()) {
                        reporteWriter.println("     - Cantidad: " + pnc.getCantidad() + " | " + pnc.getPieza().toString()); 
                    }
                }
                reporteWriter.println("-------------------------------------------------");
            }
            System.out.println("Reporte generado con éxito en REPORTE_ORDENES_DETALLE.txt");
        } catch (IOException e) {
            System.err.println("ERROR al generar el reporte: " + e.getMessage());
        }
    }
}