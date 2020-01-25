package com.example.mispubs.ui.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mispubs.R;
import com.google.android.material.snackbar.Snackbar;

public class ActivityLogin extends AppCompatActivity {

    private EditText etLoginEmail;
    private EditText etLoginPasssword;
    private TextView tvLoginRegistro;
    private RelativeLayout relativeLoginBoton;



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
                    Snackbar.make(v,"Botón entrar",Snackbar.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }


        }
    };


}
