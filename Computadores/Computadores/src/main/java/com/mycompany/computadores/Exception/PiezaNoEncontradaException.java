/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computadores.Exception;

/**
 *
 * @author gabri_oq3uzky
 */
public class PiezaNoEncontradaException extends Exception {
    public PiezaNoEncontradaException(String message) {
        super(message);
    }

    public PiezaNoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }
}
