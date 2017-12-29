package com.rldevelopers.cobros.tresenrayas.Trabajador;

/**
 * Created by Ronald Lopez on 02/11/2017.
 */

public class HistoricoModel {

    public String fecha;
    public String fechaFin;
    public String estado;
    public String codigo;

    public HistoricoModel(String fecha, String fechaFin, String estado, String codigo) {
        this.fecha = fecha;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.codigo = codigo;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public HistoricoModel() {
    }

    public String getFecha() {
        return fecha;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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


}
