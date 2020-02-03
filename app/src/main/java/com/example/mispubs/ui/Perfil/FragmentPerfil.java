package com.example.mispubs.ui.Perfil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mispubs.Controlador.UtilSQL;
import com.example.mispubs.MainActivity;
import com.example.mispubs.Modelo.Usuario;
import com.example.mispubs.R;
import com.example.mispubs.REST.APIUtils;
import com.example.mispubs.REST.SesionRest;
import com.example.mispubs.REST.UsuarioRest;
import com.example.mispubs.Utilidades.Util;
import com.example.mispubs.ui.Login.ActivityLogin;
import com.example.mispubs.ui.Mapas.FragmentMapas;
import com.example.mispubs.ui.Pubs.FragmentDetallePubs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPerfil extends Fragment {

    //Usuario logeado
    private Usuario usuario;
    private Usuario actualizarUsuario;

    //Para consumir el servicio REST
    private UsuarioRest usuarioRest;
    private SesionRest sesionRest;
    private FragmentManager fm;

    //Para la interfaz
    private EditText etPerfilNombre, etPerfilCorreo, etPerfilPassword;
    private RelativeLayout btnPerfilEliminarUsuario, btnPerfilCerrarSesion;
    private ImageView imageViewEdit, imageViewModificar, ivImagenPerfil;

    //Para la gestión de imágenes
    private static final int GALERIA = 1;
    private static final int CAMARA = 2;

    public FragmentPerfil getFragment() {
        return this;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Cojemos el objeto usuario
        fm = getFragmentManager();
        usuario = MainActivity.getUsuario();
        usuarioRest = APIUtils.getService();
        sesionRest = APIUtils.getServiceSesiones();
        llamarVistas();

    }

    private void llamarVistas() {

        this.etPerfilNombre = getView().findViewById(R.id.etPerfilNombre);
        this.etPerfilNombre.setText(usuario.getNombre());
        this.etPerfilCorreo = getView().findViewById(R.id.etPerfilCorreo);
        this.etPerfilCorreo.setText(usuario.getCorreo());
        this.etPerfilPassword = getView().findViewById(R.id.etPerfilPassword);
        this.etPerfilPassword.setText("PASSWORD");
        this.btnPerfilEliminarUsuario = getView().findViewById(R.id.btnPerfilEliminarUsuario);
        this.btnPerfilEliminarUsuario.setOnClickListener(listenerBotones);
        this.btnPerfilCerrarSesion = getView().findViewById(R.id.btnPerfilCerrarSesion);
        this.btnPerfilCerrarSesion.setOnClickListener(listenerBotones);
        this.imageViewEdit = getView().findViewById(R.id.edit);
        this.imageViewEdit.setOnClickListener(listenerBotones);
        this.imageViewModificar = getView().findViewById(R.id.ivPerfilModificar);
        this.imageViewModificar.setOnClickListener(listenerBotones);
        this.ivImagenPerfil = getView().findViewById(R.id.profile);
        this.ivImagenPerfil.setEnabled(false);

        //Para la gestión de imágenes
        if (usuario.getImagen() != null) {
            this.ivImagenPerfil.setImageBitmap(Util.base64ToBitmap(usuario.getImagen()));
        } else {
            this.ivImagenPerfil.setImageResource(R.drawable.man);
        }

        ivImagenPerfil.setOnClickListener(listenerBotones);

    }


    private View.OnClickListener listenerBotones = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnPerfilEliminarUsuario:
                    System.out.println(usuario.toString());
                    mostrarDialogoEliminar();
                    break;
                case R.id.edit:
                    //ponemos invisible el boton pulsado, mostramos el de modificar y habilitamos los campos
                    //modoActualizar(true);
                    imageViewEdit.setVisibility(View.INVISIBLE);
                    imageViewModificar.setVisibility(View.VISIBLE);
                    ivImagenPerfil.setEnabled(true);
                    ActualizarDialog dialog = new ActualizarDialog(getFragment(), usuario);
                    dialog.show(fm, "Actualizar Usuario");
                    ivImagenPerfil.setEnabled(true);


                    break;
                case R.id.ivPerfilModificar:

                    cambiarImagen();

                    //ponemos invisible el boton pulsado, mostramos el de editar y deshabilitamos los campos
                    imageViewModificar.setVisibility(View.INVISIBLE);
                    imageViewEdit.setVisibility(View.VISIBLE);
                    ivImagenPerfil.setEnabled(true);

                    if (Util.isOnline(getContext())) {
                        modificarUsuario(actualizarUsuario);
                    } else {
                        Toast.makeText(getContext(), "Debes activar una conexión a internet ", Toast.LENGTH_LONG).show();
                    }

                    break;
                case R.id.profile:
                    gestionImagen();
                    break;
                case R.id.btnPerfilCerrarSesion:
                    mostrarDialogoCerrarSesion();
                default:
                    break;
            }
        }
    };

    private void actualizarSesion(){
        Log.e("Entroooo", actualizarUsuario.getNombre() + usuario.getNombre());
        etPerfilNombre.setText(actualizarUsuario.getNombre());
        etPerfilCorreo.setText(actualizarUsuario.getCorreo());
        Bitmap img = ((BitmapDrawable) ivImagenPerfil.getDrawable()).getBitmap();
        Bitmap comprimido = Util.comprimirImagen(img);
        String convertida = Util.bitmapToBase64(comprimido);
        usuario.setImagen(convertida);
    }


    /**
     * Metodo encargado de crear un usuario, para modificar el usuario de la sesion en la base de datos y en la sesion
     */
    public void crearActualizarUsuario(String nombre, String password) {

        actualizarUsuario = new Usuario(nombre, usuario.getCorreo(), password, "imagen");
        actualizarUsuario.setId(usuario.getId());
    }

    /**
     * Para modificar la imagen en el nuevo usuario
     */
    private void cambiarImagen() {

        Bitmap img = ((BitmapDrawable) ivImagenPerfil.getDrawable()).getBitmap();
        Bitmap comprimido = Util.comprimirImagen(img);
        String convertida = Util.bitmapToBase64(comprimido);
        actualizarUsuario.setImagen(convertida);
    }


   /* private void modoActualizar(boolean modo) {

        etPerfilNombre.setEnabled(modo);
        etPerfilCorreo.setEnabled(modo);
        etPerfilPassword.setEnabled(modo);

        if (modo) {
            imageViewEdit.setVisibility(View.INVISIBLE);
            imageViewModificar.setVisibility(View.VISIBLE);
            ivImagenPerfil.setEnabled(true);
            this.etPerfilPassword.setText(this.usuario.getPassword());
        } else {
            imageViewModificar.setVisibility(View.INVISIBLE);
            imageViewEdit.setVisibility(View.VISIBLE);
            etPerfilPassword.setText("PASSWORD");
        }
    }*/

    private void mostrarDialogoEliminar() {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getContext());
        deleteDialog.setTitle(usuario.getNombre() + ", ¿Estás seguro de querer eliminar tu cuenta?");
        String[] deleteDialogitems = {"Sí", "No"};
        deleteDialog.setItems(deleteDialogitems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (Util.isOnline(getContext())) {
                            eliminarUsuario();
                        } else {
                            Toast.makeText(getContext(), "Debes activar una conexión a internet ", Toast.LENGTH_LONG).show();
                        }
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

    private void mostrarDialogoCerrarSesion() {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getContext());
        deleteDialog.setTitle(usuario.getNombre() + ", ¿Estás seguro de querer cerrar sesion?");
        String[] deleteDialogitems = {"Sí", "No"};
        deleteDialog.setItems(deleteDialogitems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        cerrarSesion();
                        break;
                    case 1:
                        Toast.makeText(getContext(), "De acuerdo",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
        deleteDialog.show();
    }


    private void cerrarSesion() {
        int id = UtilSQL.consultarSesion(getContext()).getId();
        //Borramos la sesión local
        UtilSQL.eliminarSesionLocal(id,getContext());
        startActivity(new Intent(getActivity(), ActivityLogin.class));
        getActivity().finish();
    }

    private void gestionImagen() {
        AlertDialog.Builder fotoDialogo = new AlertDialog.Builder(getContext());
        fotoDialogo.setTitle("Elige un método de entrada");
        String[] fotoDialogoItems = {
                "Seleccionar fotografía de galería",
                "Capturar fotografía desde la cámara"};
        fotoDialogo.setItems(fotoDialogoItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                elegirFotoGaleria();
                                break;
                            case 1:
                                tomarFotoCamara();
                                break;
                        }
                    }
                });
        fotoDialogo.show();
    }

    private void elegirFotoGaleria() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALERIA);
    }


    private void tomarFotoCamara() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMARA);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALERIA) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    this.ivImagenPerfil.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Snackbar.make(getView(), "Fallo en la galería", Snackbar.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == CAMARA) {
            Bitmap thumbnail = null;
            try {
                thumbnail = (Bitmap) data.getExtras().get("data");
                this.ivImagenPerfil.setImageBitmap(thumbnail);
            } catch (Exception e) {
                Snackbar.make(getView(), "Fallo en la cámara", Snackbar.LENGTH_LONG).show();
            }
        }
    }


    /**
     * Modificamos el usuario, asegurandonos de que no exista en la base de dato el mismo nombre de usuario
     *
     * @param user
     */
    private void modificarUsuario(Usuario user) {

        Call<Usuario> callupdate = usuarioRest.modificarUsuario(user.getId(), user, user.getNombre());
        callupdate.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                System.out.println("Modificando");
                if (response.code() == 200) {
                    System.out.println("si");
                    actualizarSesion();
                    Toast.makeText(getContext(), "Actualizando Cambios",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }


    /**
     * Eliminamos un usuario, el servicio ya se encarga de comprobar si existe
     */
    private void eliminarUsuario() {
        Call<Usuario> call = usuarioRest.borrarUsuario(usuario.getId());
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                System.out.println("Entrando");
                if (response.isSuccessful()) {
                    System.out.println("OK");
                    if (response.code() == 200) {
                        System.out.println("Eliminado");
                        Toast.makeText(getContext(), response.body().getNombre() + " eliminado",
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


}