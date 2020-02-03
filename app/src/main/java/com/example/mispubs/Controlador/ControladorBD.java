package com.example.mispubs.Controlador;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ControladorBD extends SQLiteOpenHelper {

    private final static String CREATE_TABLA_USUARIO =
            "CREATE TABLE USUARIO( "+
                    "id INTEGER, " +
                    "nombre VARCHAR(255) NOT NULL," +
                    "correo VARCHAR(255) NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "imagen BLOB);";

    private final static String CREATE_TABLA_SESION =
            "CREATE TABLE SESION( "+
            "id INTEGER, " +
            "idusuario INTEGER NOT NULL," +
            "token VARCHAR(255) NOT NULL, " +
            "fechainicio VARCHAR(255) NOT NULL, " +
            "fechafin VARCHAR(255) NOT NULL);";

    public ControladorBD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLA_USUARIO);
        db.execSQL(CREATE_TABLA_SESION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
