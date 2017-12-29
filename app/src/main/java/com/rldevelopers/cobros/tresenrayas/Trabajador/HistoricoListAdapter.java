package com.rldevelopers.cobros.tresenrayas.Trabajador;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
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
        TextView tvFechaIni = (TextView) v.findViewById(R.id.cardTrabajador_informe_historico_fecha_ini);
        TextView tvFechaFin = (TextView) v.findViewById(R.id.cardTrabajador_informe_historico_fecha_fin);
        TextView tvEstado = (TextView) v.findViewById(R.id.cardTrabajador_informe_historico_estado);
        String estado = "";
        if (listImage.get(position).getEstado().equals("1")) {
            estado = "En Curso";
            tvFechaFin.setText(Html.fromHtml("<b>Fecha Ciere: </b>" + " Sin Fecha"));
            tvEstado.setText(estado);
            tvEstado.setTextColor(Color.RED);
        } else {
            estado = "Finalizado";
            tvEstado.setText(estado);
            tvFechaFin.setText(Html.fromHtml("<b>Fecha Ciere: </b>" + listImage.get(position).getFechaFin()));
        }
        tvFechaIni.setText(Html.fromHtml("<b>Fecha Inicio: </b>" + listImage.get(position).getFecha()));
        return v;
    }

}
