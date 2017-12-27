package com.rldevelopers.cobros.tresenrayas.Cliente;

import android.app.Activity;
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

public class ClienteListAdapter extends ArrayAdapter<ClienteModel> {
    private Activity context;
    private int resource;
    private List<ClienteModel> listImage;

    public ClienteListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<ClienteModel> objects) {
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
        TextView tvCodigo = (TextView) v.findViewById(R.id.cardCliente_codigo);
        TextView tvNombre = (TextView) v.findViewById(R.id.cardCliente_nombre);
        TextView tvCelular = (TextView) v.findViewById(R.id.cardCliente_celular);

        tvCodigo.setText(listImage.get(position).getCodigo());
        tvNombre.setText(listImage.get(position).getNombre());
        tvCelular.setText(listImage.get(position).getCelular());
        return v;
    }

}
