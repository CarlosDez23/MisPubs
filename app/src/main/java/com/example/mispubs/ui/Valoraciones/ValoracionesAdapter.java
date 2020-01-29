package com.example.mispubs.ui.Valoraciones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mispubs.Modelo.Valoracion;
import com.example.mispubs.R;


import java.util.ArrayList;

public class ValoracionesAdapter extends RecyclerView.Adapter<ValoracionesAdapter.ViewHolder> {

    ArrayList<Valoracion> listValoraciones;
    Context context;

    public ValoracionesAdapter(ArrayList<Valoracion> listValoraciones, Context context) {
        this.listValoraciones = listValoraciones;
        this.context = context;


    }

    @NonNull
    @Override
    public ValoracionesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemListaValoraciones = layoutInflater.inflate(R.layout.item_valoraciones,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemListaValoraciones);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ValoracionesAdapter.ViewHolder holder, int position) {
        final Valoracion v = listValoraciones.get(position);
        holder.rbValoracion.setRating((float)v.getValoracion());
        holder.tvValoracionUsuario.setText(String.valueOf(v.getIdusuario()));
        holder.tvValoracionDetalle.setText(v.getDetalle());
        holder.tvCardValoracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Pulsada",Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return listValoraciones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RatingBar rbValoracion;
        private TextView tvValoracionUsuario;
        private TextView tvValoracionDetalle;
        private CardView tvCardValoracion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.rbValoracion = itemView.findViewById(R.id.rbValoracion);
            this.tvValoracionUsuario = itemView.findViewById(R.id.tvValoracionUsuario);
            this.tvValoracionDetalle = itemView.findViewById(R.id.tvValoracionDetalle);
            tvCardValoracion = itemView.findViewById(R.id.cardValoracion);

        }
    }
}
