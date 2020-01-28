package com.example.mispubs.ui.Pubs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mispubs.Modelo.Pub;
import com.example.mispubs.R;
import com.example.mispubs.REST.APIUtils;
import com.example.mispubs.REST.PubRest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentPubs extends Fragment {

    private RecyclerView recyclerView;


    private ArrayList<Pub> listaPubs = new ArrayList<>();


    //Para el REST
    private PubRest pubRest;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pubs, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pubRest = APIUtils.getServicePubs();
        llamarVistas();

    }

    private void llamarVistas(){
        recyclerView = getView().findViewById(R.id.rvPubs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listarPubs();
    }



    /**
     * Método para consumir REST y que nos devuelva todos los pubs que
     * hay listados
     */
    private void listarPubs(){
        Call<List<Pub>> call = pubRest.findAllPubs();
        call.enqueue(new Callback<List<Pub>>() {
            @Override
            public void onResponse(Call<List<Pub>> call, Response<List<Pub>> response) {
                if (response.isSuccessful()){
                    listaPubs = (ArrayList<Pub>) response.body();
                    recyclerView.setAdapter(new PubsAdapter(listaPubs,getContext(),getActivity()));
                }
            }
            @Override
            public void onFailure(Call<List<Pub>> call, Throwable t) {

            }
        });
    }
}