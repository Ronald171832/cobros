package com.rldevelopers.cobros.tresenrayas.Trabajador;

/**
 * Created by Ronald Lopez on 02/11/2017.
 */

public class HistoricoModel {

    public String fecha;
    public String estado;

    public HistoricoModel(String fecha, String estado, String codigo) {
        this.fecha = fecha;
        this.estado = estado;
        this.codigo = codigo;
    }

    public String codigo;

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
