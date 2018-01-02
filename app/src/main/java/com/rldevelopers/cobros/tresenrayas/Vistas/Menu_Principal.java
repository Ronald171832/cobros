package com.rldevelopers.cobros.tresenrayas.Vistas;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.rldevelopers.cobros.tresenrayas.Cliente.ClienteListAdapter;
import com.rldevelopers.cobros.tresenrayas.Cliente.ClienteModel;
import com.rldevelopers.cobros.tresenrayas.Cliente.MapsActivity;
import com.rldevelopers.cobros.tresenrayas.Cuenta.Cuenta;
import com.rldevelopers.cobros.tresenrayas.R;
import com.rldevelopers.cobros.tresenrayas.RUTAS.PasoDeParametros;
import com.rldevelopers.cobros.tresenrayas.RUTAS.Rutas;
import com.rldevelopers.cobros.tresenrayas.Trabajador.HistoricoListAdapter;
import com.rldevelopers.cobros.tresenrayas.Trabajador.HistoricoModel;
import com.rldevelopers.cobros.tresenrayas.Trabajador.Login_Trabajador;
import com.rldevelopers.cobros.tresenrayas.Trabajador.MovimientoListAdapter;
import com.rldevelopers.cobros.tresenrayas.Trabajador.MovimientoModel;
import com.rldevelopers.cobros.tresenrayas.Trabajador.Trabajador;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Menu_Principal extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        sharedPreferences = getSharedPreferences("colombianos", MODE_PRIVATE);

    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editar_contra_su, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_editar_contra_su:
                editarContra();
                break;
        }
        return true;
    }

    private void editarContra() {
        final Dialog registrar_gasto = new Dialog(Menu_Principal.this);
        registrar_gasto.setTitle("Editar Contra");
        registrar_gasto.setContentView(R.layout.editar_contra);
        final EditText monto = (EditText) registrar_gasto.findViewById(R.id.et_editar_contra_anterior);
        final EditText descripcion = (EditText) registrar_gasto.findViewById(R.id.et_editar_contra_actual);
        Button boton = (Button) registrar_gasto.findViewById(R.id.bt_editar_contra);
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
                String cod_tra = sharedPreferences.getString("superUsuarioNombre", "");
                String URL = Rutas.SU_EDITAR_CONTRA + mo + "/" + de + "/" + cod_tra;
                RequestQueue queue = Volley.newRequestQueue(Menu_Principal.this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            int respuesta = (int) root.get("confirmacion");
                            if (respuesta == 1) {
                                Toast.makeText(Menu_Principal.this, "Contraseña actualizada Satisfactoriamente", Toast.LENGTH_LONG).show();
                                registrar_gasto.cancel();
                            } else if (respuesta == 0) {
                                Toast.makeText(Menu_Principal.this, "Contraseña Anterior Incorrecta", Toast.LENGTH_LONG).show();
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
        int width = (int) (Menu_Principal.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Menu_Principal.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        registrar_gasto.getWindow().setLayout(width, height);
        registrar_gasto.show();
    }

    List<ClienteModel> listaClientes;
    ClienteListAdapter clienteListAdapter;

    public void verClientes(View view) {
        final ArrayList<String> listItems = new ArrayList<>();
        listItems.add("Ver Cuentas");
        listItems.add("Ver en Mapa");
        new AlertDialog.Builder(Menu_Principal.this)
                .setTitle("Elija una Opción:")
                .setCancelable(false)
                .setAdapter(new ArrayAdapter<String>(Menu_Principal.this, android.R.layout.simple_list_item_1, listItems),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                       /* //Toast.makeText(Cliente.this, pos + "  " + listaClientes.get(pos).getNombre(), Toast.LENGTH_LONG).show();
                                        Toast.makeText(Cliente.this, listItems.get(item), Toast.LENGTH_LONG).show();*/
                                switch (item) {
                                    case 0:
                                        verClientes();
                                        break;
                                    case 1:
                                        verEnMapa();
                                        break;

                                }
                            }
                        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        }).show();
    }

    private void verEnMapa() {
        Intent intent = new Intent(Menu_Principal.this, MapsActivity.class);
        PasoDeParametros.TIPO_DE_CLIENTE = "5";
        startActivity(intent);
    }


    ListView listViewClientes;

    public void verClientes() {
        String URL = Rutas.SU_CLIENTES;
        final Dialog verInforme = new Dialog(Menu_Principal.this);
        verInforme.setTitle("Clientes");
        verInforme.setContentView(R.layout.trabajador_informe_detalle);
        final TextView titulo = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_detalle_titulo);
        listViewClientes = (ListView) verInforme.findViewById(R.id.lv_movimiento);
        titulo.setText("Lista de Clientes");
        listaClientes = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando Clientes...");
        progressDialog.show();
        tocarListViewClientes();
        RequestQueue queue = Volley.newRequestQueue(Menu_Principal.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ClienteModel clienteModel = null;
                    JSONObject root = new JSONObject(response);
                    JSONArray jsonArray = root.getJSONArray("clientes");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            String codigo = (int) jsonArray.getJSONObject(i).get("id") + "";
                            String nombre = (String) jsonArray.getJSONObject(i).get("nombre");
                            String estado = (int) jsonArray.getJSONObject(i).get("conPrestamo") + "";
                            String celular = (int) jsonArray.getJSONObject(i).get("celular") + "";
                            clienteModel = new ClienteModel(codigo, nombre, celular, estado);
                        } catch (JSONException e) {
                            Log.e("Parser JSON", e.toString());
                        }
                        listaClientes.add(clienteModel);
                    }
                    clienteListAdapter = new ClienteListAdapter(Menu_Principal.this, R.layout.clientes_card, listaClientes);
                    listViewClientes.setAdapter(clienteListAdapter);
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

        int width = (int) (Menu_Principal.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Menu_Principal.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        verInforme.getWindow().setLayout(width, height);
        verInforme.show();

    }

    public void tocarListViewClientes() {
        listViewClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Menu_Principal.this, Cuenta.class);
                intent.putExtra("borrarBoton", true);
                PasoDeParametros.CODIGO_DE_CLIENTE = listaClientes.get(i).getCodigo();
                startActivity(intent);
            }
        });
    }


    public void trabajador(View view) {
        startActivity(new Intent(Menu_Principal.this, Trabajador.class));
    }

    public void informeGeneral(View view) {
        final ArrayList<String> listItems = new ArrayList<>();
        listItems.add("Abrir Caja");
        listItems.add("Cerrar Caja");
        listItems.add("Ver Ultimo");
        listItems.add("Historico");
        new AlertDialog.Builder(Menu_Principal.this)
                .setTitle("Elija una Opción:")
                .setCancelable(false)
                .setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listItems),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                switch (item) {
                                    case 0:
                                        abrirCaja();
                                        break;
                                    case 1:
                                        cerrarCaja();
                                        break;
                                    case 2:
                                        String URL = Rutas.SU_INFORME_ULTIMO;
                                        verInforme(URL);
                                        break;
                                    case 3:
                                        verHistorico();
                                        break;
                                }
                            }
                        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        }).show();
    }

    private void cerrarCaja() {
        String URL = Rutas.CERRAR_CAJA;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject root = new JSONObject(response);
                    int respuesta = (int) root.get("confirmacion");
                    if (respuesta == 1) {
                        Toast.makeText(getApplicationContext(), "CAJA CERRADA CORRECTAMENTE!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "YA SE CERRO LA CAJA ANTERIORMENTE!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "El usuario no existe en la base de datos", Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Problemas de conexion verifique su Internet", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    private void abrirCaja() {
        String URL = Rutas.ABRIR_CAJA;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject root = new JSONObject(response);
                    int respuesta = (int) root.get("confirmacion");
                    if (respuesta == 1) {
                        Toast.makeText(getApplicationContext(), "CAJA ABIERTA CORRECTAMENTE!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "YA SE ABRIÓ LA CAJA ANTERIORMENTE!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "El usuario no existe en la base de datos", Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Problemas de conexion verifique su Internet", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    public void iniciarSesionTrabajador(View view) {
        startActivity(new Intent(Menu_Principal.this, Login_Trabajador.class));
    }


    HistoricoListAdapter adapterHistorico;
    ListView listViewHistorico;
    List<HistoricoModel> listaHistoricos;

    private void verHistorico() {
        final ProgressDialog progressDialog;
        final Dialog verInforme = new Dialog(Menu_Principal.this);
        verInforme.setTitle("Historico");
        verInforme.setContentView(R.layout.trabajador_informe_historico);
        listViewHistorico = (ListView) verInforme.findViewById(R.id.lv_historico_trabajador);
        listaHistoricos = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando Historico...");
        progressDialog.show();
        String URL = Rutas.SU_HISTORICO_GENERAL;
        RequestQueue queue = Volley.newRequestQueue(Menu_Principal.this);
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
                            String fechaFin = (String) jsonArray.getJSONObject(i).get("fecha_cierre");
                            String estado = (int) jsonArray.getJSONObject(i).get("estado") + "";
                            historicoModel = new HistoricoModel(fecha, fechaFin, estado, codigo);
                        } catch (JSONException e) {
                            Log.e("Parser JSON", e.toString());
                        }
                        listaHistoricos.add(historicoModel);
                    }
                    adapterHistorico = new HistoricoListAdapter(Menu_Principal.this, R.layout.trabajador_informe_historico_card, listaHistoricos);
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
        int width = (int) (Menu_Principal.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Menu_Principal.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        verInforme.getWindow().setLayout(width, height);
        verInforme.show();

    }

    private void tocarListViewHistoricos() {
        listViewHistorico.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String URL = Rutas.SU_INFORME_ESPECIFICO + listaHistoricos.get(i).getCodigo();
                verInforme(URL);
            }
        });
    }


    String CODIGO_INFORME;
    private List<MovimientoModel> listaDetalles;
    private MovimientoListAdapter adapter;
    private ProgressDialog progressDialog;

    private void verInforme(String URL) {
        final Dialog verInforme = new Dialog(Menu_Principal.this);
        verInforme.setTitle("Informe General");
        verInforme.setContentView(R.layout.trabajador_informe);
        final TextView fechaFin = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_fecha_Fin);

        final TextView fecha = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_fecha);
        final TextView saldo = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_saldo);
        final TextView porCobrar = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_por_cobrar);
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

        RequestQueue queue = Volley.newRequestQueue(Menu_Principal.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    CODIGO_INFORME = (String) root.get("codigo");
                    String estado = (String) root.get("estado");
                    if (estado.equals("1")) {
                        fechaFin.setText(Html.fromHtml("<b>Fecha Cierre: </b>" + "Sin Fecha"));
                    } else {
                        fechaFin.setText(Html.fromHtml("<b>Fecha Cierre: </b>" + (String) root.get("fecha_cierre")));
                    }
                    fecha.setText(Html.fromHtml("<b>Fecha Inicio: </b>" + (String) root.get("fecha")));
                    egreso.setText(Html.fromHtml("<b>Egreso: </b>" + (String) root.get("egresos") + " Bs."));
                    ingreso.setText(Html.fromHtml("<b>Ingreso: </b>" + (String) root.get("ingresos") + " Bs."));
                    saldo.setText((String) root.get("saldo") + " Bs.");
                    porCobrar.setText((String) root.get("porCobrar") + " Bs.");
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
                String URL = Rutas.SU_INFORME_EGRESOS + CODIGO_INFORME;
                verMovimiento(URL, "Egresos", "egresos", 1);
            }
        });
        b_ingreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL = Rutas.SU_INFORME_INGRESOS + CODIGO_INFORME;
                verMovimiento(URL, "Ingresos", "ingresos", 2);
            }
        });
        b_gasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL = Rutas.SU_INFORME_GASTOS + CODIGO_INFORME;
                verMovimiento(URL, "Gastos", "gastos", 3);
            }
        });

        int width = (int) (Menu_Principal.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Menu_Principal.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        verInforme.getWindow().setLayout(width, height);
        verInforme.show();
    }

    private void verMovimiento(String URL, String TITULO, final String NOMBRE_JSONARRAY, final int TIPO_MOVIMIENTO) {
        final Dialog verInforme = new Dialog(Menu_Principal.this);
        verInforme.setTitle("Detalle " + TITULO);
        verInforme.setContentView(R.layout.trabajador_informe_detalle);
        final TextView titulo = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_detalle_titulo);
        final ListView listView = (ListView) verInforme.findViewById(R.id.lv_movimiento);
        titulo.setText("Detalle de " + TITULO);
        listaDetalles = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando " + TITULO + "...");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(Menu_Principal.this);
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
                            String nombre = (String) jsonArray.getJSONObject(i).get("nombre");
                            movimientoModel = new MovimientoModel(fecha, monto, detalle, descripcion, nombre);
                        } catch (JSONException e) {
                            Log.e("Parser JSON", e.toString());
                        }
                        listaDetalles.add(movimientoModel);
                    }
                    adapter = new MovimientoListAdapter(Menu_Principal.this, R.layout.trabajador_informe_detalle_card, listaDetalles, TIPO_MOVIMIENTO);
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

        int width = (int) (Menu_Principal.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Menu_Principal.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        verInforme.getWindow().setLayout(width, height);
        verInforme.show();

    }


    @Override
    public void onBackPressed() {
            AlertDialog.Builder cliente = new AlertDialog.Builder(Menu_Principal.this);
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
            cliente.show();
    }
}
