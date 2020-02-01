package com.example.mispubs.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sesion {


    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("idusuario")
    @Expose
    private Integer idusuario;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("fechainicio")
    @Expose
    private String fechainicio;

    @SerializedName("fechafin")
    @Expose
    private String fechafin;

    public Sesion() {
    }

    public Sesion(Integer idusuario, String token, String fechainicio, String fechafin) {
        this.idusuario = idusuario;
        this.token = token;
        this.fechainicio = fechainicio;
        this.fechafin = fechafin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Integer idusuario) {
        this.idusuario = idusuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(String fechainicio) {
        this.fechainicio = fechainicio;
    }

    public String getFechafin() {
        return fechafin;
    }

    public void setFechafin(String fechafin) {
        this.fechafin = fechafin;
    }

    @Override
    public String toString() {
        return "Sesion{" +
                "id=" + id +
                ", idusuario=" + idusuario +
                ", token='" + token + '\'' +
                ", fechainicio='" + fechainicio + '\'' +
                ", fechafin='" + fechafin + '\'' +
                '}';
    }
}
