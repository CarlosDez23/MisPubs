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

import java.util.ArrayList;

public class FragmentDetallePubs extends Fragment {

    private Pub pub;
    private Spinner spinnerEstilos;
    private ArrayList<String> listaEstilos = new ArrayList<String>();





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
    }

}
