package com.rldevelopers.cobros.tresenrayas.Trabajador;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rldevelopers.cobros.tresenrayas.Cliente.AgregarCliente;
import com.rldevelopers.cobros.tresenrayas.Cliente.Cliente;
import com.rldevelopers.cobros.tresenrayas.R;
import com.rldevelopers.cobros.tresenrayas.RUTAS.PasoDeParametros;
import com.rldevelopers.cobros.tresenrayas.RUTAS.Rutas;

import org.json.JSONException;
import org.json.JSONObject;

public class Menu_Trabajador extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trabajador_menu);
        sharedPreferences = getSharedPreferences("colombianos", MODE_PRIVATE);

    }

    public void clienteTrabajador(View view) {

        AlertDialog.Builder cliente = new AlertDialog.Builder(Menu_Trabajador.this);
        cliente.setTitle("Cliente");
        cliente.setMessage("Elija una opcion:");
        cliente.setPositiveButton("Clientes Pendientes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Menu_Trabajador.this, Cliente.class);
                //intent.putExtra("tipo","2");
                PasoDeParametros.TIPO_DE_CLIENTE = "2";
                startActivity(intent);

            }
        });
        cliente.setNegativeButton("Todos los Clientes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Menu_Trabajador.this, Cliente.class);
                //intent.putExtra("tipo","1");
                PasoDeParametros.TIPO_DE_CLIENTE = "1";
                startActivity(intent);

            }
        });
        cliente.setNeutralButton("Crear Cliente", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Menu_Trabajador.this, AgregarCliente.class));

            }
        });
        cliente.show();
    }

    public void informeTrabajador(View view) {
        AlertDialog.Builder informe = new AlertDialog.Builder(Menu_Trabajador.this);
        informe.setTitle("Informe");
        informe.setMessage("Elija una opcion:");
        informe.setPositiveButton("Ver Informe", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                verInforme();
            }
        });
        informe.setNegativeButton("Ver Historico", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        informe.setNeutralButton("Registrar Gasto", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                registrarGasto();
            }
        });
        informe.show();
    }

    String CODIGO_INFORME;

    private void verInforme() {
        final Dialog verInforme = new Dialog(Menu_Trabajador.this);
        verInforme.setTitle("Agregar Credito");
        verInforme.setContentView(R.layout.trabajador_informe);
        final TextView fecha = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_fecha);
        final TextView saldo = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_saldo);
        final TextView ingreso = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_ingreso);
        final TextView egreso = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_egreso);
        final TextView cargado = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_cargado);
        final TextView prestado = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_prestado);
        final TextView cobrado = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_cobrado);
        final TextView gastado = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_gastos);
        final Button b_egreso = (Button) verInforme.findViewById(R.id.bt_informe_trabajador_egreso);
        final Button b_ingreso = (Button) verInforme.findViewById(R.id.bt_informe_trabajador_ingreso);
        final Button b_gasto = (Button) verInforme.findViewById(R.id.bt_informe_trabajador_gastos);
        CODIGO_INFORME = "";
        String cod_tra = sharedPreferences.getString("codigo_trabajador", "");
        String URL = Rutas.ULTIMO_INFORME + cod_tra;
        RequestQueue queue = Volley.newRequestQueue(Menu_Trabajador.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    CODIGO_INFORME = (String) root.get("codigo");
                    fecha.setText(Html.fromHtml("<b>Fecha: </b>" + (String) root.get("fecha")));
                    egreso.setText(Html.fromHtml("<b>Egreso: </b>" + (String) root.get("egresos")+ " Bs."));
                    ingreso.setText(Html.fromHtml("<b>Ingreso: </b>" + (String) root.get("ingresos")+ " Bs."));
                    saldo.setText((String) root.get("saldo") + " Bs.");
                    cargado.setText(Html.fromHtml("<b>Cargado: </b>" + (String) root.get("cargado")+ " Bs."));
                    prestado.setText(Html.fromHtml("<b>Prestado: </b>" + (String) root.get("prestado")+ " Bs."));
                    gastado.setText(Html.fromHtml("<b>Gastado: </b>" + (String) root.get("gastado")+ " Bs."));
                    cobrado.setText(Html.fromHtml("<b>Cobrado: </b>" + (String) root.get("cobrado")+ " Bs."));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Compruebe su conexion a Internet!", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);

        int width = (int) (Menu_Trabajador.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Menu_Trabajador.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        verInforme.getWindow().setLayout(width, height);
        verInforme.show();
    }

    private void registrarGasto() {
        final Dialog registrar_gasto = new Dialog(Menu_Trabajador.this);
        registrar_gasto.setTitle("Agregar Credito");
        registrar_gasto.setContentView(R.layout.trabajador_registrar_gasto);
        final EditText monto = (EditText) registrar_gasto.findViewById(R.id.et_trabajador_gasto_monto);
        final EditText descripcion = (EditText) registrar_gasto.findViewById(R.id.et_trabajador_gasto_descripcion);
        Button boton = (Button) registrar_gasto.findViewById(R.id.bt_trabajador_gasto);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mo = monto.getText().toString().trim();
                String de = descripcion.getText().toString().trim();
                if (mo.isEmpty()) {
                    monto.setError("Debe llenar este campo...");
                    monto.requestFocus();
                    return;
                } else if (de.isEmpty()) {
                    descripcion.setError("Debe llenar este campo...");
                    descripcion.requestFocus();
                    return;
                }
                String cod_tra = sharedPreferences.getString("codigo_trabajador", "");
                String URL = Rutas.REGISTRAR_GASTO + mo + "/" + de + "/" + cod_tra;
                RequestQueue queue = Volley.newRequestQueue(Menu_Trabajador.this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            int respuesta = (int) root.get("confirmacion");
                            if (respuesta == 1) {
                                Toast.makeText(Menu_Trabajador.this, "Gasto registrado Satisfactoriamente", Toast.LENGTH_LONG).show();
                                registrar_gasto.cancel();
                            } else if (respuesta == 0) {
                                Toast.makeText(Menu_Trabajador.this, "No tiene dinero suficiente!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Compruebe su conexion a Internet!", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(stringRequest);
            }
        });
        int width = (int) (Menu_Trabajador.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Menu_Trabajador.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        registrar_gasto.getWindow().setLayout(width, height);
        registrar_gasto.show();
    }

    public void cerrarSesionTrabajador(View view) {
        sharedPreferences.edit().putBoolean("trabajador", false).apply();
        startActivity(new Intent(this, Login_Trabajador.class));
    }
}
