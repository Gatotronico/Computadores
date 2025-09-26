package com.mycompany.computadores;

import java.util.Scanner;
import com.mycompany.computadores.servicios.ServicioDatos; // Asume que ya creaste el paquete servicios
import java.util.InputMismatchException;

public class Computadores {

    public static void main(String[] args) {

        ServicioDatos servicio = new ServicioDatos();
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- Menú Principal Compus (Consola) ---");
            System.out.println("1) Agregar pieza a una orden (con validación de stock)");
            System.out.println("2) Mostrar todas las órdenes y sus piezas");
            System.out.println("3) Generar Reporte TXT");
            System.out.println("4) Salir (Guarda los datos)");
            
            try {
                System.out.print("Ingrese una opción: ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); 
                switch (opcion) {
                    case 1:

                        servicio.agregarPiezaAOrdenPorConsola(scanner); 
                        break;
                    case 2:
                        servicio.mostrarOrdenesYPiezas();
                        break;
                    case 3:
                        servicio.generarReporteDetallado();
                        break;
                    case 4:
                        System.out.println("Guardando datos y saliendo...");
                        servicio.guardarDatos(); 
                        salir = true;
                        break;
                    default:
                        System.out.println("Opción no válida. Intente otra vez.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("ERROR: Entrada inválida. Por favor, ingrese un número.");
                scanner.nextLine(); // Limpiar el buffer
            } catch (Exception e) {
                 System.out.println("ERROR: Ocurrió un error en la operación: " + e.getMessage());
            }
        }
        scanner.close();
    }
}