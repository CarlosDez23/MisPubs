package com.example.mispubs.ui.Pubs;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.example.mispubs.Utilidades.Util;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class FragmentPubs extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton fabPub;
    private FloatingActionButton fabMenu;
    private FloatingActionButton fabVoz;
    private Spinner spinnerFiltros;
    private int modoVer = 1;

    //Clase para gestion del menú de fabs
    private MFabButtons menuFabs;


    //Lista de pubs
    private ArrayList<Pub> listaPubs = new ArrayList<>();

    //Para el control por voz
    private static final int VOZ = 10;


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
        if(Util.isOnline(getContext())){
            pubRest = APIUtils.getServicePubs();
        }else{
            Toast.makeText(getContext(), "Necesitas una conexión a internet", Toast.LENGTH_LONG).show();
        }

        llamarVistas();

    }

    private void llamarVistas() {
        recyclerView = getView().findViewById(R.id.rvPubs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (Util.isOnline(getContext())) {
            listarPubs();
        } else {
            Toast.makeText(getContext(), "Debes activar una conexión a internet ", Toast.LENGTH_LONG).show();
        }
        gestionMenuFabs();
        this.spinnerFiltros = getView().findViewById(R.id.spinnerPubsEstilos);
        gestionSpinner();
    }

    /**
     * Para gestionar el menú de FABS
     */
    private void gestionMenuFabs() {

        this.fabPub = getView().findViewById(R.id.fabPubs);
        this.fabMenu = getView().findViewById(R.id.fabMenu);
        this.fabVoz = getView().findViewById(R.id.fabVoz);
        menuFabs = new MFabButtons(getContext(), fabMenu, fabPub, fabVoz, getFragmentManager(), this);

    }


    /**
     * Para gestionar los filtros musicales a través del spinner
     */
    private void gestionSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.filtroEstilos, android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltros.setAdapter(adapter);
        spinnerFiltros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipoFiltro = "";
                switch (spinnerFiltros.getSelectedItemPosition()) {
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
                if (tipoFiltro.equals("")) {
                    if (Util.isOnline(getContext())) {
                        listarPubs();
                    } else {
                        Toast.makeText(getContext(), "Debes activar una conexión a internet ", Toast.LENGTH_LONG).show();
                    }
                } else {

                    if (Util.isOnline(getContext())) {
                        listarEstilos(tipoFiltro);
                    } else {
                        Toast.makeText(getContext(), "Debes activar una conexión a internet ", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * CONTROL POR VOZ
     */

    public void activarControlVoz() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "¿Qué estilo buscas?");
        try {
            startActivityForResult(intent, VOZ);
        } catch (Exception e) {
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }


        if (requestCode == VOZ) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> voz = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                String secuencia = "";

                for (String v : voz) {
                    secuencia += " " + v;
                }

                if (secuencia != null) {
                    componerFiltro(secuencia);
                }
            }
        }
    }

    private void componerFiltro(String secuencia){
        String palabraClave = "";

        if (secuencia.contains("rock")){
            palabraClave = "Rock";
            spinnerFiltros.setSelection(1);
        }else if(secuencia.contains("reggaeton")){
            palabraClave = "Reggaeton";
            spinnerFiltros.setSelection(2);
        }else if(secuencia.contains("clásica")){
            palabraClave = "Clasica";
            spinnerFiltros.setSelection(3);
        }else if(secuencia.contains("electrónica")){
            palabraClave = "Electronica";
            spinnerFiltros.setSelection(4);
        }else if(secuencia.contains("indie")){
            palabraClave = "Indie";
            spinnerFiltros.setSelection(5);
        }else if(secuencia.contains("pop")){
            palabraClave = "Pop";
            spinnerFiltros.setSelection(6);
        }

        if (palabraClave.equals("")){
            if (Util.isOnline(getContext())) {
                listarPubs();
            } else {
                Toast.makeText(getContext(), "Debes activar una conexión a internet ", Toast.LENGTH_LONG).show();
            }
        }else{
            if (Util.isOnline(getContext())) {
                listarEstilos(palabraClave);
            } else {
                Toast.makeText(getContext(), "Debes activar una conexión a internet ", Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * Método para consumir REST y que nos devuelva todos los pubs que
     * hay listados
     */
    private void listarPubs() {
        Call<List<Pub>> call = pubRest.findAllPubs();
        call.enqueue(new Callback<List<Pub>>() {
            @Override
            public void onResponse(Call<List<Pub>> call, Response<List<Pub>> response) {
                if (response.isSuccessful()) {
                    listaPubs = (ArrayList<Pub>) response.body();
                    recyclerView.setAdapter(new PubsAdapter(listaPubs, getContext(), getActivity(), getFragmentManager()));
                }
            }

            @Override
            public void onFailure(Call<List<Pub>> call, Throwable t) {
                Toast.makeText(getContext(), "No se han podido recuperar los pubs ", Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Método para consumir el REST, esta vez utilizando algún tipo de filtro
     */
    private void listarEstilos(String estilo) {
        Call<List<Pub>> call = pubRest.findByEstilo(estilo);
        call.enqueue(new Callback<List<Pub>>() {
            @Override
            public void onResponse(Call<List<Pub>> call, Response<List<Pub>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        listaPubs = (ArrayList<Pub>) response.body();
                        recyclerView.setAdapter(new PubsAdapter(listaPubs, getContext(), getActivity(), getFragmentManager()));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Pub>> call, Throwable t) {
                Toast.makeText(getContext(), "No se han podido recuperar los pubs ", Toast.LENGTH_LONG).show();

            }
        });
    }
}