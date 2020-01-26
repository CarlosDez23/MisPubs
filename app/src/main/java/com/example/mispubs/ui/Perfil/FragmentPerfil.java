package com.example.mispubs.ui.Perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mispubs.Modelo.Usuario;
import com.example.mispubs.R;

public class FragmentPerfil extends Fragment {

    private Usuario usuario;
    private TextView tvPerfilNombre;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        usuario = (Usuario) getActivity().getIntent().getExtras().getSerializable("usuario");

        llamarVistas();

    }

    private void llamarVistas(){

        this.tvPerfilNombre = getView().findViewById(R.id.name);
        this.tvPerfilNombre.setText(usuario.getNombre());
    }
}