package com.rldevelopers.cobros.tresenrayas.Cuenta;

/**
 * Created by Ronald Lopez on 26/12/2017.
 */


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rldevelopers.cobros.tresenrayas.R;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder> {

    private List<String> calendario;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView fecha;
        public TextView layout;

        public MyViewHolder(View view) {
            super(view);
            fecha = (TextView) view.findViewById(R.id.cardCalendario_fecha);
            layout = (TextView) view.findViewById(R.id.ll_cardCalendario);
        }
    }


    public CalendarAdapter(List<String> calendario) {
        this.calendario = calendario;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cuentas_credito_detalle_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String fe = calendario.get(position);
        holder.fecha.setText(fe);
        if (fe.equals("")) {
            holder.layout.setText("❌");
             //holder.layout.setBackgroundResource(R.drawable.add_cuenta);
        }else {
            holder.layout.setText("✅");
        }
    }

    @Override
    public int getItemCount() {
        return calendario.size();
    }
}