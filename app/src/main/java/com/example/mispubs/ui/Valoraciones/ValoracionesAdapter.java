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

import com.example.mispubs.Modelo.Usuario;
import com.example.mispubs.Modelo.Valoracion;
import com.example.mispubs.R;
import com.example.mispubs.REST.UsuarioRest;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValoracionesAdapter extends RecyclerView.Adapter<ValoracionesAdapter.ViewHolder> {

    private ArrayList<Valoracion> listValoraciones;
    private Context context;

    private UsuarioRest usuarioRest;


    public ValoracionesAdapter(ArrayList<Valoracion> listValoraciones, Context context, UsuarioRest usuarioRest) {
        this.listValoraciones = listValoraciones;
        this.context = context;
        this.usuarioRest = usuarioRest;


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
        buscarNombreUsuario(v.getIdusuario(),holder);
        holder.tvValoracionDetalle.setText(v.getDetalle());
        holder.tvCardValoracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Pulsada",Toast.LENGTH_LONG).show();
            }
        });


    }


    private void buscarNombreUsuario (int usuario, ValoracionesAdapter.ViewHolder holder){
        Call<Usuario> call = usuarioRest.findByIdUsuarios(usuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.isSuccessful()){
                    if (response.code() == 200){
                        Usuario u = (Usuario) response.body();
                        holder.tvValoracionUsuario.setText(u.getNombre());

                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

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
