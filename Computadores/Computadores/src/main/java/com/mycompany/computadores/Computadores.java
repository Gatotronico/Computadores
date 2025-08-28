package com.mycompany.computadores;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import com.mycompany.computadores.Cliente;
import com.mycompany.computadores.EquipoCliente;
import com.mycompany.computadores.OrdenDeTrabajo;
import com.mycompany.computadores.PiezaNecesariaComputador;
import com.mycompany.computadores.Piezas;

/**
 *
 * @author gabri_mpagqzf
 */
public class Computadores {

    public static void main(String[] args) {
        ArrayList<OrdenDeTrabajo> listaOrdenes = new ArrayList<>();
        HashMap< Integer, Piezas> inventarioStock = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        Date fecha = new Date();
        //inventario inicial
        
        /*
        los id son exactos, los 1000 son para procesador, los 2000 son para placas madres
        los 3000 son memorias ram, los 4000 son graficas, los 5000 son memorias hdd y los 6000 son ssd
        */
        inventarioStock.put(4001, new Piezas("Tarjeta Grafica RTX 5060",4001,5));
        inventarioStock.put(3001, new Piezas("Memoria Ram 8GB DDR4",3001,17));
        inventarioStock.put(3002, new Piezas("Memoria Ram 16GB DDR4",3002,8));
        inventarioStock.put(1001, new Piezas("Procesador Intel Core I3 10100",1001,2));
        inventarioStock.put(1002, new Piezas("Porcesador AMD Ryzen 3100",1002,3));
        inventarioStock.put(2001, new Piezas("Placa Madre B450",2001,4));
        
        //clientes inciales
        //cliente 1
        //es nombre con apellido juntos, rut y numero de telefono
        Cliente cliente1 = new Cliente("Juan Alvarez","12345678-9","981726354");
        //el equipo es solo el modelo y su numero de serie
        EquipoCliente equipo1 = new EquipoCliente("Pavilion","ABC-12345");
        //aqui se agrega todo como orden de trabajo, el cual va id, fecha con date now, que le pasa, su estado actual,
        //posible fecha o null y los datos recien agregados
        OrdenDeTrabajo orden1 = new OrdenDeTrabajo (1, fecha,"No enciende","En analisis",cliente1,equipo1);
        orden1.getPiezaNecesariaComputador().add(new PiezaNecesariaComputador(inventarioStock.get(1001),1));
        orden1.getPiezaNecesariaComputador().add(new PiezaNecesariaComputador(inventarioStock.get(3001),2));
        
        listaOrdenes.add(orden1);
        
        
        boolean salir = false;
        while (!salir){
            System.out.println("Menu Compus");
            System.out.println("1) Agregar pieza a una orden");
            System.out.println("2) Mostrar ordenes y sus piezas");
            System.out.println("3) Salir");
            try {
                int opcion = scanner.nextInt();
                scanner.nextLine();
                switch(opcion){
                    
                    case 1:
                        System.out.print("Ingrese el numero de orden de trabajo: ");
                        int numOrden=scanner.nextInt();
                        scanner.nextLine();
                        
                        OrdenDeTrabajo ordenAModificar = null;
                        for (OrdenDeTrabajo orden : listaOrdenes){
                            if(orden.getNumeroOrden()== numOrden){}
                                ordenAModificar=orden;
                                break;
                        }
                        if (ordenAModificar != null){
                            System.out.print("Ingrese el ID de la pieza a agregar: ");
                            int idPieza = scanner.nextInt();
                            scanner.nextLine();
                            Piezas piezaEnStock = inventarioStock.get(idPieza);
                            if (piezaEnStock != null){
                                System.out.print("Ingrese la cantidad requerida: ");
                                int cantidad = scanner.nextInt();
                                scanner.nextInt();
                                ordenAModificar.getPiezaNecesariaComputador().add(new PiezaNecesariaComputador(piezaEnStock,cantidad));
                                System.out.println("Agregada correctamente");
                               
                            }else{
                                System.out.println("ID de Pieza no encontrada");
                            }  
                        }else{
                            System.out.println("Orden no encontrada");
                        }
                        break;
                    
                    case 2:
                        System.out.println("Lista de ordenes de trabajo");
                        if (listaOrdenes.isEmpty()){
                            System.out.println("No hay ordenes ingresadas");
                        }else{
                            for(OrdenDeTrabajo orden : listaOrdenes){
                                System.out.println("Orden #"+ orden.getNumeroOrden());
                                System.out.println("Descripcion: "+ orden.getDescripcionProblema());
                                System.out.println("Estado: "+ orden.getEstado());
                                System.out.println("Cliente: "+orden.getCliente().getNombre());
                                System.out.println("Piezas requeridas:");
                                for (PiezaNecesariaComputador piezaReq: orden.getPiezaNecesariaComputador()){
                                    System.out.println(" -"+piezaReq.getPiezas().getNombre() +" (Cantidad: "+piezaReq.getCantidad() +")");
                                }
                            }
                        }
                        break;
                
                    case 3:
                        System.out.println("Nos Vemos");
                        salir=true;
                        break;
                        
                    default:
                        System.out.println("Opcion no valida, otra por favor");
                        break;
                }
            } catch(InputMismatchException e){
                System.out.println("Entrada invalida, intente otra vez");
                scanner.nextLine();
            }
        }
        scanner.close();
    }
}
