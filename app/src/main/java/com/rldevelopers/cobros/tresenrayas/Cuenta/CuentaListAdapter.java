package com.rldevelopers.cobros.tresenrayas.Cuenta;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rldevelopers.cobros.tresenrayas.R;

import java.util.List;

/**
 * Created by Ronald Lopez on 6/21/2017.
 */

public class CuentaListAdapter extends ArrayAdapter<CuentaModel> {
    private Activity context;
    private int resource;
    private List<CuentaModel> listImage;

    public CuentaListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<CuentaModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource, null);
        TextView tvFecha = (TextView) v.findViewById(R.id.cardCuenta_fecha);
        TextView tvMonto = (TextView) v.findViewById(R.id.cardCuenta_monto);
        TextView tvEstado = (TextView) v.findViewById(R.id.cardCuenta_estado);

        tvFecha.setText(listImage.get(position).getFecha());
        tvMonto.setText(listImage.get(position).getMonto() +" Bs.");
        if(listImage.get(position).getEstado().equals("1")){
            tvEstado.setText("Pendiente");
            tvEstado.setTextColor(Color.RED);
        }else{
            tvEstado.setText("Finalizado");
        }
        return v;
    }

}
