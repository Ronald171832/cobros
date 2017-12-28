package com.rldevelopers.cobros.tresenrayas.RUTAS;

/**
 * Created by Ronald Lopez on 25/12/2017.
 */

public class Rutas {

    public final static String IP = "http://192.168.0.104";
    public final static String PUERTO = ":8080";
    public final static String CARPETA = "/cobros/public/";

    /*public final static String IP = "http://manueldeveloper.xyz";
    public final static String PUERTO = "";
    public final static String CARPETA = "/r/cobros01/public/";
*/

    public final static String LOGIN = IP + PUERTO + CARPETA + "iniciar/";
    public final static String ABONAR_TRABAJADOR = IP + PUERTO + CARPETA + "movimiento/abono/";



    public final static String LOGIN_TRABAJADOR = IP + PUERTO + CARPETA + "iniciarTrabajador/";
    public final static String LISTA_TRABAJADORES = IP + PUERTO + CARPETA + "trabajadores/mostrar";
    public final static String AGREGAR_TRABAJADOR = IP + PUERTO + CARPETA + "agregarTrabajador/";
    public final static String ID_TRABAJADOR = IP + PUERTO + CARPETA + "nuevoTrabajador";
    public final static String REGISTRAR_GASTO = IP + PUERTO + CARPETA + "movimiento/gasto/";
    public final static String ULTIMO_INFORME = IP + PUERTO + CARPETA + "balance/verUltimo/";
    public final static String INFORME_INGRESOS = IP + PUERTO + CARPETA + "balance/verIngresos/";
    public final static String INFORME_EGRESOS = IP + PUERTO + CARPETA + "balance/verEgresos/";
    public final static String INFORME_GASTOS = IP + PUERTO + CARPETA + "balance/verGastos/";
    public final static String INFORME_HISTORICO = IP + PUERTO + CARPETA + "balance/verHistorico/";
    public final static String INFORME_HISTORICO_ESPECIFICO = IP + PUERTO + CARPETA + "balance/ver/";



    public final static String LISTA_CLIENTES_TODOS = IP + PUERTO + CARPETA + "clientes/mostrar/todos/";
    public final static String LISTA_CLIENTES_PENDIENTES = IP + PUERTO + CARPETA + "clientes/mostrar/pendientes/";
    public final static String AGREGAR_CLIENTE = IP + PUERTO + CARPETA + "agregarCliente/";
    public final static String EDITAR_CLIENTE = IP + PUERTO + CARPETA + "clientes/actualizar/";
    public final static String CUENTAS_CLIENTE = IP + PUERTO + CARPETA + "clientes/mostrar/cuentas/";
    public final static String NO_PAGO_CLIENTE = IP + PUERTO + CARPETA + "observacion/nueva/";


    public final static String CREAR_CREDITO = IP + PUERTO + CARPETA + "cuentas/nuevoCredito/";
    public final static String DETALLE_CREDITO = IP + PUERTO + CARPETA + "cuentas/ver/";
    public final static String PAGO_ABONO = IP + PUERTO + CARPETA + "abono/nuevoAbono/";
    public final static String LISTAR_ABONOS = IP + PUERTO + CARPETA + "abono/ver/";


}
