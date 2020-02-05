package com.example.mispubs.ui.Valoraciones;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mispubs.Modelo.Pub;
import com.example.mispubs.Modelo.Valoracion;
import com.example.mispubs.R;
import com.example.mispubs.REST.APIUtils;
import com.example.mispubs.REST.UsuarioRest;
import com.example.mispubs.REST.ValoracionRest;
import com.example.mispubs.Utilidades.Util;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentValoraciones extends Fragment {

    //Elementos de la interfaz
    private RecyclerView recyclerView;
    private RatingBar rbGeneral;
    private FloatingActionButton fabValoracionesAdd;
    private TextView tvNombrePub;


    //Para datos
    private ArrayList<Valoracion> listaValoraciones = new ArrayList<>();
    private Pub pub;


    //Para el REST
    private ValoracionRest valoracionRest;
    private UsuarioRest usuarioRest;


    View root;

    public FragmentValoraciones(Pub pub) {
        this.pub = pub;
    }

    public FragmentValoraciones getFragment() {
        return this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_valoraciones_fragment, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Util.isOnline(getContext())) {
            valoracionRest = APIUtils.getServiceValoraciones();
            usuarioRest = APIUtils.getService();
        }
        llamarVistas();
    }

    private void llamarVistas() {
        rbGeneral = getView().findViewById(R.id.rbValoracionesGeneral);
        rbGeneral.setIsIndicator(true);
        recyclerView = getView().findViewById(R.id.recyclerValoraciones);
        GridLayoutManager manager = new GridLayoutManager(getActivity(),
                2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        if (Util.isOnline(getContext())) {
            listarValoraciones();
        } else {
            Toast.makeText(getContext(), "Debes activar una conexión a internet ", Toast.LENGTH_LONG).show();
        }
        fabValoracionesAdd = getView().findViewById(R.id.fabValoracionesAdd);
        fabValoracionesAdd.setOnClickListener(listenerBotones);
        tvNombrePub = getView().findViewById(R.id.tvNombrePub);
        tvNombrePub.setText(pub.getNombre());
    }

    private View.OnClickListener listenerBotones = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ValoracionDialog dialog = new ValoracionDialog(pub.getId(), valoracionRest, getFragment());
            dialog.show(getFragmentManager(), "Mi valoración");
        }
    };

    /**
     * Metodo para listar las valoraciones de la base de datos
     */
    public void listarValoraciones() {
        Call<List<Valoracion>> call = valoracionRest.findValoracionesPub(pub.getId());
        call.enqueue(new Callback<List<Valoracion>>() {
            @Override
            public void onResponse(Call<List<Valoracion>> call, Response<List<Valoracion>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        listaValoraciones = (ArrayList<Valoracion>) response.body();
                        recyclerView.setAdapter(new ValoracionesAdapter
                                (listaValoraciones, getContext(), usuarioRest, getFragmentManager()));
                        establecerValoracionGeneral();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Valoracion>> call, Throwable t) {

            }
        });
    }

    /**
     * Método para sacar la valoración media
     */
    private void establecerValoracionGeneral() {
        int suma = 0;
        for (int i = 0; i < listaValoraciones.size(); i++) {
            suma += listaValoraciones.get(i).getValoracion();
        }
        float media = (float) suma / (float) listaValoraciones.size();
        rbGeneral.setRating(media);

    }

}
