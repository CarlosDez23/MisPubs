package com.example.mispubs.ui.Login;



import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mispubs.Controlador.ControladorBD;
import com.example.mispubs.Controlador.UtilSQL;
import com.example.mispubs.MainActivity;
import com.example.mispubs.Modelo.Sesion;
import com.example.mispubs.Modelo.Usuario;
import com.example.mispubs.R;
import com.example.mispubs.REST.APIUtils;
import com.example.mispubs.REST.SesionRest;
import com.example.mispubs.REST.UsuarioRest;
import com.example.mispubs.Utilidades.Util;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLogin extends Fragment {

    private EditText etLoginEmail;
    private EditText etLoginPasssword;
    private TextView tvLoginRegistro;
    private RelativeLayout relativeLoginBoton;

    //Para utilizarlo en el REST
    private UsuarioRest usuarioRest;
    private SesionRest sesionRest;

    public static FragmentLogin newInstance() {
        return new FragmentLogin();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        llamarVistas();
        if (Util.isOnline(getContext())){
            usuarioRest = APIUtils.getService();
            sesionRest = APIUtils.getServiceSesiones();
        }else{
            Toast.makeText(getContext(),"Debes activar una conexión a internet", Toast.LENGTH_LONG).show();
        }


    }



    private void llamarVistas(){

        this.etLoginEmail = getView().findViewById(R.id.etLoginEmail);
        this.etLoginPasssword = getView().findViewById(R.id.etLoginPassword);
        this.tvLoginRegistro = getView().findViewById(R.id.tvLoginRegistro);
        this.relativeLoginBoton = getView(). findViewById(R.id.relativeLoginBoton);

        tvLoginRegistro.setOnClickListener(listenerBotones);
        relativeLoginBoton.setOnClickListener(listenerBotones);

    }

    private View.OnClickListener listenerBotones = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.tvLoginRegistro:
                    getFragmentManager().beginTransaction().add(R.id.fragment, new FragmentRegistro()).addToBackStack(null).commit();
                    break;
                case R.id.relativeLoginBoton:
                    //Snackbar.make(v,"Botón entrar",Snackbar.LENGTH_LONG).show();
                    String correo = etLoginEmail.getText().toString();
                    String password = etLoginPasssword.getText().toString();
                    System.out.println(correo+" "+password);
                    if (correo.isEmpty() || password.isEmpty()){
                        Toast.makeText(getContext(), "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
                    }else{
                        relativeLoginBoton.setClickable(false);
                        byte[]datos = password.getBytes();
                        String cifrada = Util.resumirPassword(datos);
                        if (Util.isOnline(getContext())){
                            comprobarUsuario(correo,cifrada);
                        }else{
                            Toast.makeText(getContext(), "Debes activar una conexión a internet ", Toast.LENGTH_LONG).show();
                            relativeLoginBoton.setClickable(true);
                        }


                    }

                    break;
                default:
                    break;
            }
        }
    };

    private void comprobarUsuario(String correo, String password){

        Call<Usuario> call = usuarioRest.buscarPorCorreoPass(correo,password);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()){

                    if (response.code() == 200){
                        Usuario usuario = response.body();
                        UtilSQL.insertarUsuarioLocal(usuario,getContext());
                        String[] fechas = Util.gestionFechas();
                        Sesion sesion = new Sesion (usuario.getId(),Util.generarToken(usuario.getNombre()),fechas[0],fechas[1]);
                        Call<Sesion> call2 = sesionRest.nuevaSesion(sesion);
                        call2.enqueue(new Callback<Sesion>() {
                            @Override
                            public void onResponse(Call<Sesion> call, Response<Sesion> response) {
                                if (response.isSuccessful()){
                                    if (response.code() == 200){
                                        UtilSQL.insertarSesionLocal(response.body(),getContext());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Sesion> call, Throwable t) {

                            }
                        });
                        Intent i = new Intent(getActivity(),MainActivity.class);
                        i.putExtra("usuario", usuario);
                        startActivity(i);
                        getActivity().finish();
                    }else{
                        Snackbar.make(getView(), "NO REGISTRADO", Snackbar.LENGTH_LONG).show();
                        relativeLoginBoton.setClickable(true);
                    }
                }else {
                    Toast.makeText(getContext(), "No se obtuvo respuesta", Toast.LENGTH_SHORT).show();
                    relativeLoginBoton.setClickable(true);
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(getContext(), "Servicio no activo", Toast.LENGTH_SHORT).show();
                Log.e("ERROR: ", t.getMessage());
                relativeLoginBoton.setClickable(true);
            }
        });
    }
}
