package com.example.mispubs.ui.Valoraciones;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
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
    private FragmentManager fm;

    private UsuarioRest usuarioRest;


    public ValoracionesAdapter(ArrayList<Valoracion> listValoraciones, Context context, UsuarioRest usuarioRest, FragmentManager fm) {
        this.listValoraciones = listValoraciones;
        this.context = context;
        this.usuarioRest = usuarioRest;
        this.fm = fm;
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


                ValoracionDetalleDialog dialog = new ValoracionDetalleDialog
                        (holder.tvValoracionUsuario.getText().toString(),
                                holder.rbValoracion.getRating(),
                                holder.tvValoracionDetalle.getText().toString());
                dialog.show(fm,"Detalle");


            }
        });


    }

    /**
     * Buscamos en nombre del usuario en nuestro servicio REST utilizando el id de usuario
     * que sacamos de la valoracion. Le pasamos el holder para que directamente le añada el
     * texto a nuestro item del recyclerview
     * @param usuario
     * @param holder
     */
    private void buscarNombreUsuario (int usuario, ValoracionesAdapter.ViewHolder holder){
        Call<Usuario> call = usuarioRest.findByIdUsuarios(usuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.isSuccessful()){
                    if (response.code() == 200){
                        Usuario u = response.body();
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
