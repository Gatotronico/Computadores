/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores;
import java.util.ArrayList;
import java.util.Date;
/**
 *
 * @author gabri_mpagqzf
 */
public class OrdenDeTrabajo {
    private int numeroOrden;
    private Date fechaRecepcion;
    private String descripcionProblema;
    private String estado;
    private Date fechaEstimadaEntrega;
    
    //relaciones
    private Cliente cliente;
    private EquipoCliente equipoCliente;
    private ArrayList<PiezaNecesariaComputador> PiezaNecesariaComputador;
    
    
    public OrdenDeTrabajo(int numeroOrden, Date fechaRecepcion,String descripcionProblema, String estado, Cliente cliente, EquipoCliente equipoCliente){
        this.numeroOrden = numeroOrden;
        this.fechaRecepcion = fechaRecepcion;
        this.descripcionProblema = descripcionProblema;
        this.estado= estado;
        this.cliente =cliente;
        this.equipoCliente = equipoCliente;
        this.PiezaNecesariaComputador = new ArrayList<>();
    }

    public void setNumeroOrden(int numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public void setFechaRecepcion(Date fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public void setDescripcionProblema(String descripcionProblema) {
        this.descripcionProblema = descripcionProblema;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFechaEstimadaEntrega(Date fechaEstimadaEntrega) {
        this.fechaEstimadaEntrega = fechaEstimadaEntrega;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setEquipoCliente(EquipoCliente equipoCliente) {
        this.equipoCliente = equipoCliente;
    }

    public void setPiezaNecesariaComputador(ArrayList<PiezaNecesariaComputador> PiezaNecesariaComputador) {
        this.PiezaNecesariaComputador = PiezaNecesariaComputador;
    }

    public int getNumeroOrden() {
        return numeroOrden;
    }

    public Date getFechaRecepcion() {
        return fechaRecepcion;
    }

    public String getDescripcionProblema() {
        return descripcionProblema;
    }

    public String getEstado() {
        return estado;
    }

    public Date getFechaEstimadaEntrega() {
        return fechaEstimadaEntrega;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public EquipoCliente getEquipoCliente() {
        return equipoCliente;
    }

    public ArrayList<PiezaNecesariaComputador> getPiezaNecesariaComputador() {
        return PiezaNecesariaComputador;
    }
    
    //metodo agregar piezas para el pc
    public void agregarPiezasNecesarias(PiezaNecesariaComputador pieza){
        this.PiezaNecesariaComputador.add(pieza);
    }
    
    public void estimarFechaDeEntrega(int dias){
        long millis= fechaRecepcion.getTime() +(dias *24L *60L *60L *1000L);
        this.fechaEstimadaEntrega= new Date(millis);
    }
    
}