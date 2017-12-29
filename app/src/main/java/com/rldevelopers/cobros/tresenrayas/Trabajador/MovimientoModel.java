package com.rldevelopers.cobros.tresenrayas.Trabajador;

/**
 * Created by Ronald Lopez on 02/11/2017.
 */

public class MovimientoModel {

    public String fecha;
    public String monto;
    public String detalle;
    public String descripcion;
    public String nombreTrabajador;

    public String getNombreTrabajador() {
        return nombreTrabajador;
    }

    public void setNombreTrabajador(String nombreTrabajador) {
        this.nombreTrabajador = nombreTrabajador;
    }

    public MovimientoModel(String fecha, String monto, String detalle, String descripcion, String nombreTrabajador) {
        this.fecha = fecha;
        this.monto = monto;
        this.detalle = detalle;
        this.descripcion = descripcion;
        this.nombreTrabajador = nombreTrabajador;
    }

    public MovimientoModel() {
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


}
