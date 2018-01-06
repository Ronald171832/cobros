package com.rldevelopers.cobros.tresenrayas.Trabajador;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
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
import com.rldevelopers.cobros.tresenrayas.Cliente.MapsActivity;
import com.rldevelopers.cobros.tresenrayas.R;
import com.rldevelopers.cobros.tresenrayas.RUTAS.PasoDeParametros;
import com.rldevelopers.cobros.tresenrayas.RUTAS.Rutas;
import com.rldevelopers.cobros.tresenrayas.Vistas.Menu_Principal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Trabajador extends AppCompatActivity {
    private List<TrabajadorModel> listaTrabajadores;
    private ListView lv;
    private TrabajadorListAdapter adapterTrabajdor;
    private ProgressDialog progress;

    //filtro  de busqueda
    private List<TrabajadorModel> listaTrabajadoresFiltro;
    SearchView searchView;

    private void getData(String query) {
        List<TrabajadorModel> output = new ArrayList<>();
        List<TrabajadorModel> filteredOutp = new ArrayList<>();
        for (int i = 0; i < listaTrabajadores.size(); i++) {
            output.add(listaTrabajadores.get(i));
        }
        if (searchView != null) {
            for (TrabajadorModel model : output) {
                if (model.getNombre().toLowerCase().startsWith(query.toLowerCase())) {
                    filteredOutp.add(model);
                }
            }
        } else {
            filteredOutp = output;
        }
        adapterTrabajdor = new TrabajadorListAdapter(Trabajador.this, R.layout.trabajadores_card, filteredOutp);
        lv.setAdapter(adapterTrabajdor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trabajador_pantalla_principal);
        init();
        cargarListView();
        tocarListView();
        refrescarPantalla();
    }

    private void refrescarPantalla() {
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh, R.color.refresh1, R.color.refresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        finish();
                        startActivity(getIntent());

                    }
                }, 1000);
            }
        });

    }


    private void tocarListView() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int pos, long l) {
                final ArrayList<String> listItems = new ArrayList<>();
                listItems.add("Ver Informes");
                listItems.add("Ver Ubicacion");
                listItems.add("Abonar");
                listItems.add("Editar Datos");
                if (listaTrabajadores.get(pos).getEstado().equals("1")) {
                    listItems.add("Dar de baja");
                } else {
                    listItems.add("Habilitar");
                }
                new AlertDialog.Builder(Trabajador.this)
                        .setTitle("Elija una Opción:")
                        .setCancelable(false)
                        .setAdapter(new ArrayAdapter<String>(Trabajador.this, android.R.layout.simple_list_item_1, listItems),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        View view1 = view;
                                        TextView codigo = (TextView) view1.findViewById(R.id.cardTrabajador_codigo);
                                        int posicion = retornarPosicion(codigo.getText().toString());
                                       /* //Toast.makeText(Cliente.this, pos + "  " + listaClientes.get(pos).getNombre(), Toast.LENGTH_LONG).show();
                                        Toast.makeText(Cliente.this, listItems.get(item), Toast.LENGTH_LONG).show();*/
                                        switch (item) {
                                            case 0:
                                                //  System.out.println(codigo.getText().toString() + "**************************************************");
                                                verHistorico(posicion);
                                                break;
                                            case 1:
                                                verUbicacion(posicion);
                                                break;
                                            case 2:
                                                abonarDinero(posicion);
                                                break;
                                            case 3:
                                                editarDatos(posicion);
                                                break;
                                            case 4:
                                                darDeAltaBaja(posicion);
                                                break;
                                        }
                                    }
                                }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).show();
                // Toast.makeText(Trabajador.this, pos + "  " + listaTrabajadores.get(pos).getNombre(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private int retornarPosicion(String codigo) {
        for (int i = 0; i < listaTrabajadores.size(); i++) {
            if (codigo.equals(listaTrabajadores.get(i).getCodigo())) {
                return i;
            }
        }
        return 0;
    }

    private void darDeAltaBaja(int pos) {
        String URL = Rutas.DAR_BAJA_ALTA + listaTrabajadores.get(pos).getCodigo();
        RequestQueue queue = Volley.newRequestQueue(Trabajador.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    int respuesta = (int) root.get("confirmacion");
                    if (respuesta == 1) {
                        startActivity(new Intent(Trabajador.this, Trabajador.class));
                        Toast.makeText(Trabajador.this, "Trabajador actualizado Satisfactoriamente", Toast.LENGTH_LONG).show();
                    } else if (respuesta == 0) {
                        Toast.makeText(Trabajador.this, "Primero debe dar cierre!!", Toast.LENGTH_LONG).show();
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

    private void editarDatos(final int pos) {
        final Dialog editarDatosCliente = new Dialog(Trabajador.this);
        editarDatosCliente.setTitle("Editar Datos");
        editarDatosCliente.setContentView(R.layout.cliente_editar);
        final EditText nombre = (EditText) editarDatosCliente.findViewById(R.id.et_cliente_editar_nombre);
        final EditText celular = (EditText) editarDatosCliente.findViewById(R.id.et_cliente_editar_celular);
        Button boton = (Button) editarDatosCliente.findViewById(R.id.bt_cliente_editar);
        nombre.setText(listaTrabajadores.get(pos).getNombre());
        celular.setText(listaTrabajadores.get(pos).getCelular());
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String no = nombre.getText().toString().trim();
                String ce = celular.getText().toString().trim();
                if (no.isEmpty()) {
                    nombre.setError("Debe llenar este campo...");
                    nombre.requestFocus();
                    return;
                } else if (ce.isEmpty()) {
                    celular.setError("Debe llenar este campo...");
                    celular.requestFocus();
                    return;
                }

                String URL = Rutas.EDITAR_TRABAJADOR + listaTrabajadores.get(pos).getCodigo() + "/" + no + "/" + ce;
                RequestQueue queue = Volley.newRequestQueue(Trabajador.this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            int respuesta = (int) root.get("confirmacion");
                            if (respuesta == 1) {
                                startActivity(new Intent(Trabajador.this, Trabajador.class));
                                Toast.makeText(Trabajador.this, "Trabajdor actualizado Satisfactoriamente", Toast.LENGTH_LONG).show();
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
        int width = (int) (Trabajador.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Trabajador.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        editarDatosCliente.getWindow().setLayout(width, height);
        editarDatosCliente.show();
    }

    //pagar dinero
    boolean abonarDinero = true;

    private void abonarDinero(final int pos) {
        final Dialog registrar_abono = new Dialog(Trabajador.this);
        registrar_abono.setTitle("Abobar Dinero");
        registrar_abono.setContentView(R.layout.trabajador_registrar_abono);
        final EditText monto = (EditText) registrar_abono.findViewById(R.id.et_trabajador_abonar);
        Button boton = (Button) registrar_abono.findViewById(R.id.bt_trabajador_abonar);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mo = monto.getText().toString().trim();
                if (mo.isEmpty()) {
                    monto.setError("Debe llenar este campo...");
                    monto.requestFocus();
                    return;
                }
                String cod_tra = listaTrabajadores.get(pos).getCodigo();
                String URL = Rutas.ABONAR_TRABAJADOR + mo + "/" + cod_tra;
                if (abonarDinero) {
                    abonarDinero = !abonarDinero;
                    RequestQueue queue = Volley.newRequestQueue(Trabajador.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject root = new JSONObject(response);
                                int respuesta = (int) root.get("confirmacion");
                                if (respuesta == 1) {
                                    Toast.makeText(Trabajador.this, "Abono registrado Satisfactoriamente", Toast.LENGTH_LONG).show();
                                    abonarDinero = !abonarDinero;
                                    registrar_abono.cancel();
                                } else if (respuesta == 0) {
                                    Toast.makeText(Trabajador.this, "No abrio informe!", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            abonarDinero = !abonarDinero;
                            Toast.makeText(getApplicationContext(), "Compruebe su conexion a Internet!", Toast.LENGTH_LONG).show();
                        }
                    });
                    queue.add(stringRequest);
                } else {
                    Toast.makeText(Trabajador.this, "Operacion en curso espere por favor!", Toast.LENGTH_LONG).show();
                }
            }
        });
        int width = (int) (Trabajador.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Trabajador.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        registrar_abono.getWindow().setLayout(width, height);
        registrar_abono.show();
    }

    private void verUbicacion(int pos) {
        Intent intent = new Intent(Trabajador.this, MapsActivity.class);
        intent.putExtra("la", listaTrabajadores.get(pos).getLatitud());
        intent.putExtra("lo", listaTrabajadores.get(pos).getLongitud());
        intent.putExtra("no", listaTrabajadores.get(pos).getNombre());
        intent.putExtra("es", listaTrabajadores.get(pos).getEstado() + "");
        PasoDeParametros.TIPO_DE_CLIENTE = "4";
        startActivity(intent);
    }

    private void cargarListView() {
        String URL = Rutas.LISTA_TRABAJADORES;
        RequestQueue queue = Volley.newRequestQueue(Trabajador.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    TrabajadorModel trabajadorModel = null;
                    ArrayList<String> lista = new ArrayList<>();
                    JSONObject root = new JSONObject(response);
                    JSONArray jsonArray = root.getJSONArray("trabajadores");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            String codigo = (int) jsonArray.getJSONObject(i).get("id") + "";
                            String nombre = (String) jsonArray.getJSONObject(i).get("nombre");
                            String celular = (int) jsonArray.getJSONObject(i).get("celular") + "";
                            String latitud = (String) jsonArray.getJSONObject(i).get("latitud");
                            String longitud = (String) jsonArray.getJSONObject(i).get("longitud");
                            String contra = (String) jsonArray.getJSONObject(i).get("password");
                            String estado = (int) jsonArray.getJSONObject(i).get("habilitado") + "";
                            trabajadorModel = new TrabajadorModel(codigo, nombre, celular, latitud, longitud, contra, estado);
                        } catch (JSONException e) {
                            Log.e("Parser JSON", e.toString());
                        }
                        listaTrabajadores.add(trabajadorModel);
                    }
                    adapterTrabajdor = new TrabajadorListAdapter(Trabajador.this, R.layout.trabajadores_card, listaTrabajadores);
                    lv.setAdapter(adapterTrabajdor);
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }

    private void init() {
        listaTrabajadores = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lv_trabajador);
        progress = new ProgressDialog(this);
        progress.setMessage("Cargando Trabajadores...");
        progress.show();

    }

    private SearchManager searchManager;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_trabajador, menu);
        // MenuItem search = menu.findItem(R.id.search_trabajador);
        MenuItem searchItem = (MenuItem) menu.findItem(R.id.search_trabajador);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

//        searchView.setQueryHint(getString(R.string.search));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getData(newText);
                return false;
            }
        });


    /*    searchView.setOnCloseListener((SearchView.OnCloseListener) this);
        searchView.requestFocus();*/


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add_trabajador:
                agregarTrabajador();
                break;
        }
        return true;
    }


    //valores para fragment agrefar trabajador
    EditText codigo;
    EditText nombre;
    EditText celular;
    EditText contra;
    boolean agregarTrabajado = true;

    private void agregarTrabajador() {
        final Dialog agregarTrabajador = new Dialog(Trabajador.this);
        agregarTrabajador.setTitle("Agregar Trabajador");
        agregarTrabajador.setContentView(R.layout.trabajador_agregar);
        codigo = (EditText) agregarTrabajador.findViewById(R.id.et_trabajador_id);
        nombre = (EditText) agregarTrabajador.findViewById(R.id.et_trabajador_nombre);
        celular = (EditText) agregarTrabajador.findViewById(R.id.et_trabajador_celular);
        contra = (EditText) agregarTrabajador.findViewById(R.id.et_trabajador_contra);
        agregarCodigo();
        Button boton = (Button) agregarTrabajador.findViewById(R.id.bt_trabajador_agregar);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validar()) {
                    if (agregarTrabajado) {
                        agregarTrabajado = !agregarTrabajado;
                        agregarTrabajadorValidado();
                        agregarTrabajador.cancel();
                    } else {
                        Toast.makeText(Trabajador.this, "Operacion en curso espere por favor!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        int width = (int) (Trabajador.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Trabajador.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        agregarTrabajador.getWindow().setLayout(width, height);
        agregarTrabajador.show();
    }

    private void agregarTrabajadorValidado() {
        String URL = Rutas.AGREGAR_TRABAJADOR + nombre.getText().toString().trim() +
                "/" + celular.getText().toString().trim() + "/" + contra.getText().toString().trim();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    int respuesta = (int) root.get("confirmacion");
                    if (respuesta == 1) {
                        startActivity(new Intent(Trabajador.this, Trabajador.class));
                        Toast.makeText(Trabajador.this, "Trabajador agregado Satisfactoriamente", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                agregarTrabajado = !agregarTrabajado;
                Toast.makeText(getApplicationContext(), "Compruebe su conexion a Internet!", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    private void agregarCodigo() {
        String URL = Rutas.ID_TRABAJADOR;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    int respuesta = (int) root.get("codigo");
                    codigo.setText(respuesta + "");
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

    private boolean validar() {
        String nom = nombre.getText().toString().trim();
        String con = contra.getText().toString().trim();
        String cel = celular.getText().toString().trim();

        if (nom.isEmpty()) {
            nombre.setError("Debe llenar este campo...");
            nombre.requestFocus();
            return false;
        } else if (cel.isEmpty()) {
            celular.setError("Debe llenar este campo...");
            celular.requestFocus();
            return false;
        } else if (con.isEmpty()) {
            contra.setError("El nombre no es valido...");
            contra.requestFocus();
            return false;
        }
        return true;
    }


    HistoricoListAdapter adapterHistorico;
    ListView listViewHistorico;
    List<HistoricoModel> listaHistoricos;

    private void verHistorico(int pos) {
        final ProgressDialog progressDialog;
        final Dialog verInforme = new Dialog(Trabajador.this);
        verInforme.setTitle("Historico");
        verInforme.setContentView(R.layout.trabajador_informe_historico);
        listViewHistorico = (ListView) verInforme.findViewById(R.id.lv_historico_trabajador);
        listaHistoricos = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando Historico...");
        progressDialog.show();
        String cod_tra = listaTrabajadores.get(pos).getCodigo();
        String URL = Rutas.INFORME_HISTORICO + cod_tra;
        RequestQueue queue = Volley.newRequestQueue(Trabajador.this);
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
                    adapterHistorico = new HistoricoListAdapter(Trabajador.this, R.layout.trabajador_informe_historico_card, listaHistoricos);
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
        int width = (int) (Trabajador.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Trabajador.this.getResources().getDisplayMetrics().heightPixels * 0.9);
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
        final Dialog verInforme = new Dialog(Trabajador.this);
        verInforme.setTitle("Informe General");
        verInforme.setContentView(R.layout.trabajador_informe);
        final TextView fecha = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_fecha);
        final TextView fechaFin = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_fecha_Fin);
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

        RequestQueue queue = Volley.newRequestQueue(Trabajador.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    CODIGO_INFORME = (String) root.get("codigo");
                    fecha.setText(Html.fromHtml("<b>Fecha Inicio: </b>" + (String) root.get("fecha")));
                    String estado = (String) root.get("estado");
                    if (estado.equals("1")) {
                        fechaFin.setText(Html.fromHtml("<b>Fecha Cierre: </b>" + "Sin Fecha"));
                    } else {
                        fechaFin.setText(Html.fromHtml("<b>Fecha Cierre: </b>" + (String) root.get("fecha_cierre")));
                    }
                    porCobrar.setText((String) root.get("porCobrar") + " Bs.");
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

        int width = (int) (Trabajador.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Trabajador.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        verInforme.getWindow().setLayout(width, height);
        verInforme.show();
    }

    private void verMovimiento(String URL, String TITULO, final String NOMBRE_JSONARRAY, final int TIPO_MOVIMIENTO) {
        final Dialog verInforme = new Dialog(Trabajador.this);
        verInforme.setTitle("Detalle " + TITULO);
        verInforme.setContentView(R.layout.trabajador_informe_detalle);
        final TextView titulo = (TextView) verInforme.findViewById(R.id.tv_informe_trabajador_detalle_titulo);
        final ListView listView = (ListView) verInforme.findViewById(R.id.lv_movimiento);
        titulo.setText("Detalle de " + TITULO);
        listaDetalles = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando " + TITULO + "...");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(Trabajador.this);
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
                    adapter = new MovimientoListAdapter(Trabajador.this, R.layout.trabajador_informe_detalle_card, listaDetalles, TIPO_MOVIMIENTO);
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

        int width = (int) (Trabajador.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Trabajador.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        verInforme.getWindow().setLayout(width, height);
        verInforme.show();

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(Trabajador.this, Menu_Principal.class));
    }
}
