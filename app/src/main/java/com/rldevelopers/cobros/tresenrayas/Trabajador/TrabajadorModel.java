package com.rldevelopers.cobros.tresenrayas.Trabajador;

/**
 * Created by Ronald Lopez on 02/11/2017.
 */

public class TrabajadorModel {

    public String codigo;
    public String nombre;
    public String celular;
    public String latitud;
    public String longitud;
    public String contra;
    public String estado;

    public TrabajadorModel() {
    }

    public TrabajadorModel(String codigo, String nombre, String celular, String latitud, String longitud, String contra, String estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.celular = celular;
        this.latitud = latitud;
        this.longitud = longitud;
        this.contra = contra;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
