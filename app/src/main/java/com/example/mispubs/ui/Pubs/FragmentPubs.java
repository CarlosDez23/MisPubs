package com.example.mispubs.ui.Pubs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mispubs.Modelo.Pub;
import com.example.mispubs.R;
import com.example.mispubs.REST.APIUtils;
import com.example.mispubs.REST.PubRest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentPubs extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton fabPub;
    private Spinner spinnerFiltros;
    private int modoVer = 1;



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
        this.fabPub = getView().findViewById(R.id.fabPubs);
        this.fabPub.setOnClickListener(listenerBotones);
        this.spinnerFiltros = getView().findViewById(R.id.spinnerPubsEstilos);
    }

    private View.OnClickListener listenerBotones = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fabPubs:

                    FragmentDetallePubs detallePubs = new FragmentDetallePubs();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.nav_host_fragment,detallePubs );
                    transaction.addToBackStack(null);
                    transaction.commit();


                    break;
                default:
                    break;
            }
        }
    };

    private void gestionSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.filtroEstilos, android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltros.setAdapter(adapter);
        spinnerFiltros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipoFiltro = "";
                switch(spinnerFiltros.getSelectedItemPosition()){
                    case 1:
                        tipoFiltro = "Rock";
                        break;
                    case 2:
                        tipoFiltro = "Reggaeton";
                        break;
                    case 3:
                        tipoFiltro = "Clasica";
                        break;
                    case 4:
                        tipoFiltro = "Electronica";
                        break;
                    case 5:
                        tipoFiltro = "Indie";
                        break;
                    case 6:
                        tipoFiltro = "Pop";
                        break;
                    default:
                        break;
                }

                listarEstilos(tipoFiltro);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                    recyclerView.setAdapter(new PubsAdapter(listaPubs,getContext(),getActivity(), getFragmentManager()));
                }
            }
            @Override
            public void onFailure(Call<List<Pub>> call, Throwable t) {

            }
        });
    }


    /**
     * Método para consumir el REST, esta vez utilizando algún tipo de filtro
     */
    private void listarEstilos (String estilo){
        Call<List<Pub>> call = pubRest.findByEstilo(estilo);
        call.enqueue(new Callback<List<Pub>>() {
            @Override
            public void onResponse(Call<List<Pub>> call, Response<List<Pub>> response) {
                if (response.isSuccessful()){
                    if (response.code() == 200){
                        listaPubs = (ArrayList<Pub>) response.body();
                        recyclerView.setAdapter(new PubsAdapter(listaPubs,getContext(),getActivity(), getFragmentManager()));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Pub>> call, Throwable t) {

            }
        });
    }
}