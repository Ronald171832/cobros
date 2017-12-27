package com.rldevelopers.cobros.tresenrayas.Cliente;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rldevelopers.cobros.tresenrayas.Cuenta.Cuenta;
import com.rldevelopers.cobros.tresenrayas.R;
import com.rldevelopers.cobros.tresenrayas.RUTAS.PasoDeParametros;
import com.rldevelopers.cobros.tresenrayas.RUTAS.Rutas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends AppCompatActivity {
    private List<ClienteModel> listaClientes;
    private ListView lv;
    private ClienteListAdapter adapter;
    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    String TIPO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cliente_principal);
        init();
        TIPO = PasoDeParametros.TIPO_DE_CLIENTE;
        if (TIPO.equals("1")) {
            cargarListViewTodos();

        } else if (TIPO.equals("2")) {
            cargarListViewPendiente();
        }
        tocarListView();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cliente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_mapa:
                Intent intent = new Intent(Cliente.this, MapsActivity.class);
                //intent.putExtra("tipo", TIPO);
                startActivity(intent);
                break;
        }
        return true;
    }


    private void tocarListView() {


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int pos, long l) {

                final ArrayList<String> listItems = new ArrayList<>();
                listItems.add("Ver Cuentas");
                listItems.add("No Pago!");
                listItems.add("Ver en Mapa");
                listItems.add("Editar Datos");
                new AlertDialog.Builder(Cliente.this)
                        .setTitle("Elija una Opción:")
                        .setCancelable(false)
                        .setAdapter(new ArrayAdapter<String>(Cliente.this, android.R.layout.simple_list_item_1, listItems),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                       /* //Toast.makeText(Cliente.this, pos + "  " + listaClientes.get(pos).getNombre(), Toast.LENGTH_LONG).show();
                                        Toast.makeText(Cliente.this, listItems.get(item), Toast.LENGTH_LONG).show();*/
                                        switch (item) {
                                            case 0:
                                                verCuentas(pos);
                                                break;
                                            case 1:
                                                break;
                                            case 2:
                                                verEnMapa(pos);
                                                break;
                                            case 3:
                                                editarDatos(pos);
                                                break;
                                        }
                                    }
                                }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).show();

            }
        });
    }

    private void editarDatos(final int pos) {
        final Dialog editarDatosCliente = new Dialog(Cliente.this);
        editarDatosCliente.setTitle("Agregar Credito");
        editarDatosCliente.setContentView(R.layout.cliente_editar);
        final EditText nombre = (EditText) editarDatosCliente.findViewById(R.id.et_cliente_editar_nombre);
        final EditText celular = (EditText) editarDatosCliente.findViewById(R.id.et_cliente_editar_celular);
        Button boton = (Button) editarDatosCliente.findViewById(R.id.bt_cliente_editar);
        nombre.setText(listaClientes.get(pos).getNombre());
        celular.setText(listaClientes.get(pos).getCelular());
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

                String URL = Rutas.EDITAR_CLIENTE + listaClientes.get(pos).getCodigo() + "/" + no + "/" + ce;
                RequestQueue queue = Volley.newRequestQueue(Cliente.this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            int respuesta = (int) root.get("confirmacion");
                            if (respuesta == 1) {
                                startActivity(new Intent(Cliente.this, Cliente.class));
                                Toast.makeText(Cliente.this, "Cliente actualizado Satisfactoriamente", Toast.LENGTH_LONG).show();
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
        int width = (int) (Cliente.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Cliente.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        editarDatosCliente.getWindow().setLayout(width, height);
        editarDatosCliente.show();
    }

    private void verCuentas(int pos) {
        Intent intent = new Intent(Cliente.this, Cuenta.class);
        //intent.putExtra("codigoCliente",listaClientes.get(pos).getCodigo());
        PasoDeParametros.CODIGO_DE_CLIENTE = listaClientes.get(pos).getCodigo();
        startActivity(intent);
    }

    private void verEnMapa(int pos) {
        Intent intent = new Intent(Cliente.this, MapsActivity.class);
        intent.putExtra("la", listaClientes.get(pos).getLatitud());
        intent.putExtra("lo", listaClientes.get(pos).getLongitud());
        intent.putExtra("no", listaClientes.get(pos).getNombre());
        intent.putExtra("es", listaClientes.get(pos).getEstado() + "");
        PasoDeParametros.TIPO_DE_CLIENTE = 3 + "";
        startActivity(intent);
    }

    private void cargarListViewTodos() {
        String cod_tra = sharedPreferences.getString("codigo_trabajador", "");
        String URL = Rutas.LISTA_CLIENTES_TODOS + cod_tra;
        System.out.println(URL);
        RequestQueue queue = Volley.newRequestQueue(Cliente.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ClienteModel clienteModel = null;
                    JSONObject root = new JSONObject(response);
                    JSONArray jsonArray = root.getJSONArray("clientesCon");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            String codigo = (int) jsonArray.getJSONObject(i).get("id") + "";
                            String nombre = (String) jsonArray.getJSONObject(i).get("nombre");
                            String celular = (int) jsonArray.getJSONObject(i).get("celular") + "";
                            String latitud = (String) jsonArray.getJSONObject(i).get("latitud");
                            String longitud = (String) jsonArray.getJSONObject(i).get("longitud");
                            String estado = (int) jsonArray.getJSONObject(i).get("conPrestamo") + "";
                            clienteModel = new ClienteModel(codigo, nombre, celular, latitud, longitud, estado);
                        } catch (JSONException e) {
                            Log.e("Parser JSON", e.toString());
                        }
                        listaClientes.add(clienteModel);
                    }
                    jsonArray = root.getJSONArray("clientesSin");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            String codigo = (int) jsonArray.getJSONObject(i).get("id") + "";
                            String nombre = (String) jsonArray.getJSONObject(i).get("nombre");
                            String celular = (int) jsonArray.getJSONObject(i).get("celular") + "";
                            String latitud = (String) jsonArray.getJSONObject(i).get("latitud");
                            String longitud = (String) jsonArray.getJSONObject(i).get("longitud");
                            String estado = (int) jsonArray.getJSONObject(i).get("conPrestamo") + "";
                            clienteModel = new ClienteModel(codigo, nombre, celular, latitud, longitud, estado);
                        } catch (JSONException e) {
                            Log.e("Parser JSON", e.toString());
                        }
                        listaClientes.add(clienteModel);
                    }
                    adapter = new ClienteListAdapter(Cliente.this, R.layout.clientes_card, listaClientes);
                    lv.setAdapter(adapter);
                    progressDialog.dismiss();
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

    private void cargarListViewPendiente() {
        String cod_tra = sharedPreferences.getString("codigo_trabajador", "");
        String URL = Rutas.LISTA_CLIENTES_PENDIENTES + cod_tra;
        System.out.println(URL);
        RequestQueue queue = Volley.newRequestQueue(Cliente.this);
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
                            String celular = (int) jsonArray.getJSONObject(i).get("celular") + "";
                            String latitud = (String) jsonArray.getJSONObject(i).get("latitud");
                            String longitud = (String) jsonArray.getJSONObject(i).get("longitud");
                            String estado = (int) jsonArray.getJSONObject(i).get("conPrestamo") + "";
                            System.out.println(nombre + "***********************************");
                            clienteModel = new ClienteModel(codigo, nombre, celular, latitud, longitud, estado);
                        } catch (JSONException e) {
                            Log.e("Parser JSON", e.toString());
                        }
                        listaClientes.add(clienteModel);
                    }
                    adapter = new ClienteListAdapter(Cliente.this, R.layout.clientes_card, listaClientes);
                    lv.setAdapter(adapter);
                    progressDialog.dismiss();
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
        sharedPreferences = getSharedPreferences("colombianos", MODE_PRIVATE);
        listaClientes = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lv_cliente);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando Clientes...");
        progressDialog.show();

    }

}
