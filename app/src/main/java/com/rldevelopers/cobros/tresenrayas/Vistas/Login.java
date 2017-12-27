package com.rldevelopers.cobros.tresenrayas.Vistas;

import android.app.ProgressDialog;
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

public class Login extends AppCompatActivity {
    EditText nombre, contra;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
    }

    private void init() {
        sharedPreferences = getSharedPreferences("colombianos", MODE_PRIVATE);
        boolean ingresar = sharedPreferences.getBoolean("superUsuario", false);
        if (ingresar) {
            startActivity(new Intent(Login.this, Menu.class));
        }
        nombre = (EditText) findViewById(R.id.et_login_name);
        contra = (EditText) findViewById(R.id.et_login_contra);
    }

    public void iniciarSesion(View view) {
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("VERIFICANDO");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        ConsultaPass(Rutas.LOGIN + nombre.getText().toString().trim() + "/" + contra.getText().toString().trim());
    }

    private void ConsultaPass(String URL) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject root = new JSONObject(response);
                    int respuesta = (int) root.get("respuesta");
                    if (respuesta == 1) {
                        sharedPreferences.edit().putBoolean("superUsuario", true).apply();
                        Toast.makeText(getApplicationContext(), "Bienvenido " + nombre.getText().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, Menu.class);
                        startActivity(intent);
                        pDialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Verifique su contraseña", Toast.LENGTH_SHORT).show();
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
}
