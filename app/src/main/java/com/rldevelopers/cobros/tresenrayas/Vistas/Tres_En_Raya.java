package com.rldevelopers.cobros.tresenrayas.Vistas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rldevelopers.cobros.tresenrayas.R;


public class Tres_En_Raya extends AppCompatActivity implements View.OnClickListener {


    Button b1,b2,b3,b4,b5,b6,b7,b8,b9;
    private int juego[][];
    private boolean sw;
    private int c;
    TextView jLabel2;

    int i;
    public void ingresarOculto(View view){
        i++;
        if(i==5){
            startActivity(new Intent(this,Login.class));
            //startActivity(new Intent(this,Login_Trabajador.class));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juego);
        i=0;
        b1=(Button)findViewById(R.id.b1);
        b2=(Button)findViewById(R.id.b2);
        b3=(Button)findViewById(R.id.b3);
        b4=(Button)findViewById(R.id.b4);
        b5=(Button)findViewById(R.id.b5);
        b6=(Button)findViewById(R.id.b6);
        b7=(Button)findViewById(R.id.b7);
        b8=(Button)findViewById(R.id.b8);
        b9=(Button)findViewById(R.id.b9);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);
        b9.setOnClickListener(this);

        jLabel2=(TextView)findViewById(R.id.label);

        iniciar();

    }

    private void iniciar() {

        juego = new int[3][3];
        sw = true;
        c = 0;
        //initComponents();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                juego[i][j] = 0;
            }
        }
        b1.setBackgroundColor(00000000);
        b2.setBackgroundColor(00000000);
        b3.setBackgroundColor(00000000);
        b4.setBackgroundColor(00000000);
        b5.setBackgroundColor(00000000);
        b6.setBackgroundColor(00000000);
        b7.setBackgroundColor(00000000);
        b8.setBackgroundColor(00000000);
        b9.setBackgroundColor(00000000);

        jLabel2.setText("Turno del jugador 1...");

    }



    private boolean gano(int j) {
        int a, b, c, d, e, f, g, h, i;
        a = juego[0][0];
        b = juego[0][1];
        c = juego[0][2];
        d = juego[1][0];
        e = juego[1][1];
        f = juego[1][2];
        g = juego[2][0];
        h = juego[2][1];
        i = juego[2][2];
        if (a == j && a == b && b == c) {
            if(j==1){
                b1.setBackgroundResource(R.drawable.exit);
                b2.setBackgroundResource(R.drawable.exit);
                b3.setBackgroundResource(R.drawable.exit);
            }else{
                b1.setBackgroundResource(R.drawable.jugador2);
                b2.setBackgroundResource(R.drawable.jugador2);
                b3.setBackgroundResource(R.drawable.jugador2);
            }



            return true;
        }
        if (d == j && d == e && e == f) {
            if(j==1){
                b4.setBackgroundResource(R.drawable.jugador);
                b5.setBackgroundResource(R.drawable.jugador);
                b6.setBackgroundResource(R.drawable.jugador);
            }else{
                b4.setBackgroundResource(R.drawable.jugador2);
                b5.setBackgroundResource(R.drawable.jugador2);
                b6.setBackgroundResource(R.drawable.jugador2);
            }
            return true;
        }
        if (g == j && g == h && h == i) {
            if(j==1){
                b7.setBackgroundResource(R.drawable.jugador);
                b8.setBackgroundResource(R.drawable.jugador);
                b9.setBackgroundResource(R.drawable.jugador);
            }else{
                b7.setBackgroundResource(R.drawable.jugador2);
                b8.setBackgroundResource(R.drawable.jugador2);
                b9.setBackgroundResource(R.drawable.jugador2);
            }
            return true;
        }
        if (a == j && a == d && d == g) {
            if(j==1){
                b1.setBackgroundResource(R.drawable.jugador);
                b4.setBackgroundResource(R.drawable.jugador);
                b7.setBackgroundResource(R.drawable.jugador);
            }else{
                b1.setBackgroundResource(R.drawable.jugador2);
                b4.setBackgroundResource(R.drawable.jugador2);
                b7.setBackgroundResource(R.drawable.jugador2);
            }
            return true;
        }
        if (b == j && b == e && e == h) {
            if(j==1){
                b2.setBackgroundResource(R.drawable.jugador);
                b5.setBackgroundResource(R.drawable.jugador);
                b8.setBackgroundResource(R.drawable.jugador);
            }else{
                b2.setBackgroundResource(R.drawable.jugador2);
                b5.setBackgroundResource(R.drawable.jugador2);
                b8.setBackgroundResource(R.drawable.jugador2);
            }
            return true;
        }
        if (c == j && c == f && f == i) {
            if(j==1){
                b3.setBackgroundResource(R.drawable.jugador);
                b6.setBackgroundResource(R.drawable.jugador);
                b9.setBackgroundResource(R.drawable.jugador);
            }else{
                b3.setBackgroundResource(R.drawable.jugador2);
                b6.setBackgroundResource(R.drawable.jugador2);
                b9.setBackgroundResource(R.drawable.jugador2);
            }
            return true;
        }
        if (a == j && a == e && e == i) {
            if(j==1){
                b1.setBackgroundResource(R.drawable.jugador);
                b5.setBackgroundResource(R.drawable.jugador);
                b9.setBackgroundResource(R.drawable.jugador);
            }else{
                b1.setBackgroundResource(R.drawable.jugador2);
                b5.setBackgroundResource(R.drawable.jugador2);
                b9.setBackgroundResource(R.drawable.jugador2);
            }
            return true;
        }
        if (c == j && c == e && e == g) {
            if(j==1){
                b3.setBackgroundResource(R.drawable.jugador);
                b5.setBackgroundResource(R.drawable.jugador);
                b7.setBackgroundResource(R.drawable.jugador);
            }else{
                b3.setBackgroundResource(R.drawable.jugador2);
                b5.setBackgroundResource(R.drawable.jugador2);
                b7.setBackgroundResource(R.drawable.jugador2);
            }
            return true;
        }
        return false;
    }

    public void jugarDeNuevo(View view){
        iniciar();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.b1:
                if (!gano(1) && !gano(2)) {

                    if (juego[0][0] == 0) {
                        if(sw){
                            jLabel2.setText("Turno del jugador 2...");
                        }else{
                            jLabel2.setText("Turno del jugador 1...");
                        }

                        c++;
                        if (sw) {
                            b1.setBackgroundResource(R.drawable.jugador);
                            juego[0][0] = 1;
                            if (gano(1)) {
                                jLabel2.setText("El jugador 1 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }

                        } else {
                            b1.setBackgroundResource(R.drawable.jugador2);
                            juego[0][0] = 2;
                            if (gano(2)) {
                                jLabel2.setText("El jugador 2 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }
                        }
                        sw = !sw;
                    }
                }

                break;
            case R.id.b2:
                if (!gano(1) && !gano(2)) {

                    if (juego[0][1] == 0) {
                        if(sw){
                            jLabel2.setText("Turno del jugador 2...");
                        }else{
                            jLabel2.setText("Turno del jugador 1...");
                        }

                        c++;
                        if (sw) {
                            b2.setBackgroundResource(R.drawable.jugador);
                            juego[0][1] = 1;
                            if (gano(1)) {
                                jLabel2.setText("El jugador 1 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }

                        } else {
                            b2.setBackgroundResource(R.drawable.jugador2);
                            juego[0][1] = 2;
                            if (gano(2)) {
                                jLabel2.setText("El jugador 2 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }
                        }
                        sw = !sw;
                    }
                }

                break;
            case R.id.b3:
                if (!gano(1) && !gano(2)) {

                    if (juego[0][2] == 0) {
                        if(sw){
                            jLabel2.setText("Turno del jugador 2...");
                        }else{
                            jLabel2.setText("Turno del jugador 1...");
                        }

                        c++;
                        if (sw) {
                            b3.setBackgroundResource(R.drawable.jugador);
                            juego[0][2] = 1;
                            if (gano(1)) {
                                jLabel2.setText("El jugador 1 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }

                        } else {
                            b3.setBackgroundResource(R.drawable.jugador2);
                            juego[0][2] = 2;
                            if (gano(2)) {
                                jLabel2.setText("El jugador 2 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }
                        }
                        sw = !sw;
                    }
                }

                break;
            case R.id.b4:
                if (!gano(1) && !gano(2)) {

                    if (juego[1][0] == 0) {
                        if(sw){
                            jLabel2.setText("Turno del jugador 2...");
                        }else{
                            jLabel2.setText("Turno del jugador 1...");
                        }

                        c++;
                        if (sw) {
                            b4.setBackgroundResource(R.drawable.jugador);
                            juego[1][0] = 1;
                            if (gano(1)) {
                                jLabel2.setText("El jugador 1 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }

                        } else {
                            b4.setBackgroundResource(R.drawable.jugador2);
                            juego[1][0] = 2;
                            if (gano(2)) {
                                jLabel2.setText("El jugador 2 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }
                        }
                        sw = !sw;
                    }
                }

                break;
            case R.id.b5:
                if (!gano(1) && !gano(2)) {

                    if (juego[1][1] == 0) {
                        if(sw){
                            jLabel2.setText("Turno del jugador 2...");
                        }else{
                            jLabel2.setText("Turno del jugador 1...");
                        }

                        c++;
                        if (sw) {
                            b5.setBackgroundResource(R.drawable.jugador);
                            juego[1][1] = 1;
                            if (gano(1)) {
                                jLabel2.setText("El jugador 1 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }

                        } else {
                            b5.setBackgroundResource(R.drawable.jugador2);
                            juego[1][1] = 2;
                            if (gano(2)) {
                                jLabel2.setText("El jugador 2 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }
                        }
                        sw = !sw;
                    }
                }

                break;
            case R.id.b6:
                if (!gano(1) && !gano(2)) {

                    if (juego[1][2] == 0) {
                        if(sw){
                            jLabel2.setText("Turno del jugador 2...");
                        }else{
                            jLabel2.setText("Turno del jugador 1...");
                        }

                        c++;
                        if (sw) {
                            b6.setBackgroundResource(R.drawable.jugador);
                            juego[1][2] = 1;
                            if (gano(1)) {
                                jLabel2.setText("El jugador 1 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }

                        } else {
                            b6.setBackgroundResource(R.drawable.jugador2);
                            juego[1][2] = 2;
                            if (gano(2)) {
                                jLabel2.setText("El jugador 2 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }
                        }
                        sw = !sw;
                    }
                }

                break;
            case R.id.b7:
                if (!gano(1) && !gano(2)) {

                    if (juego[2][0] == 0) {
                        if(sw){
                            jLabel2.setText("Turno del jugador 2...");
                        }else{
                            jLabel2.setText("Turno del jugador 1...");
                        }

                        c++;
                        if (sw) {
                            b7.setBackgroundResource(R.drawable.jugador);
                            juego[2][0] = 1;
                            if (gano(1)) {
                                jLabel2.setText("El jugador 1 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }

                        } else {
                            b7.setBackgroundResource(R.drawable.jugador2);
                            juego[2][0] = 2;
                            if (gano(2)) {
                                jLabel2.setText("El jugador 2 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }
                        }
                        sw = !sw;
                    }
                }

                break;
            case R.id.b8:
                if (!gano(1) && !gano(2)) {

                    if (juego[2][1] == 0) {
                        if(sw){
                            jLabel2.setText("Turno del jugador 2...");
                        }else{
                            jLabel2.setText("Turno del jugador 1...");
                        }

                        c++;
                        if (sw) {
                            b8.setBackgroundResource(R.drawable.jugador);
                            juego[2][1] = 1;
                            if (gano(1)) {
                                jLabel2.setText("El jugador 1 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }

                        } else {
                            b8.setBackgroundResource(R.drawable.jugador2);
                            juego[2][1] = 2;
                            if (gano(2)) {
                                jLabel2.setText("El jugador 2 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }
                        }
                        sw = !sw;
                    }
                }


                break;
            case R.id.b9:
                if (!gano(1) && !gano(2)) {

                    if (juego[2][2] == 0) {
                        if(sw){
                            jLabel2.setText("Turno del jugador 2...");
                        }else{
                            jLabel2.setText("Turno del jugador 1...");
                        }

                        c++;
                        if (sw) {
                            b9.setBackgroundResource(R.drawable.jugador);
                            juego[2][2] = 1;
                            if (gano(1)) {
                                jLabel2.setText("El jugador 1 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }

                        } else {
                            b9.setBackgroundResource(R.drawable.jugador2);
                            juego[2][2] = 2;
                            if (gano(2)) {
                                jLabel2.setText("El jugador 2 gano...");
                            } else {
                                if (c == 9) {

                                    jLabel2.setText("Juego empatado..");
                                }
                            }
                        }
                        sw = !sw;
                    }
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Bye...",Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
    private void pausar(){

    }
}

