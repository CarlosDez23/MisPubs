package com.example.mispubs.Controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;

import com.example.mispubs.Modelo.Sesion;
import com.example.mispubs.Modelo.Usuario;
import com.example.mispubs.R;
import com.example.mispubs.Utilidades.Util;

public class UtilSQL {

    private static final String NOMBRE_BD = "BDConfig";
    private static final int VERSION_BD = 1;

    public static Sesion consultarSesion(Context context) {
        Sesion aux = null;
        ControladorBD controlador = new ControladorBD(context, NOMBRE_BD, null, VERSION_BD);
        SQLiteDatabase bd = controlador.getReadableDatabase();
        String[] campos = new String[]{"id", "idusuario", "token", "fechainicio", "fechafin"};
        Cursor c = bd.query("SESION", campos, null, null, null, null, null);
        if (c.moveToFirst()) {
            aux = new Sesion(c.getInt(1), c.getString(2), c.getString(3), c.getString(4));
            aux.setId(c.getInt(0));
        }
        bd.close();
        controlador.close();
        return aux;
    }

    public static Usuario consultarUsuarioLocal(Context context) {
        Usuario aux = null;
        ControladorBD controlador = new ControladorBD(context, NOMBRE_BD, null, VERSION_BD);
        SQLiteDatabase bd = controlador.getReadableDatabase();
        String[] campos = new String[]{"id", "nombre", "correo", "password", "imagen"};
        Cursor c = bd.query("USUARIO", campos, null, null, null, null, null);
        if (c.moveToFirst()) {
            aux = new Usuario(c.getString(1), c.getString(2),
                    c.getString(3), c.getString(4));
            aux.setId(c.getInt(0));
        }
        bd.close();
        controlador.close();
        return aux;
    }

    public static void insertarUsuarioLocal(Usuario u, Context context) {
        ControladorBD controlador = new ControladorBD(context, NOMBRE_BD, null, VERSION_BD);
        SQLiteDatabase bd = controlador.getWritableDatabase();
        ContentValues contenido = new ContentValues();
        contenido.put("id", u.getId());
        contenido.put("nombre", u.getNombre());
        contenido.put("correo", u.getCorreo());
        contenido.put("password", u.getPassword());
        if (u.getImagen() == null) {
            contenido.put("imagen", Util.bitmapToBase64
                    (BitmapFactory.decodeResource(context.getResources(), R.drawable.man)));
        } else {
            contenido.put("imagen", u.getImagen());
        }
        bd.insert("USUARIO", null, contenido);
        bd.close();
        controlador.close();
    }

    public static void insertarSesionLocal(Sesion s, Context context) {
        ControladorBD controlador = new ControladorBD(context, NOMBRE_BD, null, VERSION_BD);
        SQLiteDatabase bd = controlador.getWritableDatabase();
        ContentValues contenido = new ContentValues();
        contenido.put("id", s.getId());
        contenido.put("idusuario", s.getIdusuario());
        contenido.put("token", s.getToken());
        contenido.put("fechainicio", s.getFechainicio());
        contenido.put("fechafin", s.getFechafin());
        bd.insert("SESION", null, contenido);
        bd.close();
        controlador.close();
    }

    public static void eliminarSesionLocal(int idSesion, Context context) {
        ControladorBD controlador = new ControladorBD(context, NOMBRE_BD, null, VERSION_BD);
        SQLiteDatabase bd = controlador.getWritableDatabase();
        bd.delete("SESION", "id=" + idSesion, null);
        bd.close();
        controlador.close();
    }

    public static void eliminarUsuarioLocal(int idUsuario, Context context) {
        ControladorBD controlador = new ControladorBD(context, NOMBRE_BD, null, VERSION_BD);
        SQLiteDatabase bd = controlador.getWritableDatabase();
        bd.delete("USUARIO", "id=" + idUsuario, null);
        bd.close();
        controlador.close();
    }

    public static void actualizarUsuarioLocal(Usuario u, Context context) {
        ControladorBD controlador = new ControladorBD(context, NOMBRE_BD, null, VERSION_BD);
        SQLiteDatabase bd = controlador.getWritableDatabase();
        ContentValues contenido = new ContentValues();
        System.out.println(u.toString());
        contenido.put("id", u.getId());
        contenido.put("nombre", u.getNombre());
        contenido.put("correo", u.getCorreo());
        contenido.put("password", u.getPassword());
        if (u.getImagen() == null) {
            contenido.put("imagen", Util.bitmapToBase64
                    (BitmapFactory.decodeResource(context.getResources(), R.drawable.man)));
        } else {
            contenido.put("imagen", u.getImagen());
        }
        bd.update("USUARIO", contenido, "id="+u.getId(), null);
        bd.close();
        controlador.close();
    }
}
