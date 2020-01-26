package com.example.mispubs.ui.Perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mispubs.Modelo.Usuario;
import com.example.mispubs.R;
import com.example.mispubs.REST.APIUtils;
import com.example.mispubs.REST.UsuarioRest;
import com.example.mispubs.ui.Login.ActivityLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPerfil extends Fragment {

    //Usuario logeado
    private Usuario usuario;

    //Para consumir el servicio REST
    private UsuarioRest usuarioRest;

    //Para la interfaz
    private TextView tvPerfilNombre;
    private RelativeLayout btnPerfilEliminarUsuario;

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
        usuarioRest = APIUtils.getService();

    }

    private void llamarVistas(){

        this.tvPerfilNombre = getView().findViewById(R.id.name);
        this.tvPerfilNombre.setText(usuario.getNombre());
        this.btnPerfilEliminarUsuario = getView().findViewById(R.id.btnPerfilEliminarUsuario);
        this.btnPerfilEliminarUsuario.setOnClickListener(listenerBotones);
    }


    private View.OnClickListener listenerBotones = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.btnPerfilEliminarUsuario:
                    System.out.println(usuario.toString());
                    eliminarUsuario();
                    break;
                default:
                    break;
            }

        }
    };

    /**
     * Eliminamos un usuario, el servicio ya se encarga de comprobar si existe
     */
    private void eliminarUsuario(){
        Call<Usuario> call = usuarioRest.borrarUsuario(usuario.getId());
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                System.out.println("Entrando");
                if (response.isSuccessful()){
                    System.out.println("OK");
                    if (response.code() == 200){
                        System.out.println("Eliminado");
                        Toast.makeText(getContext(),response.body().getNombre()+" eliminado",
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), ActivityLogin.class));
                    }
                }

            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                System.out.println("Mal");
            }
        });

    }
}