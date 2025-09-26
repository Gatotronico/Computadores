/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores;
import java.util.ArrayList;
import java.time.LocalDate;
/**
 *
 * @author gabri_mpagqzf
 */
public class OrdenDeTrabajo {
    private int numeroOrden;
    private LocalDate fechaRecepcion; 
    private String descripcionProblema;
    private EstadoOrden estado;
    private LocalDate fechaEstimadaEntrega;
    
    
    //relaciones
    private Cliente cliente;
    private EquipoCliente equipoCliente;
    private ArrayList<PiezaNecesariaComputador> piezasNecesariasComputador;
    
    public OrdenDeTrabajo(int numeroOrden, LocalDate fechaRecepcion, String descripcionProblema, EstadoOrden estado, Cliente cliente, EquipoCliente equipoCliente){
        this.numeroOrden = numeroOrden;
        this.fechaRecepcion = fechaRecepcion;
        this.descripcionProblema = descripcionProblema;
        this.estado= estado;
        this.cliente =cliente;
        this.equipoCliente = equipoCliente;
        this.piezasNecesariasComputador = new ArrayList<>(); 
    }

    public void setNumeroOrden(int numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public void setFechaRecepcion(LocalDate fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public void setDescripcionProblema(String descripcionProblema) {
        this.descripcionProblema = descripcionProblema;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public void setFechaEstimadaEntrega(LocalDate fechaEstimadaEntrega) {
        this.fechaEstimadaEntrega = fechaEstimadaEntrega;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setEquipoCliente(EquipoCliente equipoCliente) {
        this.equipoCliente = equipoCliente;
    }

    public void setPiezaNecesariaComputador(ArrayList<PiezaNecesariaComputador> PiezaNecesariaComputador) {
        this.piezasNecesariasComputador = PiezaNecesariaComputador;
    }

    public int getNumeroOrden() {
        return numeroOrden;
    }

    public LocalDate getFechaRecepcion() {
        return fechaRecepcion;
    }

    public String getDescripcionProblema() {
        return descripcionProblema;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public LocalDate getFechaEstimadaEntrega() {
        return fechaEstimadaEntrega;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public EquipoCliente getEquipoCliente() {
        return equipoCliente;
    }

    public ArrayList<PiezaNecesariaComputador> getpiezasNecesariasComputador() {
        return piezasNecesariasComputador;
    }
    
    //metodo agregar piezas para el pc
    public void agregarPiezasNecesarias(PiezaNecesariaComputador pieza) {
        this.piezasNecesariasComputador.add(pieza);
    }

    public void estimarFechaDeEntrega(int dias) {
        this.fechaEstimadaEntrega = this.fechaRecepcion.plusDays(dias); 
    }
}