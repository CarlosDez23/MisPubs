package com.example.mispubs.ui.Pubs;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mispubs.Modelo.Pub;
import com.example.mispubs.R;
import com.example.mispubs.REST.PubRest;
import com.example.mispubs.ui.Mapas.FragmentMapas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDetallePubs extends Fragment {

    private View root;
    private Pub pub;
    private int modo;
    private final String
            eliminar = "Eliminar Pub",
            modificar = "Modificar Pub",
            guardar = "Guardar Pub";
    private final int
            modoNuevo = 0,
            modoVer = 1,
            modoModificar = 2,
            modoEliminar = 3;
    private Spinner spinnerEstilos;
    private ArrayList<String> listaEstilos = new ArrayList<String>();
    private TextInputLayout
            nombrePub,
            webPub,
            visitasPub;
    private FloatingActionButton cambiarFotoPub, mapaPub;
    private TextView tvDetallePubsModoBoton;
    private CardView cvDetallePubBotonModo;
    private RelativeLayout btnDetallePubBotonModo;
    private PubRest pubRest;


    public FragmentDetallePubs() {
        this.modo = 0;
    }

    public FragmentDetallePubs(Pub pub, int modo) {
        this.pub = pub;
        this.modo = modo;
    }

    public static FragmentDetallePubs newInstance() {
        return new FragmentDetallePubs();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_detalle_pubs_fragment, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        llamarVistas();

        gestionarModo(this.modo);
    }

    private void llamarVistas(){
        this.nombrePub = getView().findViewById(R.id.tvDetallePubsNombre);
        this.webPub = getView().findViewById(R.id.tvDetallePubsWeb);
        this.visitasPub = getView().findViewById(R.id.tvDetallePubsVisitas);
        this.cambiarFotoPub = getView().findViewById(R.id.fabDetallePubsCamara);
        this.mapaPub = getView().findViewById(R.id.fabDetallePubsMapa);
        this.spinnerEstilos = getView().findViewById(R.id.spinnerDetallePubsEstilos);
        this.mapaPub = getView().findViewById(R.id.fabDetallePubsMapa);
        this.mapaPub.setOnClickListener(listenerBotones);
        this.tvDetallePubsModoBoton = getView().findViewById(R.id.tvDetallePubsModoBoton);
        this.cvDetallePubBotonModo = getView().findViewById(R.id.cvDetallePubBotonModo);
        this.btnDetallePubBotonModo = getView().findViewById(R.id.btnDetallePubBotonModo);
        this.btnDetallePubBotonModo.setOnClickListener(listenerBotones);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.estilos, android.R.layout.simple_spinner_dropdown_item);
        spinnerEstilos.setAdapter(adapter);
    }

    private View.OnClickListener listenerBotones = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.fabDetallePubsMapa:
                    FragmentMapas mapa = new FragmentMapas();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.nav_host_fragment,mapa );
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
                case R.id.btnDetallePubBotonModo:
                    gestionarModoBoton(tvDetallePubsModoBoton.getText().toString());
                    break;
                default:
                    break;
            }
        }
    };


    private void rellenarCampos(){
        this.nombrePub.getEditText().setText(this.pub.getNombre());
        this.webPub.getEditText().setText(this.pub.getWeb());
        this.visitasPub.getEditText().setText(Integer.toString(this.pub.getVisitas()));

        seleccionarEstiloPub();
    }

    private void gestionarModo(int modo){

        switch (modo){
            case modoNuevo:
                habilitarDeshabilitar(true);
                this.tvDetallePubsModoBoton.setText(guardar);
                break;
            case modoVer:
                habilitarDeshabilitar(false);
                rellenarCampos();
                this.cvDetallePubBotonModo.setVisibility(View.INVISIBLE);
                break;
            case modoModificar:
                habilitarDeshabilitar(true);
                rellenarCampos();
                this.tvDetallePubsModoBoton.setText(modificar);
                break;
            case modoEliminar:
                habilitarDeshabilitar(false);
                rellenarCampos();
                this.tvDetallePubsModoBoton.setText(eliminar);
                break;
            default:
                    break;
        }
    }

    private void gestionarModoBoton(String modo){

        switch (modo){
            case guardar:
                Toast.makeText(getContext(),"guardar", Toast.LENGTH_LONG).show();
                guardarPub(this.pub);
                break;
            case modificar:
                Toast.makeText(getContext(),"modificar", Toast.LENGTH_LONG).show();
                //modificarPub();
                break;
            case eliminar:
                Toast.makeText(getContext(),"eliminar", Toast.LENGTH_LONG).show();
                //eliminarPub();
                break;
            default:
                break;
        }

    }


    private void habilitarDeshabilitar(boolean accion){
        this.nombrePub.setEnabled(accion);
        this.webPub.setEnabled(accion);
        this.visitasPub.setEnabled(accion);
        this.spinnerEstilos.setEnabled(accion);
        this.mapaPub.setEnabled(accion);
        if (accion == true){
            this.cambiarFotoPub.show();
        }else {
            this.cambiarFotoPub.hide();
        }
    }



    public void seleccionarEstiloPub() {
        switch (this.pub.getEstilo()) {
            case "Rock":
                spinnerEstilos.setSelection(0);
                break;
            case "Reggaeton":
                spinnerEstilos.setSelection(1);
                break;
            case "Clasica":
                spinnerEstilos.setSelection(2);
                break;
            case "Electronica":
                spinnerEstilos.setSelection(3);
                break;
            case "Indie":
                spinnerEstilos.setSelection(4);
                break;
            case "Pop":
                spinnerEstilos.setSelection(5);
                break;
        }
    }

    private void guardarPub(Pub guardarPub){
        Call<Pub> call = pubRest.buscarPorNombreDelPub(this.pub.getNombre());
        call.enqueue(new Callback<Pub>() {
            @Override
            public void onResponse(Call<Pub> call, Response<Pub> response) {
                if (response.isSuccessful()){
                    if (response.code() == 204){

                        Call<Pub> callInsert = pubRest.insertarPub(guardarPub);
                        callInsert.enqueue(new Callback<Pub>() {
                            @Override
                            public void onResponse(Call<Pub> call, Response<Pub> response) {
                                if (response.code() == 200){

                                }
                            }

                            @Override
                            public void onFailure(Call<Pub> call, Throwable t) {

                            }
                        });

                    }else{
                        Toast.makeText(getContext(), "El Pub ya existe",
                                Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Pub> call, Throwable t) {

            }
        });
    }

}
