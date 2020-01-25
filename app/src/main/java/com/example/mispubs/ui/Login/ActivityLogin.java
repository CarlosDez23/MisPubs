package com.example.mispubs.ui.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mispubs.MainActivity;
import com.example.mispubs.Modelo.Usuario;
import com.example.mispubs.R;
import com.example.mispubs.REST.UsuarioRest;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity {

    private EditText etLoginEmail;
    private EditText etLoginPasssword;
    private TextView tvLoginRegistro;
    private RelativeLayout relativeLoginBoton;

    //Para utilizarlo en el REST
    private UsuarioRest usuarioRest;


    private ActivityLogin getActividad(){
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Lo ponemos a pantalla completa
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();

        llamarVistas();
    }


    private void llamarVistas(){

        this.etLoginEmail = findViewById(R.id.etLoginEmail);
        this.etLoginPasssword = findViewById(R.id.etLoginPassword);
        this.tvLoginRegistro = findViewById(R.id.tvLoginRegistro);
        this.relativeLoginBoton = findViewById(R.id.relativeLoginBoton);

        tvLoginRegistro.setOnClickListener(listenerBotones);
        relativeLoginBoton.setOnClickListener(listenerBotones);

    }

    private View.OnClickListener listenerBotones = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.tvLoginRegistro:
                    Snackbar.make(v, "Registro", Snackbar.LENGTH_LONG).show();
                    break;
                case R.id.relativeLoginBoton:
                    //Snackbar.make(v,"Botón entrar",Snackbar.LENGTH_LONG).show();
                    comprobarUsuario();
                    break;
                default:
                    break;
            }
        }
    };

    private void comprobarUsuario(){

        Call<Usuario> call = usuarioRest.buscarPorCorreoPass(etLoginEmail.getText().toString(),etLoginPasssword.getText().toString());

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()){
                    if (response.code() == 200){
                        Snackbar.make(getActividad().getWindow().getDecorView().getRootView(), "TODO OK", Snackbar.LENGTH_LONG).show();
                        startActivity(new Intent(getActividad(), MainActivity.class));
                    }else{
                        Snackbar.make(getActividad().getWindow().getDecorView().getRootView(), "NO REGISTRADO", Snackbar.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }


}
