package com.example.mispubs.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sesion {


    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("id_usuario")
    @Expose
    private Integer id_usuario;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("fecha_inicio")
    @Expose
    private String fecha_inicio;

    @SerializedName("fecha_fin")
    @Expose
    private String fecha_fin;

    public Sesion() {
    }

    public Sesion(Integer id_usuario, String token, String fecha_inicio, String fecha_fin) {
        this.id_usuario = id_usuario;
        this.token = token;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    @Override
    public String toString() {
        return "Sesion{" +
                "id=" + id +
                ", id_usuario=" + id_usuario +
                ", token='" + token + '\'' +
                ", fecha_inicio='" + fecha_inicio + '\'' +
                ", fecha_fin='" + fecha_fin + '\'' +
                '}';
    }
}
