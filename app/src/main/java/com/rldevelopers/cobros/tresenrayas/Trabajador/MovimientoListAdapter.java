package com.rldevelopers.cobros.tresenrayas.Trabajador;

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

public class MovimientoListAdapter extends ArrayAdapter<MovimientoModel> {
    private Activity context;
    private int resource;
    private List<MovimientoModel> listImage;
    private int TIPO_DE_MOVIMIENTO;

    public MovimientoListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<MovimientoModel> objects, int TIPO_MOVIMIENTO) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
        TIPO_DE_MOVIMIENTO = TIPO_MOVIMIENTO;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource, null);
        TextView tvFecha = (TextView) v.findViewById(R.id.cardTrabajador_informe_fecha);
        TextView tvNombre = (TextView) v.findViewById(R.id.cardTrabajador_informe_nombreTrabajdor);
        TextView tvMonto = (TextView) v.findViewById(R.id.cardTrabajador_informe_monto);
        TextView tvDetalle = (TextView) v.findViewById(R.id.cardTrabajador_informe_detalle);
        TextView tvDescripcion = (TextView) v.findViewById(R.id.cardTrabajador_informe_descripcion);
        TextView tvIndicador = (TextView) v.findViewById(R.id.cardTrabajador_informe_indicador);
        if (TIPO_DE_MOVIMIENTO == 1) {
            tvIndicador.setText("▼");
            tvIndicador.setTextColor(Color.RED);
        } else if (TIPO_DE_MOVIMIENTO == 2) {
            tvIndicador.setText("▲");
            tvIndicador.setTextColor(Color.GREEN);
        } else if (TIPO_DE_MOVIMIENTO == 3) {
            tvIndicador.setText("▼");
            tvIndicador.setTextColor(Color.RED);
        }

        tvFecha.setText(listImage.get(position).getFecha());
        tvNombre.setText(listImage.get(position).getNombreTrabajador());
        tvMonto.setText(listImage.get(position).getMonto());
        tvDetalle.setText(listImage.get(position).getDetalle());
        tvDescripcion.setText(listImage.get(position).getDescripcion());
        return v;
    }

}
