package com.rldevelopers.cobros.tresenrayas.Trabajador;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.rldevelopers.cobros.tresenrayas.Vistas.Menu_Principal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
                String cod_tra = sharedPreferences.getString("codigo_trabajador", "");
                String URL = Rutas.ULTIMO_INFORME + cod_tra;
                verInforme(URL);
            }
        });
        informe.setNegativeButton("Ver Historico", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                verHistorico();
            }
        });
        informe.setNeutralButton("Registrar Gasto", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                registrarGasto();
            }
        });
        informe.show();
    }

    HistoricoListAdapter adapterHistorico;
    ListView listViewHistorico;
    List<HistoricoModel> listaHistoricos;

    private void verHistorico() {
        final ProgressDialog progressDialog;
        final Dialog verInforme = new Dialog(Menu_Trabajador.this);
        verInforme.setTitle("Historico");
        verInforme.setContentView(R.layout.trabajador_informe_historico);
        listViewHistorico = (ListView) verInforme.findViewById(R.id.lv_historico_trabajador);
        listaHistoricos = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando Historico...");
        progressDialog.show();
        String cod_tra = sharedPreferences.getString("codigo_trabajador", "");
        String URL = Rutas.INFORME_HISTORICO + cod_tra;
        RequestQueue queue = Volley.newRequestQueue(Menu_Trabajador.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    HistoricoModel historicoModel = null;
                    JSONObject root = new JSONObject(response);
                    JSONArray jsonArray = root.getJSONArray("informes");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            String codigo = (int) jsonArray.getJSONObject(i).get("id") + "";
                            String fecha = (String) jsonArray.getJSONObject(i).get("fecha");
                            String estado = (int) jsonArray.getJSONObject(i).get("estado") + "";
                            historicoModel = new HistoricoModel(fecha, estado, codigo);
                        } catch (JSONException e) {
                            Log.e("Parser JSON", e.toString());
                        }
                        listaHistoricos.add(historicoModel);
                    }
                    adapterHistorico = new HistoricoListAdapter(Menu_Trabajador.this, R.layout.trabajador_informe_historico_card, listaHistoricos);
                    listViewHistorico.setAdapter(adapterHistorico);
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    progressDialog.cancel();
                    Toast.makeText(getApplicationContext(), "Sin Historico Registrado!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(getApplicationContext(), "Compruebe su conexion a Internet!", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
        tocarListViewHistoricos();
        int width = (int) (Menu_Trabajador.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Menu_Trabajador.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        verInforme.getWindow().setLayout(width, height);
        verInforme.show();

    }

    private void tocarListViewHistoricos() {
        listViewHistorico.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String URL = Rutas.INFORME_HISTORICO_ESPECIFICO + listaHistoricos.get(i).getCodigo();
                verInforme(URL);
            }
        });
    }


    String CODIGO_INFORME;
    private List<MovimientoModel> listaDetalles;
    private MovimientoListAdapter adapter;
    private ProgressDialog progressDialog;

    private void verInforme(String URL) {
        final Dialog verInforme = new Dialog(Menu_Trabajador.this);
        verInforme.setTitle("Informe General");
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

        RequestQueue queue = Volley.newRequestQueue(Menu_Trabajador.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    CODIGO_INFORME = (String) root.get("codigo");
                    fecha.setText(Html.fromHtml("<b>Fecha: </b>" + (String) root.get("fecha")));
                    egreso.setText(Html.fromHtml("<b>Egreso: </b>" + (String) root.get("egresos") + " Bs."));
                    ingreso.setText(Html.fromHtml("<b>Ingreso: </b>" + (String) root.get("ingresos") + " Bs."));
                    saldo.setText((String) root.get("saldo") + " Bs.");
                    cargado.setText(Html.fromHtml("<b>Cargado: </b>" + (String) root.get("cargado") + " Bs."));
                    prestado.setText(Html.fromHtml("<b>Prestado: </b>" + (String) root.get("prestado") + " Bs."));
                    gastado.setText(Html.fromHtml("<b>Gastado: </b>" + (String) root.get("gastado") + " Bs."));
                    cobrado.setText(Html.fromHtml("<b>Cobrado: </b>" + (String) root.get("cobrado") + " Bs."));
                } catch (JSONException e) {
                    verInforme.cancel();
                    Toast.makeText(getApplicationContext(), "No hay Informe en curso!", Toast.LENGTH_LONG).show();
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
        b_egreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL = Rutas.INFORME_EGRESOS + CODIGO_INFORME;
                verMovimiento(URL, "Egresos", "egresos", 1);
            }
        });
        b_ingreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL = Rutas.INFORME_INGRESOS + CODIGO_INFORME;
                verMovimiento(URL, "Ingresos", "ingresos", 2);
            }
        });
        b_gasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL = Rutas.INFORME_GASTOS + CODIGO_INFORME;
                verMovimiento(URL, "Gastos", "gastos", 3);
            }
        });

        int width = (int) (Menu_Trabajador.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Menu_Trabajador.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        verInforme.getWindow().setLayout(width, height);
        verInforme.show();
    }

    private void verMovimiento(String URL, String TITULO, final String NOMBRE_JSONARRAY, final int TIPO_MOVIMIENTO) {
        final Dialog verInforme = new Dialog(Menu_Trabajador.this);
        verInforme.setTitle("Detalle " + TITULO);
        verInforme.setContentView(R.layout.trabajador_informe_detalle);
        final TextView titulo = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_detalle_titulo);
        final ListView listView = (ListView) verInforme.findViewById(R.id.lv_movimiento);
        titulo.setText("Detalle de " + TITULO);
        listaDetalles = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando " + TITULO + "...");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(Menu_Trabajador.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    MovimientoModel movimientoModel = null;
                    JSONObject root = new JSONObject(response);
                    JSONArray jsonArray = root.getJSONArray(NOMBRE_JSONARRAY);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            String fecha = (String) jsonArray.getJSONObject(i).get("fecha");
                            String monto = (int) jsonArray.getJSONObject(i).get("monto") + " Bs.";
                            String detalle = (String) jsonArray.getJSONObject(i).get("detalle");
                            String descripcion = (String) jsonArray.getJSONObject(i).get("descripcion");
                            movimientoModel = new MovimientoModel(fecha, monto, detalle, descripcion);
                        } catch (JSONException e) {
                            Log.e("Parser JSON", e.toString());
                        }
                        listaDetalles.add(movimientoModel);
                    }
                    adapter = new MovimientoListAdapter(Menu_Trabajador.this, R.layout.trabajador_informe_detalle_card, listaDetalles, TIPO_MOVIMIENTO);
                    listView.setAdapter(adapter);
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    progressDialog.cancel();
                    Toast.makeText(getApplicationContext(), "No hay Movimientos Registrados!", Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        //TRABAJADOR
        /*AlertDialog.Builder cliente = new AlertDialog.Builder(Menu.this);
        cliente.setTitle("Salir");
        cliente.setMessage("Elija una opcion:");
        cliente.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        cliente.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//
            }
        });
        cliente.show();*/
        //SUPER USUARIO
        startActivity(new Intent(this, Menu_Principal.class));
    }
}
