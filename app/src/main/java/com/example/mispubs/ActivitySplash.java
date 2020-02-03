package com.example.mispubs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.mispubs.Controlador.ControladorBD;
import com.example.mispubs.Modelo.Sesion;
import com.example.mispubs.Modelo.Usuario;
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
                File bd = new File ("/data/data/com.example.mispubs/databases/BDConfig");
                if (!bd.exists()){
                    Intent intent = new Intent(ActivitySplash.this, ActivityLogin.class);
                    startActivity(intent);
                }else{
                    int idUsuario = consultarUsuarioLocal();
                    System.out.println(idUsuario);
                    Sesion sesion = consultarSesion();
                    Date fechainicio = parseFecha(sesion.getFechainicio());
                    Date fechafin = parseFecha(sesion.getFechafin());
                    int dias=(int) ((fechainicio.getTime()-fechafin.getTime())/86400000);
                    if (dias >= 7){
                        Intent intent = new Intent(ActivitySplash.this, ActivityLogin.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(ActivitySplash.this, MainActivity.class);
                        startActivity(intent);
                    }


                }

                finish();
            }
        },3000);//tiempo que debe estar ejecutandose
    }

    /**
     * Consultamos la id del usuario que tenemos almacenado localmente
     * @return
     */
    private int consultarUsuarioLocal(){
        int id = 0;
        ControladorBD controlador = new ControladorBD(this.getApplicationContext(), "BDConfig", null, 1);
        SQLiteDatabase bd = controlador.getReadableDatabase();
        String[]campos = new String[] {"id"};
        Cursor c = bd.query("USUARIO", campos, null, null, null, null,null);
        if (c.moveToFirst()){
            id = c.getInt(0);
        }
        bd.close();
        controlador.close();
        return id;
    }

    private Sesion consultarSesion(){
        Sesion aux = null;
        ControladorBD controlador = new ControladorBD(this.getApplicationContext(), "BDConfig", null, 1);
        SQLiteDatabase bd = controlador.getReadableDatabase();
        String[]campos = new String[] {"id", "idusuario", "token","fechainicio","fechafin"};
        Cursor c = bd.query("SESION", campos, null, null, null, null,null);
        if (c.moveToFirst()){
            aux = new Sesion(c.getInt(1), c.getString(2),c.getString(3),c.getString(4));
            aux.setId(c.getInt(0));
        }
        return aux;
    }

    private Date parseFecha(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaDate = null;
        try {
            fechaDate = formato.parse(fecha);
        } catch (ParseException ex) {
            System.out.println(ex);
        }
        return fechaDate;
    }
}
