package com.rldevelopers.cobros.tresenrayas.Vistas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rldevelopers.cobros.tresenrayas.R;
import com.rldevelopers.cobros.tresenrayas.Trabajador.*;

public class Menu_Principal extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        sharedPreferences = getSharedPreferences("colombianos", MODE_PRIVATE);

    }

    public void trabajador(View view) {
        startActivity(new Intent(Menu_Principal.this, Trabajador.class));
    }

    public void iniciarSesionTrabajador(View view) {
        startActivity(new Intent(Menu_Principal.this, Login_Trabajador.class));
    }

    public void cerrarSesion(View view) {
        sharedPreferences.edit().putBoolean("superUsuario", false).apply();
        startActivity(new Intent(this, Login.class));
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
