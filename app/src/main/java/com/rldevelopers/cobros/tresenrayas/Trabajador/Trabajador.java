package com.rldevelopers.cobros.tresenrayas.Trabajador;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.rldevelopers.cobros.tresenrayas.R;
import com.rldevelopers.cobros.tresenrayas.RUTAS.Rutas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Trabajador extends AppCompatActivity {
    private List<TrabajadorModel> listaTrabajadores;
    private ListView lv;
    private TrabajadorListAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trabajador_pantalla_principal);
        init();
        cargarListView();
        tocarListView();
    }

    private void tocarListView() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int pos, long l) {
                Toast.makeText(Trabajador.this, pos + "  " + listaTrabajadores.get(pos).getNombre(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarListView() {
        String URL =  Rutas.LISTA_TRABAJADORES;
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
                    adapter = new TrabajadorListAdapter(Trabajador.this, R.layout.trabajadores_card, listaTrabajadores);
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
        listaTrabajadores = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lv_trabajador);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando Trabajadores...");
        progressDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_trabajador, menu);
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
                    agregarTrabajadorValidado();
                    agregarTrabajador.cancel();
                }
            }
        });
        int width = (int) (Trabajador.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Trabajador.this.getResources().getDisplayMetrics().heightPixels*0.9);
        agregarTrabajador.getWindow().setLayout(width, height);
        agregarTrabajador.show();
    }

    private void agregarTrabajadorValidado() {
        String URL =  Rutas.AGREGAR_TRABAJADOR + nombre.getText().toString().trim() +
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
                Toast.makeText(getApplicationContext(), "Compruebe su conexion a Internet!", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    private void agregarCodigo() {
        String URL =  Rutas.ID_TRABAJADOR;
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
}
