package com.rldevelopers.cobros.tresenrayas.Cliente;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rldevelopers.cobros.tresenrayas.R;
import com.rldevelopers.cobros.tresenrayas.RUTAS.Rutas;
import com.rldevelopers.cobros.tresenrayas.Trabajador.Menu_Trabajador;

import org.json.JSONException;
import org.json.JSONObject;

public class AgregarCliente extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    LatLng ubicacion;
    EditText nombre, celular, carnet, direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cliente__agregar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapCrerCliente);
        mapFragment.getMapAsync(this);
        init();
    }

    private void init() {
        nombre = (EditText) findViewById(R.id.et_cliente_nombre);
        celular = (EditText) findViewById(R.id.et_cliente_celular);
        carnet = (EditText) findViewById(R.id.et_cliente_carnet);
        direccion = (EditText) findViewById(R.id.et_cliente_direccion);
    }

    public void csatelite(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void cterreno(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    public void chibrido(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    public void cnormal(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    //agregar cliente
    boolean agregarCliente = true;

    public void agregarCliente(View view) {
        if (!validar()) {
            return;
        }
        if (agregarCliente) {
            agregarCliente = !agregarCliente;
            String la = ubicacion.latitude + "";
            String lo = ubicacion.longitude + "";
            String URL = Rutas.AGREGAR_CLIENTE + nombre.getText().toString().trim() +
                    "/" + celular.getText().toString().trim() + "/" + carnet.getText().toString().trim() +
                    "/" + direccion.getText().toString().trim() + "/" + la + "/" + lo;
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject root = new JSONObject(response);
                        int respuesta = (int) root.get("confirmacion");
                        if (respuesta == 1) {
                            startActivity(new Intent(AgregarCliente.this, Menu_Trabajador.class));
                            Toast.makeText(AgregarCliente.this, "Cliente agregado Satisfactoriamente", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    agregarCliente = !agregarCliente;
                    Toast.makeText(getApplicationContext(), "Compruebe su conexion a Internet!", Toast.LENGTH_LONG).show();
                }
            });
            queue.add(stringRequest);
        } else {
            Toast.makeText(AgregarCliente.this, "Operacion en curso espere por favor!", Toast.LENGTH_LONG).show();
        }

    }

    private boolean validar() {
        String nom = nombre.getText().toString().trim();
        String cel = celular.getText().toString().trim();
        String car = carnet.getText().toString().trim();
        String dir = direccion.getText().toString().trim();

        if (nom.isEmpty()) {
            nombre.setError("Debe llenar este campo...");
            nombre.requestFocus();
            return false;
        } else if (cel.isEmpty()) {
            celular.setError("Debe llenar este campo...");
            celular.requestFocus();
            return false;
        } else if (car.isEmpty()) {
            carnet.setError("Debe llenar este campo...");
            carnet.requestFocus();
            return false;
        } else if (dir.isEmpty()) {
            direccion.setError("Debe llenar este campo...");
            direccion.requestFocus();
            return false;
        }
        if (ubicacion == null) {
            Toast.makeText(AgregarCliente.this, "Tocar el mapa Por favor!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addMyLocation(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LatLng miUbicacionActual = new LatLng(-17.797010101762, -63.2004534171);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbicacionActual, 14));
        //   mMap.addMarker(new MarkerOptions().position(miUbicacionActual).title("Ubicacion Actual"));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .anchor(0.5f, 0.5f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                        .position(latLng)
                        .snippet("Sin Agregar")
                        .title("Cliente Nuevo"));
                ubicacion = latLng;
            }
        });

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
    }

    private void addMyLocation(boolean b) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(b);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (permissions.length == 1 &&
                        permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permiso aceptado!", Toast.LENGTH_LONG).show();

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                } else {
                    Toast.makeText(getBaseContext(), "Permiso realizado...", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
