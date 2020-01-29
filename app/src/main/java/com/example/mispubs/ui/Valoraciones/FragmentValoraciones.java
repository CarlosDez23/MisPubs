package com.example.mispubs.ui.Valoraciones;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mispubs.Modelo.Pub;
import com.example.mispubs.Modelo.Valoracion;
import com.example.mispubs.R;
import com.example.mispubs.REST.APIUtils;
import com.example.mispubs.REST.ValoracionRest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentValoraciones extends Fragment {

    private RecyclerView recyclerView;


    private ArrayList<Valoracion> listaValoraciones = new ArrayList<>();

    private Pub pub;

    public FragmentValoraciones(Pub pub) {
        this.pub = pub;
    }

    //Para el REST
    private ValoracionRest valoracionRest;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_valoraciones_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        valoracionRest = APIUtils.getServiceValoraciones();

        llamarVistas();

    }

    private void llamarVistas(){
        recyclerView = getView().findViewById(R.id.recyclerValoraciones);
        GridLayoutManager manager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        listarValoraciones();

    }

    private void listarValoraciones(){
        Call<List<Valoracion>> call = valoracionRest.findValoracionesPub(pub.getId());
        call.enqueue(new Callback<List<Valoracion>>() {
            @Override
            public void onResponse(Call<List<Valoracion>> call, Response<List<Valoracion>> response) {
                if (response.isSuccessful()){
                    if (response.code() == 200){
                        listaValoraciones = (ArrayList<Valoracion>) response.body();
                        recyclerView.setAdapter(new ValoracionesAdapter(listaValoraciones,getContext()));
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Valoracion>> call, Throwable t) {

            }
        });
    }

}
