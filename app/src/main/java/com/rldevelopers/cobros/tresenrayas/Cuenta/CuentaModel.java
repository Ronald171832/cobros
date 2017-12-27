package com.rldevelopers.cobros.tresenrayas.Cuenta;

/**
 * Created by Ronald Lopez on 02/11/2017.
 */

public class CuentaModel {

    public String codigo;
    public String trabajador;
    public String monto;
    public String fecha;
    public String estado;

    public CuentaModel() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(String trabajador) {
        this.trabajador = trabajador;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public CuentaModel(String codigo, String trabajador, String monto, String fecha, String estado) {

        this.codigo = codigo;
        this.trabajador = trabajador;
        this.monto = monto;
        this.fecha = fecha;
        this.estado = estado;
    }
}
