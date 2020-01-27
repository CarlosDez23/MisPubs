package com.example.mispubs.ui.Pubs;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mispubs.Modelo.Pub;
import com.example.mispubs.R;

import java.util.ArrayList;

public class PubsAdapter extends RecyclerView.Adapter<PubsAdapter.ViewHolder> {

    private ArrayList<Pub> listaPubs = new ArrayList<>();
    private Context context;
    private Activity activity;

    private int position;

    public PubsAdapter(ArrayList<Pub> listaPubs, Context context, Activity activity) {
        this.listaPubs = listaPubs;
        this.context = context;
        this.activity = activity;
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
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

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
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuInflater inflater = activity.getMenuInflater();
            inflater.inflate(R.menu.popup_menu, menu);

        }
    }
}
