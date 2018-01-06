package com.rldevelopers.cobros.tresenrayas.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rldevelopers.cobros.tresenrayas.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ronald Lopez on 26/12/2017.
 */

public class UtilsRL extends Activity {

    private Context ctx;
    private Activity actividad;

    public UtilsRL(Activity act) {
        ctx = act.getApplicationContext();
        actividad = act;

    }

    public void fragment() {

        final Dialog registrar_gasto = new Dialog(ctx);
        registrar_gasto.setTitle("Agregar Credito");
        registrar_gasto.setContentView(R.layout.trabajador_registrar_gasto);
        final EditText monto = (EditText) registrar_gasto.findViewById(R.id.et_trabajador_gasto_monto);
        final EditText descripcion = (EditText) registrar_gasto.findViewById(R.id.et_trabajador_gasto_descripcion);
        Button boton = (Button) registrar_gasto.findViewById(R.id.bt_trabajador_gasto);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
      //  int width = (int) (getBaseContext().this.getResources().getDisplayMetrics().widthPixels);
        //int height = (int) (Activity.this.getResources().getDisplayMetrics().heightPixels * 0.9);
       // registrar_gasto.getWindow().setLayout(width, height);
        registrar_gasto.show();

    }

    public void mensajeToast(String s) {
        Toast.makeText(ctx.getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    public void mensajeToastConImagen(String s) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.login, null);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static String obtenerFecha() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaInString = dateFormat.format(new Date());
        return fechaInString;
    }

    public void compartirTexto(Context context, String mensaje) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, mensaje);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, mensaje));
    }

    public void multipleSelccion(Context context) {
        final ArrayList<String> listItems = new ArrayList<>();
        listItems.add("Ver Cuentas");
        listItems.add("No Pago!");
        listItems.add("Ver en Mapa");
        listItems.add("Editar Datos");
        new AlertDialog.Builder(ctx.getApplicationContext())
                .setTitle("Elija una Opción:")
                .setCancelable(false)
                .setAdapter(new ArrayAdapter<String>(ctx.getApplicationContext(), android.R.layout.simple_list_item_1, listItems),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                       /* //Toast.makeText(Cliente.this, pos + "  " + listaClientes.get(pos).getNombre(), Toast.LENGTH_LONG).show();
                                        Toast.makeText(Cliente.this, listItems.get(item), Toast.LENGTH_LONG).show();*/
                                switch (item) {
                                    case 0:
                                        break;
                                    case 1:
                                        break;
                                    case 2:
                                        break;
                                    case 3:
                                        break;
                                }
                            }
                        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        }).show();
    }

    public void tres_opciones() {
        AlertDialog.Builder cliente = new AlertDialog.Builder(ctx.getApplicationContext());
        cliente.setTitle("Cliente");
        cliente.setMessage("Elija una opcion:");
        cliente.setPositiveButton("Clientes Pendientes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        cliente.setNegativeButton("Todos los Clientes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        cliente.setNeutralButton("Crear Cliente", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        cliente.show();
    }
}
