package com.example.mispubs.ui.Pubs;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mispubs.Modelo.Pub;
import com.example.mispubs.R;
import com.example.mispubs.Utilidades.Util;
import com.example.mispubs.ui.Valoraciones.FragmentValoraciones;
import com.google.android.gms.dynamic.IFragmentWrapper;

import java.util.ArrayList;

public class PubsAdapter extends RecyclerView.Adapter<PubsAdapter.ViewHolder> {

    private ArrayList<Pub> listaPubs = new ArrayList<>();
    private Context context;
    private Activity activity;
    private FragmentManager fm;
    FragmentDetallePubs detallePubs;
    FragmentTransaction transaction;

    private int position;

    public PubsAdapter(ArrayList<Pub> listaPubs, Context context, Activity activity, FragmentManager fm) {
        this.listaPubs = listaPubs;
        this.context = context;
        this.activity = activity;
        this.fm = fm;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemListaPubs = layoutInflater.inflate(R.layout.item_pubs,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemListaPubs);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Pub pubLista = listaPubs.get(position);
        holder.tvPubsNombre.setText(pubLista.getNombre());
        holder.tvPubsEstilo.setText(pubLista.getEstilo());
        if (pubLista.getImagen() != null){
            holder.ivPubs.setImageBitmap(Util.base64ToBitmap(pubLista.getImagen()));
        }else{
            holder.ivPubs.setImageResource(R.drawable.fondo_por_defecto);
        }
        holder.ivMenu.setOnClickListener(new View.OnClickListener() {

            /**
             * Parte de gestión del menú contextual que se despliega en cada item del layout
             * @param v
             */
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.ivMenu);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.itemmenu1://valoraciones
                                FragmentValoraciones fragmentValoraciones = new FragmentValoraciones(pubLista);
                                transaction = fm.beginTransaction();
                                transaction.replace(R.id.nav_host_fragment, fragmentValoraciones);
                                transaction.addToBackStack(null);
                                transaction.commit();
                                break;
                            case R.id.itemmenu2://modificar
                                detallePubs = new FragmentDetallePubs(listaPubs.get(position),2);
                                transaction = fm.beginTransaction();
                                transaction.replace(R.id.nav_host_fragment,detallePubs );
                                transaction.addToBackStack(null);
                                transaction.commit();
                                break;
                            case R.id.itemmenu3://borrar
                                //Toast.makeText(context,"Borrar "+pubLista.getNombre(),Toast.LENGTH_LONG).show();
                                FragmentDetallePubs detallePubs = new FragmentDetallePubs(listaPubs.get(position),3);
                                transaction = fm.beginTransaction();
                                transaction.replace(R.id.nav_host_fragment,detallePubs );
                                transaction.addToBackStack(null);
                                transaction.commit();
                                break;
                            default:
                                break;

                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        holder.relativePubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detallePubs = new FragmentDetallePubs(listaPubs.get(position),1);
                transaction = fm.beginTransaction();
                transaction.replace(R.id.nav_host_fragment,detallePubs );
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPubs.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPubs;
        private TextView tvPubsNombre;
        private TextView tvPubsEstilo;
        private ImageView ivMenu;
        private CardView relativePubs;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ivPubs = itemView.findViewById(R.id.ivPubs);
            this.tvPubsNombre = itemView.findViewById(R.id.tvPubsNombre);
            this.tvPubsEstilo = itemView.findViewById(R.id.tvPubsEstilo);
            this.ivMenu = itemView.findViewById(R.id.ivMenu);
            relativePubs = itemView.findViewById(R.id.relativePubs);
        }
    }
}
