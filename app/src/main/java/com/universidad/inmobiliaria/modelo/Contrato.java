package com.universidad.inmobiliaria.modelo;

import java.io.Serializable;

public class Contrato implements Serializable {

    private int idContrato;
    private String fechaInicio;
    private String fechaFinalizacion;
    private double montoAlquiler;
    private int idInmueble;
    private Inmueble inmueble;
    private int idInquilino;
    private Inquilino inquilino;

    public Contrato() {
    }

    public Contrato(int idContrato, String fechaInicio, String fechaFin, double montoAlquiler, int idInmueble, Inmueble inmueble, int idInquilino, Inquilino inquilino) {
        this.idContrato = idContrato;
        this.fechaInicio = fechaInicio;
        this.fechaFinalizacion = fechaFinalizacion;
        this.montoAlquiler = montoAlquiler;
        this.idInmueble = idInmueble;
        this.inmueble = inmueble;
        this.idInquilino = idInquilino;
        this.inquilino = inquilino;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(String fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }
    public double getMontoAlquiler() {
        return montoAlquiler;
    }

    public void setMontoAlquiler(double montoAlquiler) {
        this.montoAlquiler = montoAlquiler;
    }

    public int getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(int idInmueble) {
        this.idInmueble = idInmueble;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public int getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(int idInquilino) {
        this.idInquilino = idInquilino;
    }

    public Inquilino getInquilino() {
        return inquilino;
    }

    public void setInquilino(Inquilino inquilino) {
        this.inquilino = inquilino;
    }
}