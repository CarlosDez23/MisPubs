package com.example.mispubs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.mispubs.Controlador.ControladorBD;
import com.example.mispubs.Controlador.UtilSQL;
import com.example.mispubs.Modelo.Sesion;
import com.example.mispubs.Modelo.Usuario;
import com.example.mispubs.Utilidades.Util;
import com.example.mispubs.ui.Login.ActivityLogin;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                File bd = new File("/data/data/com.example.mispubs/databases/BDConfig");
                if (!bd.exists()) {
                    Intent intent = new Intent(ActivitySplash.this, ActivityLogin.class);
                    startActivity(intent);
                } else {
                    Sesion sesion = UtilSQL.consultarSesion(getApplicationContext());
                    if (sesion == null) {
                        Intent intent = new Intent(ActivitySplash.this, ActivityLogin.class);
                        startActivity(intent);
                    } else {
                        Date fechaActual = new Date();
                        Date fechafin = Util.parseFecha(sesion.getFechafin());
                        int dias = (int) ((fechafin.getTime() - fechaActual.getTime()) / 86400000);
                        if (dias < 0) {
                            Intent intent = new Intent(ActivitySplash.this, ActivityLogin.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(ActivitySplash.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }
                finish();
            }
        }, 3000);//tiempo que debe estar ejecutandose
    }
}
