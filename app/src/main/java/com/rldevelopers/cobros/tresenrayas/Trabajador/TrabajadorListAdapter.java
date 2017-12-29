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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rldevelopers.cobros.tresenrayas.R;

import java.util.List;

/**
 * Created by Ronald Lopez on 6/21/2017.
 */

public class TrabajadorListAdapter extends ArrayAdapter<TrabajadorModel> {
    private Activity context;
    private int resource;
    private List<TrabajadorModel> listImage;

    public TrabajadorListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<TrabajadorModel> objects) {
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
        TextView tvCodigo = (TextView) v.findViewById(R.id.cardTrabajador_codigo);
        TextView tvNombre = (TextView) v.findViewById(R.id.cardTrabajador_nombre);
        TextView tvCelular = (TextView) v.findViewById(R.id.cardTrabajador_celular);
        TextView tvContra = (TextView) v.findViewById(R.id.cardTrabajador_contra);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.ll_carTrabajador);

        tvCodigo.setText(listImage.get(position).getCodigo());
        tvNombre.setText(listImage.get(position).getNombre());
        tvCelular.setText(listImage.get(position).getCelular());
        tvContra.setText(Html.fromHtml("<b>Contraseña: </b>" + listImage.get(position).getContra()));
        if (listImage.get(position).getEstado().equals("0")) {
            layout.setBackgroundColor(Color.parseColor("#E0E0E0"));
        }
        return v;
    }

}
