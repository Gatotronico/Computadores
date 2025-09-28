/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores.servicios;
import com.mycompany.computadores.Exception.PiezaNoEncontradaException;
import com.mycompany.computadores.*; 
import com.mycompany.computadores.Exception.*;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import com.mycompany.computadores.Pieza; 
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import com.mycompany.computadores.OrdenDeTrabajo;
import java.util.List;
import java.util.ArrayList;

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
    
    public List<Pieza> getListaInventario() {
        // Retorna los valores del HashMap como una nueva lista
        return new ArrayList<>(inventarioStock.values()); 
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
    
    public void eliminarPiezaDeOrden(int idOrden, int idPieza) throws OrdenNoEncontradaException {
        OrdenDeTrabajo orden = buscarOrdenPorId(idOrden);

        // Buscar y eliminar la pieza de la lista anidada usando un Iterator
        orden.getpiezasNecesariasComputador().removeIf(pnc -> {
            if (pnc.getPieza().getId() == idPieza) {
                // Reintegrar la cantidad al stock global ANTES de eliminar
                pnc.getPieza().setStock(pnc.getPieza().getStock() + pnc.getCantidad());
                return true;
            }
            return false;
        });
    }
    
    public void modificarPieza(int id, String nuevoNombre, int nuevoStock) throws PiezaNoEncontradaException {
        Pieza pieza = inventarioStock.get(id);

        if (pieza == null) {
            throw new PiezaNoEncontradaException("Pieza con ID " + id + " no encontrada.");
        }

        pieza.setNombre(nuevoNombre);
        pieza.setStock(nuevoStock);
        // Nota: Las propiedades específicas (socket, gb, etc.) requerirían un diálogo más complejo.
    }
    
    public void agregarPieza(Pieza nuevaPieza) {
    // 1. Encontrar el siguiente ID (ID máximo + 1)
    // Se asume que los IDs de pieza están en el HashMap inventarioStock
        int nuevoId = inventarioStock.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1; // Si no hay piezas, empieza en 1.

        // 2. Asignar el ID y agregar al inventario
        nuevaPieza.setId(nuevoId); 
        inventarioStock.put(nuevoId, nuevaPieza);
    }
    
    public void eliminarPieza(int id) throws PiezaNoEncontradaException {
        // Nota: Se asume que no hay validación para ver si la pieza está en uso en una orden.
        // Si la hubiera, podrías verificar listaOrdenes antes de eliminar.

        if (inventarioStock.remove(id) == null) {
            throw new PiezaNoEncontradaException("Pieza con ID " + id + " no encontrada.");
        }
    }
    
    
    public void generarReporteTxt(String rutaArchivo) throws IOException {
        try (PrintWriter reporteWriter = new PrintWriter(new FileWriter(rutaArchivo))) {

            reporteWriter.println("=================================================");
            reporteWriter.println("         REPORTE DE ÓRDENES DE TRABAJO           ");
            reporteWriter.println("=================================================");
            reporteWriter.println("Generado el: " + java.time.LocalDate.now());
            reporteWriter.println("Órdenes Totales: " + listaOrdenes.size());
            reporteWriter.println("=================================================");

            for (OrdenDeTrabajo orden : listaOrdenes) {
                reporteWriter.println("\n-------------------------------------------------");
                reporteWriter.println("ORDEN N°: " + orden.getNumeroOrden() + 
                                     " | FECHA: " + orden.getFechaRecepcion() + 
                                     " | ESTADO: " + orden.getEstado());
                reporteWriter.println("Cliente: " + orden.getCliente().getNombre() + 
                                     " (" + orden.getCliente().getRut() + ")");
                reporteWriter.println("Problema: " + orden.getDescripcionProblema());

                // Lógica para Piezas Requeridas (SIA 2.7)
                // NOTA: Asegúrate de que el método sea getPiezasNecesariasComputador() (con 'P' mayúscula)
                if (!orden.getpiezasNecesariasComputador().isEmpty()) { // << CORRECCIÓN DE CAMELCASE (image_cb9ae2.png)
                    reporteWriter.println("-> PIEZAS REQUERIDAS:");
                    for (PiezaNecesariaComputador pnc : orden.getpiezasNecesariasComputador()) {
                        reporteWriter.println("   - " + pnc.getPieza().getNombre() + 
                                             " (ID: " + pnc.getPieza().getId() + 
                                             ", Cantidad: " + pnc.getCantidad() + 
                                             ", Stock Actual: " + pnc.getPieza().getStock() + ")");
                    }
                } else {
                    reporteWriter.println("-> PIEZAS REQUERIDAS: Ninguna.");
                }
            }
            reporteWriter.println("\n=================================================");
        }
    }
    
    // Lógica para agregar piezas a la colección anidada con validación (SIA2.8, SIA2.9)
    public void agregarPiezaARequerimientos(OrdenDeTrabajo orden, Pieza pieza, int cantidad) 
            throws StockInsuficienteException {
        
        if (pieza.getStock() < cantidad) {
            throw new StockInsuficienteException("No hay stock suficiente de " + pieza.getNombre() + ". Stock: " + pieza.getStock());
        }
        
        descontarStockPieza(pieza.getId(), cantidad);
        
        orden.agregarPiezasNecesarias(new PiezaNecesariaComputador(pieza, cantidad));
    }
    
    public void descontarStockPieza(int idPieza, int cantidad) {
        Pieza pieza = inventarioStock.get(idPieza);

        if (pieza == null) {
            throw new IllegalArgumentException("Pieza con ID " + idPieza + " no encontrada en inventario.");
        }

        // Actualizar el stock
        int nuevoStock = pieza.getStock() - cantidad;
        pieza.setStock(nuevoStock);
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
    
    
    public void agregarNuevaOrdenManual(Scanner scanner) {
        
        System.out.println("\n--- REGISTRAR NUEVA ORDEN ---");
        
        // 1. Datos del Cliente (Simplificado)
        // [AQUÍ VA EL CÓDIGO PARA LEER EL CLIENTE Y EQUIPO USANDO EL SCANNER]
        System.out.print("Nombre del Cliente: ");
        String nombre = scanner.nextLine();
        System.out.print("RUT del Cliente: ");
        String rut = scanner.nextLine();
        System.out.print("Teléfono del Cliente: ");
        String telefono = scanner.nextLine();
        Cliente nuevoCliente = new Cliente(nombre, rut, telefono); 

        // 2. Datos del Equipo
        System.out.print("Modelo del Equipo: ");
        String modelo = scanner.nextLine();
        System.out.print("Número de Serie: ");
        String serie = scanner.nextLine();
        EquipoCliente nuevoEquipo = new EquipoCliente(modelo, serie);
        
        // 3. Datos de la Orden
        System.out.print("Descripción del Problema: ");
        String problema = scanner.nextLine();
        
        // Generar nuevo ID (Busca el máximo y añade 1. Ejemplo simple)
        int nuevoId = listaOrdenes.stream()
                .mapToInt(OrdenDeTrabajo::getNumeroOrden)
                .max().orElse(0) + 1;
                
        // Creamos la nueva orden (fecha actual y estado inicial)
        OrdenDeTrabajo nuevaOrden = new OrdenDeTrabajo(
            nuevoId, 
            LocalDate.now(), 
            problema, 
            EstadoOrden.EN_ANALISIS, 
            nuevoCliente, 
            nuevoEquipo
        );
        
        // Usamos el método de gestión que ya existe
        agregarOrden(nuevaOrden);
        
        System.out.println("\n✅ Orden #" + nuevoId + " registrada exitosamente.");
    }
    
    // Método para agregar la pieza al inventario (desde consola o GUI)
    public void agregarPiezaManual(Scanner scanner) {
        System.out.println("\n--- AÑADIR NUEVA PIEZA AL INVENTARIO ---");
        try {
            System.out.print("ID de la Pieza (ej: 7001): ");
            int id = scanner.nextInt();
            scanner.nextLine();
            
            if (inventarioStock.containsKey(id)) {
                System.out.println("⚠️ Error: Ya existe una pieza con el ID " + id);
                return;
            }

            System.out.print("Nombre de la Pieza: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Stock Inicial: ");
            int stock = scanner.nextInt();
            scanner.nextLine();
            
            // IMPORTANTE: Aquí deberías crear la clase hija correcta (Procesador, RAM, etc.)
            // Por simplicidad de consola, se usa la base, pero debe ser adaptado.
            Pieza nuevaPieza = new Pieza(id, nombre, stock); // Asumiendo que Pieza tiene este constructor
            
            inventarioStock.put(id, nuevaPieza);
            System.out.println("\n✅ Pieza '" + nombre + "' agregada con éxito al inventario.");

        } catch (InputMismatchException e) {
            System.out.println("❌ Entrada inválida. El ID y el Stock deben ser números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("❌ Ocurrió un error al añadir la pieza: " + e.getMessage());
        }
    }
    
    public void mostrarInventario() {
        System.out.println("\n--- INVENTARIO ACTUAL DE PIEZAS ---");

        if (inventarioStock.isEmpty()) {
            System.out.println("El inventario está vacío.");
            return;
        }

        // Encabezado de la tabla
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("| %-5s | %-30s | %-10s | %-10s |\n", "ID", "NOMBRE", "STOCK", "TIPO");
        System.out.println("-----------------------------------------------------------------");

        // Iterar sobre todos los valores (las piezas) en el HashMap
        for (Pieza pieza : inventarioStock.values()) {
            // Usamos printf para formatear la salida como una tabla
            // getClass().getSimpleName() obtiene el nombre de la clase hija (Procesador, MemoriaRam, etc.)
            System.out.printf("| %-5d | %-30s | %-10d | %-10s |\n", 
                pieza.getId(), 
                pieza.getNombre(), 
                pieza.getStock(), 
                pieza.getClass().getSimpleName()
            );
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Total de piezas diferentes: " + inventarioStock.size());
    }
    public void editarCantidadPiezaConsola(Scanner scanner) {
        System.out.println("\n--- EDITAR CANTIDAD DE PIEZA ---");
        try {
            System.out.print("Ingrese el número de orden a modificar: ");
            int idOrden = scanner.nextInt();

            System.out.print("Ingrese el ID de la pieza a editar: ");
            int idPieza = scanner.nextInt();

            System.out.print("Ingrese la NUEVA cantidad requerida: ");
            int nuevaCantidad = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            if (nuevaCantidad <= 0) {
                System.out.println("❌ Error: La cantidad debe ser mayor a cero.");
                return;
            }

            // Llama a la lógica central que maneja la búsqueda y la validación de stock
            editarCantidadPieza(idOrden, idPieza, nuevaCantidad);

        } catch (InputMismatchException e) {
            System.out.println("❌ Entrada inválida. Asegúrese de ingresar números para IDs y cantidad.");
            scanner.nextLine();
        } catch (OrdenNoEncontradaException | StockInsuficienteException e) {
            System.out.println("❌ Error de gestión: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Ocurrió un error inesperado: " + e.getMessage());
        }
    }
    public void editarCantidadPieza(int idOrden, int idPieza, int nuevaCantidad) 
        throws OrdenNoEncontradaException, StockInsuficienteException {
    
        // 1. Buscar la Orden
        OrdenDeTrabajo orden = buscarOrdenPorId(idOrden);

        // 2. Buscar la PiezaNecesariaComputador dentro de la lista anidada
        for (PiezaNecesariaComputador pnc : orden.getpiezasNecesariasComputador()) {
            if (pnc.getPieza().getId() == idPieza) {

                Pieza piezaEnInventario = pnc.getPieza();
                int diferencia = nuevaCantidad - pnc.getCantidad();

                // LÓGICA DE STOCK MEJORADA: Debe devolver el stock anterior y descontar/añadir la diferencia

                // Si la nueva cantidad es mayor a la actual (diferencia > 0), verificamos stock disponible.
                if (diferencia > 0 && piezaEnInventario.getStock() < diferencia) {
                    throw new StockInsuficienteException("Stock insuficiente para aumentar la cantidad. Max a sumar: " + piezaEnInventario.getStock());
                }

                // Aplicar el cambio de stock
                piezaEnInventario.setStock(piezaEnInventario.getStock() - diferencia);

                // 3. Modificar la cantidad en la orden
                pnc.setCantidad(nuevaCantidad);
                System.out.println("✅ Cantidad de la pieza " + pnc.getPieza().getNombre() + 
                                   " en Orden #" + idOrden + " actualizada a " + nuevaCantidad + ".");
                return;
            }
        }
        // Si llegamos aquí, la pieza no fue encontrada en ESA orden
        throw new OrdenNoEncontradaException("Pieza con ID " + idPieza + " no encontrada en la Orden #" + idOrden + ".");
    }
    
    public void eliminarPiezaDeOrdenConsola(Scanner scanner) {
        System.out.println("\n--- ELIMINAR PIEZA DE UNA ORDEN ---");
        try {
            System.out.print("Ingrese el número de orden: ");
            int idOrden = scanner.nextInt();

            System.out.print("Ingrese el ID de la pieza a ELIMINAR de la orden: ");
            int idPieza = scanner.nextInt();
            scanner.nextLine();

            // Llama a la lógica central de eliminación
            eliminarPiezaDeOrden(idOrden, idPieza);

        } catch (InputMismatchException e) {
            System.out.println("❌ Entrada inválida. Asegúrese de ingresar números para los IDs.");
            scanner.nextLine();
        } catch (OrdenNoEncontradaException e) {
            System.out.println("❌ Error de gestión: " + e.getMessage());
        }
    }
    
    public void agregarPiezaAOrden(int idOrden, int idPiezaInventario, int cantidad) 
        throws OrdenNoEncontradaException, StockInsuficienteException {

        OrdenDeTrabajo orden = buscarOrdenPorId(idOrden);

        // 1. Encontrar la pieza en el inventario global (necesitas un método o acceso a inventarioStock)
        // Asumiendo que inventarioStock es un HashMap<Integer, Pieza>
        Pieza piezaInventario = inventarioStock.get(idPiezaInventario); 

        if (piezaInventario == null) {
            throw new OrdenNoEncontradaException("La pieza ID " + idPiezaInventario + " no existe en el inventario.");
        }

        // 2. Verificar si ya existe en la orden (si ya existe, llama a editarCantidadPieza)
        for (PiezaNecesariaComputador pnc : orden.getpiezasNecesariasComputador()) {
            if (pnc.getPieza().getId() == idPiezaInventario) {
                // Si ya está, llama al método de edición para sumar la cantidad
                editarCantidadPieza(idOrden, idPiezaInventario, pnc.getCantidad() + cantidad);
                return;
            }
        }

        // 3. Validar stock
        if (piezaInventario.getStock() < cantidad) {
            throw new StockInsuficienteException("Stock insuficiente. Solo hay " + piezaInventario.getStock() + " unidades.");
        }

        // 4. Crear la PiezaNecesariaComputador y agregar a la orden
        PiezaNecesariaComputador nuevaPNC = new PiezaNecesariaComputador(piezaInventario, cantidad);
        orden.agregarPiezasNecesarias(nuevaPNC);

        // 5. Descontar stock del inventario global
        piezaInventario.setStock(piezaInventario.getStock() - cantidad);
    }
    
    public void modificarOrdenConsola(Scanner scanner) {
        System.out.println("\n--- MODIFICAR ORDEN EXISTENTE ---");
        try {
            System.out.print("Ingrese el número de orden a modificar: ");
            int idOrden = scanner.nextInt();
            scanner.nextLine();

            // 1. Mostrar estado actual y solicitar nueva descripción
            OrdenDeTrabajo orden = buscarOrdenPorId(idOrden);
            System.out.println("Orden #" + idOrden + " - Estado actual: " + orden.getEstado());
            System.out.print("Ingrese nueva descripción del problema (deje vacío para mantener): ");
            String nuevaDescripcion = scanner.nextLine();

            // 2. Solicitar y procesar el nuevo estado
            EstadoOrden nuevoEstado = null;
            System.out.println("\nSeleccione el nuevo estado:");
            EstadoOrden[] estados = EstadoOrden.values();
            for (int i = 0; i < estados.length; i++) {
                System.out.println((i + 1) + ") " + estados[i]);
            }
            System.out.print("Ingrese el número de la opción de estado (o 0 para no cambiar): ");
            int opcionEstado = scanner.nextInt();
            scanner.nextLine();

            if (opcionEstado > 0 && opcionEstado <= estados.length) {
                nuevoEstado = estados[opcionEstado - 1];
            } else if (opcionEstado != 0) {
                System.out.println("⚠️ Opción de estado no válida. El estado no será modificado.");
            }

            // 3. Llamar a la lógica central de modificación
            modificarOrdenExistente(idOrden, nuevaDescripcion, nuevoEstado);

        } catch (InputMismatchException e) {
            System.out.println("❌ Entrada inválida. Asegúrese de ingresar números donde corresponde.");
            scanner.nextLine();
        } catch (OrdenNoEncontradaException e) {
            System.out.println("❌ Error de gestión: " + e.getMessage());
        }
    }
    
    public void modificarOrdenExistente(int idOrden, String nuevaDescripcion, EstadoOrden nuevoEstado) 
            throws OrdenNoEncontradaException {

        // 1. Buscar la Orden
        OrdenDeTrabajo orden = buscarOrdenPorId(idOrden);

        // 2. Aplicar las modificaciones
        if (nuevaDescripcion != null && !nuevaDescripcion.trim().isEmpty()) {
            orden.setDescripcionProblema(nuevaDescripcion);
        }

        if (nuevoEstado != null) {
            orden.setEstado(nuevoEstado);
        }

        System.out.println("✅ Orden #" + idOrden + " modificada exitosamente.");
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
    
    public void agregarOrden(String problema, Cliente cliente, EquipoCliente equipo) {
        // 1. Encontrar el siguiente ID (ID máximo + 1)
        int nuevoId = listaOrdenes.stream()
                .mapToInt(OrdenDeTrabajo::getNumeroOrden)
                .max()
                .orElse(0) + 1; // Si no hay órdenes, empieza en 1.

        // 2. Crear la Orden (con fecha de hoy y estado inicial)
        OrdenDeTrabajo nuevaOrden = new OrdenDeTrabajo(
            nuevoId,
            LocalDate.now(),
            problema,
            EstadoOrden.EN_ANALISIS, // Asume que el estado inicial es EN_ANALISIS
            cliente,
            equipo
        );

        // 3. Agregar a la colección
        this.listaOrdenes.add(nuevaOrden);
    }
}