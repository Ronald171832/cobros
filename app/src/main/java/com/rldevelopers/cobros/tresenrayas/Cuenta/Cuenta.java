package com.rldevelopers.cobros.tresenrayas.Cuenta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.rldevelopers.cobros.tresenrayas.Cliente.Cliente;
import com.rldevelopers.cobros.tresenrayas.R;
import com.rldevelopers.cobros.tresenrayas.RUTAS.PasoDeParametros;
import com.rldevelopers.cobros.tresenrayas.RUTAS.Rutas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Cuenta extends AppCompatActivity {
    String CODIGO;
    private List<CuentaModel> listaCuentas;
    private ListView lv;
    private CuentaListAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuenta_principal);
        init();
        cargarListView();
        tocarListView();
    }

    private void init() {
        CODIGO = PasoDeParametros.CODIGO_DE_CLIENTE;
        listaCuentas = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lv_cuentas);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando Cuentas...");
        progressDialog.show();
    }

    private void cargarListView() {
        String URL = Rutas.CUENTAS_CLIENTE + CODIGO;
        System.out.println(URL);
        RequestQueue queue = Volley.newRequestQueue(Cuenta.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    CuentaModel cuentaModel = null;
                    JSONObject root = new JSONObject(response);
                    JSONArray jsonArray = root.getJSONArray("cuentas");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {

                            String codigo = (int) jsonArray.getJSONObject(i).get("id") + "";
                            String monto = (int) jsonArray.getJSONObject(i).get("monto") + "";
                            String fe = (String) jsonArray.getJSONObject(i).get("fecha");
                            String trabajador = (String) jsonArray.getJSONObject(i).get("trabajador");
                            String estado = (int) jsonArray.getJSONObject(i).get("estado") + "";
                            cuentaModel = new CuentaModel(codigo, trabajador, monto, fe, estado);
                        } catch (JSONException e) {
                            Log.e("Parser JSON", e.toString());
                        }
                        listaCuentas.add(cuentaModel);
                    }
                    adapter = new CuentaListAdapter(Cuenta.this, R.layout.cuentas_card, listaCuentas);
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

    private void tocarListView() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int pos, long l) {
                if (verificarCuentaPendiente(pos)) {
                    mostrarDetalleCuenta(listaCuentas.get(pos).getCodigo());
                } else {
                    Toast.makeText(Cuenta.this, "Cuenta Finalizada!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //iniciar para fragmet detalle de cuenta
    TextView tv_monto, tv_dias, tv_fecha, tv_interes, tv_cuota, tv_a_cuenta, tv_cuotasRestantes, tv_dias_retraso;
    RecyclerView recyclerView;
    EditText et_abono;
    LinearLayoutManager layoutManager;
    List<String> calendario;
    private CalendarAdapter mAdapter;
    int diasFaltantes;
    String fechaPago;

    private void mostrarDetalleCuenta(final String codigoCredito) {
        final Dialog agregarCuenta = new Dialog(Cuenta.this);
        agregarCuenta.setTitle("Detalle Credito");
        agregarCuenta.setContentView(R.layout.cuenta_detalle);
        et_abono = (EditText) agregarCuenta.findViewById(R.id.et_cuentaDetalle_abono);
        tv_monto = (TextView) agregarCuenta.findViewById(R.id.tv_cuentaDetalle_monto);
        tv_dias = (TextView) agregarCuenta.findViewById(R.id.tv_cuentaDetalle_dias);
        tv_fecha = (TextView) agregarCuenta.findViewById(R.id.tv_cuentaDetalle_fecha);
        tv_interes = (TextView) agregarCuenta.findViewById(R.id.tv_cuentaDetalle_interes);
        tv_cuota = (TextView) agregarCuenta.findViewById(R.id.tv_cuentaDetalle_cuota);
        tv_a_cuenta = (TextView) agregarCuenta.findViewById(R.id.tv_cuentaDetalle_a_cuenta);
        tv_cuotasRestantes = (TextView) agregarCuenta.findViewById(R.id.tv_cuentaDetalle_cuotas_faltantes);
        tv_dias_retraso = (TextView) agregarCuenta.findViewById(R.id.tv_cuentaDetalle_dias_retraso);
        recyclerView = (RecyclerView) agregarCuenta.findViewById(R.id.rv_detalle_cuotas);
        calendario = new ArrayList<>();
        layoutManager = new GridLayoutManager(getApplicationContext(), 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        obtenerDetalleDeCuenta(codigoCredito);
        mAdapter = new CalendarAdapter(calendario);
        recyclerView.setAdapter(mAdapter);

        Button bt_abonar = (Button) agregarCuenta.findViewById(R.id.bt_cuentaDetalle_abonar);
        Button bt_ver = (Button) agregarCuenta.findViewById(R.id.bt_cuentaDetalle_ver);
        bt_abonar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String abono = et_abono.getText().toString().trim();
                if (abono.isEmpty()) {
                    et_abono.setError("Debe llenar este campo...");
                    et_abono.requestFocus();
                    return;
                }
                agregarPagoDeAbono(codigoCredito);
                agregarCuenta.cancel();

            }
        });

        bt_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verAbonos(codigoCredito);
            }
        });
        int width = (int) (Cuenta.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Cuenta.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        agregarCuenta.getWindow().setLayout(width, height);
        agregarCuenta.show();
    }

    private void verAbonos(final String codigoCredito) {
        AlertDialog.Builder abonos = new AlertDialog.Builder(Cuenta.this);
        abonos.setTitle("Abonos");
        abonos.setMessage("Elija una opcion:");
        abonos.setPositiveButton("Listar Abonos", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                listarAbonos(codigoCredito, 0);
            }
        });
        abonos.setNegativeButton("Compartir Reporte", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                listarAbonos(codigoCredito, 1);
            }
        });
        abonos.show();
    }


    ArrayAdapter<String> arrayAdapter;
    List<String> listaAbonos;

    private void listarAbonos(String codigoCredito, final int tipo) {
        final Dialog agregarCuenta = new Dialog(Cuenta.this);
        agregarCuenta.setTitle("Detalle Credito");
        agregarCuenta.setContentView(R.layout.cuenta_ver_abonos);
        final ListView listView = (ListView) agregarCuenta.findViewById(R.id.lv_abonos);
        listaAbonos = new ArrayList<>();
        String URL = Rutas.LISTAR_ABONOS + codigoCredito;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray jsonArray = root.getJSONArray("abonos");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            String fecha = (String) jsonArray.getJSONObject(i).get("fecha");
                            String monto = (int) jsonArray.getJSONObject(i).get("monto") + "";
                            listaAbonos.add(monto + "Bs.   " + fecha);
                        } catch (JSONException e) {
                            Log.e("Parser JSON", e.toString());
                        }
                    }
                    arrayAdapter = new ArrayAdapter<String>(Cuenta.this, R.layout.cuenta_formato_lista_abonos, listaAbonos);
                    listView.setAdapter(arrayAdapter);
                    if (tipo == 1) {
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String fechaInString = dateFormat.format(new Date());
                        String reporte = "";
                        for (int i = 0; i < listaAbonos.size(); i++) {
                            reporte += listaAbonos.get(i) + "\n";
                        }
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "REPORTE DEL " + fechaInString + "\n" + reporte);
                        startActivity(Intent.createChooser(sendIntent, "COMPARTIR REPORTE:"));
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

        int width = (int) (Cuenta.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Cuenta.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        agregarCuenta.getWindow().setLayout(width, height);
        agregarCuenta.show();
    }

    SharedPreferences sharedPreferences;

    private void agregarPagoDeAbono(final String codigoCredito) {
        sharedPreferences = getSharedPreferences("colombianos", MODE_PRIVATE);
        String cod_tra = sharedPreferences.getString("codigo_trabajador", "");
        String URL = Rutas.PAGO_ABONO + et_abono.getText().toString().trim() +
                "/" + codigoCredito + "/" + cod_tra;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    int respuesta = (int) root.get("confirmacion");
                    if (respuesta == 1) {
                        Toast.makeText(Cuenta.this, "Abono guardado Satisfactoriamente!", Toast.LENGTH_LONG).show();
                        mostrarDetalleCuenta(codigoCredito);
                    } else if (respuesta == 2) {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.cuenta_pago_finalizado, null);
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
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


    //mAdapter.notifyDataSetChanged();
    private void obtenerDetalleDeCuenta(String codigoCredito) {
        String URL = Rutas.DETALLE_CREDITO + codigoCredito;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray jsonArray = root.getJSONArray("cuenta");
                    String monto = (int) jsonArray.getJSONObject(0).get("monto") + "";
                    String interes = (int) jsonArray.getJSONObject(0).get("interes") + "";
                    String fe = (String) jsonArray.getJSONObject(0).get("fecha") + "";
                    String dias = (int) jsonArray.getJSONObject(0).get("dias") + "";
                    String cuota = (int) jsonArray.getJSONObject(0).get("cuota") + "";
                    String acuenta = (int) jsonArray.getJSONObject(0).get("acuenta") + "";

                    int retrasos = (int) root.get("retrazos");
                    int faltantes = (int) root.get("faltantes");
                    tv_monto.setText(Html.fromHtml("<b>Monto: </b>" + monto + " Bs."));
                    tv_dias.setText(Html.fromHtml("<b>Dias: </b>" + dias));
                    tv_fecha.setText(Html.fromHtml("<b>Fecha: </b>" + fe));
                    tv_interes.setText(Html.fromHtml("<b>Interes: </b>" + interes + "%"));
                    tv_cuota.setText(Html.fromHtml("<b>Cuota: </b>" + cuota + " Bs."));
                    tv_a_cuenta.setText(Html.fromHtml("<b>A Cuenta: </b>" + acuenta + " Bs."));
                    tv_cuotasRestantes.setText(Html.fromHtml("<b>Cuotas Restantes: </b>" + faltantes));
                    tv_dias_retraso.setText(Html.fromHtml("<b>Dias de Retraso: </b>" + retrasos));
                    diasFaltantes = Integer.parseInt(dias);
                    jsonArray = root.getJSONArray("cuotas");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            fechaPago = (String) jsonArray.getJSONObject(i).get("fecha_pago");
                            calendario.add(fechaPago);
                        } catch (JSONException e) {
                            Log.e("Parser JSON", e.toString());
                        }
                    }
                    int n = diasFaltantes - calendario.size();
                    for (int i = 0; i < n; i++) {
                        calendario.add("");
                    }
                    //  mAdapter.notifyDataSetChanged();
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

    private boolean verificarCuentaPendiente(int pos) {
        if (listaCuentas.get(pos).getEstado().equals("1")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cuenta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add_cuenta:
                if (verificarCuentaPendiente()) {
                    Toast.makeText(Cuenta.this, "Tiene cuenta Pendiente!", Toast.LENGTH_LONG).show();
                } else {
                    agregarCuenta();
                }
                break;
        }
        return true;
    }

    private boolean verificarCuentaPendiente() {
        if (listaCuentas.isEmpty()) {
            return false;
        }
        if (listaCuentas.get(0).getEstado().equals("1")) {
            return true;
        }
        return false;
    }

    //valores para fragment agregar Cuenta
    EditText fecha, monto, interes, dias, cuota;

    private void agregarCuenta() {
        final Dialog agregarCuenta = new Dialog(Cuenta.this);
        agregarCuenta.setTitle("Agregar Credito");
        agregarCuenta.setContentView(R.layout.cuenta_agregar);
        fecha = (EditText) agregarCuenta.findViewById(R.id.et_cuenta_fecha);
        monto = (EditText) agregarCuenta.findViewById(R.id.et_cuenta_monto);
        interes = (EditText) agregarCuenta.findViewById(R.id.et_cuenta_interes);
        dias = (EditText) agregarCuenta.findViewById(R.id.et_cuenta_dias);
        cuota = (EditText) agregarCuenta.findViewById(R.id.et_cuenta_cuota);
        Button boton = (Button) agregarCuenta.findViewById(R.id.bt_cuota_agregar);
        initAgregarCuenta();
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validar()) {
                    agregarCuentaValidada();
                    agregarCuenta.cancel();
                }
            }
        });
        int width = (int) (Cuenta.this.getResources().getDisplayMetrics().widthPixels);
        int height = (int) (Cuenta.this.getResources().getDisplayMetrics().heightPixels * 0.9);
        agregarCuenta.getWindow().setLayout(width, height);
        agregarCuenta.show();
    }

    private void agregarCuentaValidada() {
        sharedPreferences = getSharedPreferences("colombianos", MODE_PRIVATE);
        String cod_tra = sharedPreferences.getString("codigo_trabajador", "");
        String URL = Rutas.CREAR_CREDITO + monto.getText().toString().trim() +
                "/" + interes.getText().toString().trim() + "/" + fecha.getText().toString().trim()
                + "/" + dias.getText().toString().trim() + "/" + cuota.getText().toString().trim()
                + "/" + CODIGO + "/" + cod_tra;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    int respuesta = (int) root.get("confirmacion");
                    if (respuesta == 1) {
                        startActivity(new Intent(Cuenta.this, Cliente.class));
                        Toast.makeText(Cuenta.this, "Credito agregado Satisfactoriamente", Toast.LENGTH_LONG).show();
                    } else if (respuesta == 0) {
                        Toast.makeText(Cuenta.this, "No tienes Dinero Suficiente para prestar!", Toast.LENGTH_LONG).show();
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

    private void initAgregarCuenta() {
        Date date = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fe = hourdateFormat.format(date);
        fecha.setText(fe);
        monto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    int m = Integer.parseInt(monto.getText().toString().trim());
                    int i = Integer.parseInt(interes.getText().toString().trim());
                    int d = Integer.parseInt(dias.getText().toString().trim());
                    System.out.println(s);
                    double cc = (i / 100.0f) + 1.0f;
                    int c = (int) ((int) ((m * cc) / d) + 0.49);
                    cuota.setText(c + "");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        interes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    int m = Integer.parseInt(monto.getText().toString().trim());
                    int i = Integer.parseInt(interes.getText().toString().trim());
                    int d = Integer.parseInt(dias.getText().toString().trim());
                    System.out.println(s);
                    double cc = (i / 100.0f) + 1.0f;
                    int c = (int) ((int) ((m * cc) / d) + 0.49);
                    cuota.setText(c + "");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dias.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    int m = Integer.parseInt(monto.getText().toString().trim());
                    int i = Integer.parseInt(interes.getText().toString().trim());
                    int d = Integer.parseInt(dias.getText().toString().trim());
                    System.out.println(s);
                    double cc = (i / 100.0f) + 1.0f;
                    int c = (int) ((int) ((m * cc) / d) + 0.49);
                    cuota.setText(c + "");
                }

            }

            @Override

            public void afterTextChanged(Editable s) {

            }
        });

    }

    private boolean validar() {
        String fe = fecha.getText().toString().trim();
        String mo = monto.getText().toString().trim();
        String in = interes.getText().toString().trim();
        String di = dias.getText().toString().trim();
        String cu = cuota.getText().toString().trim();

        if (fe.isEmpty()) {
            fecha.setError("Debe llenar este campo...");
            fecha.requestFocus();
            return false;
        } else if (mo.isEmpty()) {
            monto.setError("Debe llenar este campo...");
            monto.requestFocus();
            return false;
        } else if (in.isEmpty()) {
            interes.setError("El nombre no es valido...");
            interes.requestFocus();
            return false;
        } else if (di.isEmpty()) {
            dias.setError("El nombre no es valido...");
            dias.requestFocus();
            return false;
        } else if (cu.isEmpty()) {
            cuota.setError("El nombre no es valido...");
            cuota.requestFocus();
            return false;
        }
        return true;
    }

}
