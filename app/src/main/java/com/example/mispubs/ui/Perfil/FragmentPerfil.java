package com.example.mispubs.ui.Perfil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mispubs.MainActivity;
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
    private EditText etPerfilNombre, etPerfilCorreo, etPerfilPassword;
    private RelativeLayout btnPerfilEliminarUsuario;
    private ImageView imageViewEdit, imageViewModificar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Cojemos el objeto usuario
        usuario = MainActivity.getUsuario();
        usuarioRest = APIUtils.getService();
        llamarVistas();

    }

    private void llamarVistas(){

        this.etPerfilNombre = getView().findViewById(R.id.etPerfilNombre);
        this.etPerfilNombre.setText(usuario.getNombre());
        this.etPerfilCorreo = getView().findViewById(R.id.etPerfilCorreo);
        this.etPerfilCorreo.setText(usuario.getCorreo());
        this.etPerfilPassword = getView().findViewById(R.id.etPerfilPassword);
        this.etPerfilPassword.setText(usuario.getPassword());
        this.btnPerfilEliminarUsuario = getView().findViewById(R.id.btnPerfilEliminarUsuario);
        this.btnPerfilEliminarUsuario.setOnClickListener(listenerBotones);
        this.imageViewEdit = getView().findViewById(R.id.edit);
        this.imageViewEdit.setOnClickListener(listenerBotones);
        this.imageViewModificar = getView().findViewById(R.id.ivPerfilModificar);
        this.imageViewModificar.setOnClickListener(listenerBotones);

    }


    private View.OnClickListener listenerBotones = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.btnPerfilEliminarUsuario:
                    System.out.println(usuario.toString());
                    mostrarDialogoEliminar();
                    break;
                case R.id.edit:
                    //ponemos invisible el boton pulsado, mostramos el de modificar y habilitamos los campos
                    modoActualizar(true);
                    break;
                case R.id.ivPerfilModificar:
                    //ponemos invisible el boton pulsado, mostramos el de editar y deshabilitamos los campos
                    modoActualizar(false);
                    crearActualizarUsuario();
                default:
                    break;
            }
        }
    };

    /**
     * Metodo encargado de crear un usuario, para modificar el usuario de la sesion en la base de datos y en la sesion
     */
    private void crearActualizarUsuario(){
        String
                nombre = etPerfilNombre.getText().toString(),
                email = etPerfilCorreo.getText().toString(),
                password = etPerfilPassword.getText().toString();

        Usuario u = new Usuario(nombre, email, password, null);
        u.setId(usuario.getId());

        usuario.setCorreo(u.getCorreo());
        usuario.setNombre(u.getNombre());
        usuario.setPassword(u.getPassword());

        modificarUsuario(u);

    }

    /**
     * Modo que utilizaremos para rellenar los campos del perfil de usuario y asi modificarlo
     * @param modo
     */
    private void modoActualizar( boolean modo){

        etPerfilNombre.setEnabled(modo);
        etPerfilCorreo.setEnabled(modo);
        etPerfilPassword.setEnabled(modo);

        if (modo){
            imageViewEdit.setVisibility(View.INVISIBLE);
            imageViewModificar.setVisibility(View.VISIBLE);
        }else{
            imageViewModificar.setVisibility(View.INVISIBLE);
            imageViewEdit.setVisibility(View.VISIBLE);
        }
    }

    private void mostrarDialogoEliminar(){
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getContext());
        deleteDialog.setTitle(usuario.getNombre()+", ¿estás seguro de querer eliminar tu cuenta?");
        String[] deleteDialogitems = {"Sí", "No"};
        deleteDialog.setItems(deleteDialogitems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        eliminarUsuario();
                        break;
                    case 1:
                        Toast.makeText(getContext(), "Cuenta no eliminada",
                                Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        });
        deleteDialog.show();
    }

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
                        getActivity().finish();
                    }
                }

            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                System.out.println("Mal");
            }
        });
    }

    /**
     * Modificamos el usuario
     * @param user
     */
    private void modificarUsuario(Usuario user){
        Call<Usuario> call = usuarioRest.modificarUsuario(user.getId(), user);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                System.out.println("Modificando");
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "Usuario actualizado",
                            Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }
}