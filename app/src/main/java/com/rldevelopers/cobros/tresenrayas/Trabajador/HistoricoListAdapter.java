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

public class HistoricoListAdapter extends ArrayAdapter<HistoricoModel> {
    private Activity context;
    private int resource;
    private List<HistoricoModel> listImage;

    public HistoricoListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<HistoricoModel> objects) {
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
        TextView tvFecha = (TextView) v.findViewById(R.id.cardTrabajador_informe_historico_fecha);
        TextView tvEstado = (TextView) v.findViewById(R.id.cardTrabajador_informe_historico_estado);
        String estado = "";
        if (listImage.get(position).getEstado().equals("1")) {
            estado = "En Curso";
            tvEstado.setText(estado);
            tvEstado.setTextColor(Color.RED);
        } else {
            estado = "Finalizado";
            tvEstado.setText(estado);
        }
        tvFecha.setText(listImage.get(position).getFecha());
        return v;
    }

}
