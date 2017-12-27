package com.rldevelopers.cobros.tresenrayas.Cliente;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
import com.rldevelopers.cobros.tresenrayas.RUTAS.PasoDeParametros;
import com.rldevelopers.cobros.tresenrayas.RUTAS.Rutas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    String TIPO;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa_principal);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        TIPO = PasoDeParametros.TIPO_DE_CLIENTE;
        sharedPreferences = getSharedPreferences("colombianos", MODE_PRIVATE);

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
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        String URL = "";
        String cod_tra = sharedPreferences.getString("codigo_trabajador", "");
        if (TIPO.equals("1")) {//TODOS LOS CLIENTES
            URL =  Rutas.LISTA_CLIENTES_TODOS + cod_tra;
            RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject root = new JSONObject(response);
                        JSONArray jsonArray = root.getJSONArray("clientesCon");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                String nombre = (String) jsonArray.getJSONObject(i).get("nombre");
                                double latitud = Double.parseDouble((String) jsonArray.getJSONObject(i).get("latitud"));
                                double longitud = Double.parseDouble((String) jsonArray.getJSONObject(i).get("longitud"));
                                String e = (int) jsonArray.getJSONObject(i).get("conPrestamo") + "";
                                String estado = "";
                                LatLng sydney = new LatLng(latitud, longitud);
                                if (e.equals("1")) {
                                    estado = "Con Prestamo";
                                    mMap.addMarker(new MarkerOptions().position(sydney).title(nombre).snippet(estado + "").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                } else {
                                    estado = "Sin Prestamo";
                                    mMap.addMarker(new MarkerOptions().position(sydney).title(nombre).snippet(estado + "").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                                }
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));

                            } catch (JSONException e) {
                                Log.e("Parser JSON", e.toString());
                                System.out.println("------------------------------------------------------------------------------------------------------");
                            }
                        }
                         jsonArray = root.getJSONArray("clientesSin");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                String nombre = (String) jsonArray.getJSONObject(i).get("nombre");
                                double latitud = Double.parseDouble((String) jsonArray.getJSONObject(i).get("latitud"));
                                double longitud = Double.parseDouble((String) jsonArray.getJSONObject(i).get("longitud"));
                                String e = (int) jsonArray.getJSONObject(i).get("conPrestamo") + "";
                                String estado = "";
                                LatLng sydney = new LatLng(latitud, longitud);
                                if (e.equals("1")) {
                                    estado = "Con Prestamo";
                                    mMap.addMarker(new MarkerOptions().position(sydney).title(nombre).snippet(estado + "").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                } else {
                                    estado = "Sin Prestamo";
                                    mMap.addMarker(new MarkerOptions().position(sydney).title(nombre).snippet(estado + "").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                                }
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));

                            } catch (JSONException e) {
                                Log.e("Parser JSON", e.toString());
                                System.out.println("------------------------------------------------------------------------------------------------------");
                            }
                        }
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


        } else if (TIPO.equals("2")) {//CLIENTES PENDIENTES
            URL =  Rutas.LISTA_CLIENTES_PENDIENTES + cod_tra;
            RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject root = new JSONObject(response);
                        JSONArray jsonArray = root.getJSONArray("clientes");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                String nombre = (String) jsonArray.getJSONObject(i).get("nombre");
                                double latitud = Double.parseDouble((String) jsonArray.getJSONObject(i).get("latitud"));
                                double longitud = Double.parseDouble((String) jsonArray.getJSONObject(i).get("longitud"));
                                String e = (int) jsonArray.getJSONObject(i).get("conPrestamo") + "";
                                String estado = "";
                                LatLng sydney = new LatLng(latitud, longitud);
                                if (e.equals("1")) {
                                    estado = "Con Prestamo";
                                    mMap.addMarker(new MarkerOptions().position(sydney).title(nombre).snippet(estado + "").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                } else {
                                    estado = "Sin Prestamo";
                                    mMap.addMarker(new MarkerOptions().position(sydney).title(nombre).snippet(estado + "").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                                }
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
                            } catch (JSONException e) {
                                Log.e("Parser JSON", e.toString());
                                System.out.println("------------------------------------------------------------------------------------------------------");
                            }
                        }

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


        }else if(TIPO.equals("3")){//INDIVIDUAL
            double latitud =Double.parseDouble( getIntent().getStringExtra("la"));
            double longitud = Double.parseDouble( getIntent().getStringExtra("lo"));
            String nombre = getIntent().getStringExtra("no");
            String estado = getIntent().getStringExtra("es");
            LatLng sydney = new LatLng(latitud, longitud);
           if (estado.equals("1")) {
                estado = "Con Prestamo";
                mMap.addMarker(new MarkerOptions().position(sydney).title(nombre).snippet(estado).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            } else {
                estado = "Sin Prestamo";
                mMap.addMarker(new MarkerOptions().position(sydney).title(nombre).snippet(estado).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));
        }


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
                    Toast.makeText(getBaseContext(), "Permiso realizado", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }
}
