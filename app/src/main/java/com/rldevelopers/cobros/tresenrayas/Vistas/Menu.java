package com.rldevelopers.cobros.tresenrayas.Vistas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rldevelopers.cobros.tresenrayas.R;
import com.rldevelopers.cobros.tresenrayas.Trabajador.*;

public class Menu extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        sharedPreferences = getSharedPreferences("colombianos", MODE_PRIVATE);

    }

    public void trabajador(View view) {
        startActivity(new Intent(Menu.this, Trabajador.class));
    }

    public void iniciarSesionTrabajador(View view) {
        startActivity(new Intent(Menu.this, Login_Trabajador.class));
    }
    public void cerrarSesion(View view){
        sharedPreferences.edit().putBoolean("superUsuario", false).apply();
        startActivity(new Intent(this, Login.class));
    }

}
