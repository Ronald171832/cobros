package com.rldevelopers.cobros.tresenrayas.Trabajador;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rldevelopers.cobros.tresenrayas.R;
import com.rldevelopers.cobros.tresenrayas.RUTAS.Rutas;

import org.json.JSONException;
import org.json.JSONObject;

public class Login_Trabajador extends AppCompatActivity {
    EditText nombre, contra;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trabajadores_login);
        init();
    }

    private void init() {
        sharedPreferences = getSharedPreferences("colombianos", MODE_PRIVATE);
        boolean ingresar = sharedPreferences.getBoolean("trabajador", false);
     /*   if (ingresar) {
            startActivity(new Intent(Login_Trabajador.this, Menu_Trabajador.class));
        }*/
        nombre = (EditText) findViewById(R.id.et_trabajador_login_name);
        contra = (EditText) findViewById(R.id.et_trabajador_login_contra);
    }

    public void iniciarSesion(View view) {
        pDialog = new ProgressDialog(Login_Trabajador.this);
        pDialog.setMessage("VERIFICANDO");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        ConsultaPass(Rutas.LOGIN_TRABAJADOR + nombre.getText().toString().trim() + "/" + contra.getText().toString().trim());
    }

    private void ConsultaPass(String URL) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject root = new JSONObject(response);
                    int respuesta = (int) root.get("confirmacion");
                    if (respuesta == 1) {
                        sharedPreferences.edit().putBoolean("trabajador", true).apply();
                        sharedPreferences.edit().putString("codigo_trabajador", nombre.getText().toString().trim()).apply();
                        Toast.makeText(getApplicationContext(), "Bienvenido " + nombre.getText().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login_Trabajador.this, Menu_Trabajador.class);
                        startActivity(intent);
                        pDialog.dismiss();
                    } else if (respuesta == 2) {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Verifique su contraseña", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Verificar Datos!", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }

                } catch (JSONException e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "El usuario no existe en la base de datos", Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "El usuario no existe en la base de datos Registrate", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        //TRABAJADOR
        AlertDialog.Builder cliente = new AlertDialog.Builder(Login_Trabajador.this);
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
        //SUPER USUARIO
        //startActivity(new Intent(this, Menu_Principal.class));
    }
}
