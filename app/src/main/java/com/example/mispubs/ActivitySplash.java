package com.example.mispubs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.mispubs.Controlador.ControladorBD;
import com.example.mispubs.Controlador.UtilSQL;
import com.example.mispubs.Modelo.Sesion;
import com.example.mispubs.Modelo.Usuario;
import com.example.mispubs.REST.APIUtils;
import com.example.mispubs.REST.SesionRest;
import com.example.mispubs.Utilidades.Util;
import com.example.mispubs.ui.Login.ActivityLogin;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySplash extends AppCompatActivity {

    private Sesion sesion;
    private SesionRest sesionRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        if (Util.isOnline(getApplicationContext())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gestionarInicioApp();
                    finish();
                }
            }, 3000);//tiempo que debe estar ejecutandose

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Debes tener una conexión a internet " +
                            "para empezar ", Toast.LENGTH_LONG).show();
                    finish();
                }
            }, 3000);//tiempo que debe estar ejecutandose
        }
    }

    private void gestionarInicioApp() {
        File bd = new File("/data/data/com.example.mispubs/databases/BDConfig");
        if (!bd.exists()) {
            irLogin();
        } else {
            sesion = UtilSQL.consultarSesion(getApplicationContext());
            if (sesion == null) {
                irLogin();
            } else {
                Date fechaActual = new Date();
                Date fechafin = Util.parseFecha(sesion.getFechafin());
                int dias = (int) ((fechafin.getTime() - fechaActual.getTime()) / 86400000);
                if (dias < 0) {
                    UtilSQL.eliminarSesionLocal(sesion.getId(), getApplicationContext());
                    eliminarSesion(sesion.getId());
                    irLogin();
                } else {
                    irMain();
                }
            }
        }
    }

    private void irLogin() {
        Intent intent = new Intent(ActivitySplash.this, ActivityLogin.class);
        startActivity(intent);
    }

    private void irMain() {
        Intent intent = new Intent(ActivitySplash.this, MainActivity.class);
        startActivity(intent);
    }

    private void eliminarSesion(int id) {
        sesionRest = APIUtils.getServiceSesiones();
        Call<Sesion> call = sesionRest.eliminarSesion(id);
        call.enqueue(new Callback<Sesion>() {
            @Override
            public void onResponse(Call<Sesion> call, Response<Sesion> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Toast.makeText(getApplicationContext(), " Sesión expirada",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Sesion> call, Throwable t) {

            }
        });
    }
}
