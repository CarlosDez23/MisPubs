package com.example.mispubs.ui.Pubs;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.mispubs.Modelo.Pub;
import com.example.mispubs.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class FragmentDetallePubs extends Fragment {

    private Pub pub;
    private int modo;
    private final int
            modoNuevo = 0,
            modoVer = 1,
            modoModificar = 2,
            modoBorrar = 3;
    private Spinner spinnerEstilos;
    private ArrayList<String> listaEstilos = new ArrayList<String>();
    private TextInputLayout
            nombrePub,
            webPub,
            visitasPub;
    private FloatingActionButton cambiarFotoPub, mapaPub;


    public FragmentDetallePubs() {
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
        return inflater.inflate(R.layout.fragment_detalle_pubs_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        llamarVistas();

        gestionarModo(this.modo);

    }

    private void llamarVistas(){
        this.nombrePub = getView().findViewById(R.id.tvDetallePubsNombre);
        this.webPub = getView().findViewById(R.id.tvDetallePubsNombre);
        this.visitasPub = getView().findViewById(R.id.tvDetallePubsNombre);
        this.cambiarFotoPub = getView().findViewById(R.id.fabDetallePubsCamara);
        this.mapaPub = getView().findViewById(R.id.fabDetallePubsMapa);

    }

    private void rellenarCampos(){
        this.nombrePub.getEditText().setText(this.pub.getNombre());
        this.webPub.getEditText().setText(this.pub.getWeb());
        this.visitasPub.getEditText().setText(this.pub.getVisitas());
    }

    private void gestionarModo(int modo){

        switch (modo){
            case modoNuevo:
                nuevoPub();
                break;
            case modoVer:
                verPub();
                rellenarCampos();
                break;
            case modoModificar:
                modificarPub();
                rellenarCampos();
                break;
            case modoBorrar:
                borrarPub();
                rellenarCampos();
                break;
            default:
                    break;
        }

    }

    private void nuevoPub(){
    }

    private void verPub(){
        this.nombrePub.setEnabled(false);
        this.webPub.setEnabled(false);
        this.visitasPub.setEnabled(false);
    }

    private void modificarPub(){

    }

    private void borrarPub(){

    }

}
