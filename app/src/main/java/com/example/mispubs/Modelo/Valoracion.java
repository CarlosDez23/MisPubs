package com.example.mispubs.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Valoracion {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("idusuario")
    @Expose
    private Integer idusuario;

    @SerializedName("idpub")
    @Expose
    private Integer idpub;

    @SerializedName("valoracion")
    @Expose
    private Integer valoracion;

    @SerializedName("detalle")
    @Expose
    private String detalle;

    public Valoracion() {
    }

    public Valoracion(Integer idusuario, Integer idpub, Integer valoracion, String detalle) {
        this.idusuario = idusuario;
        this.idpub = idpub;
        this.valoracion = valoracion;
        this.detalle = detalle;
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

    public Integer getIdpub() {
        return idpub;
    }

    public void setIdpub(Integer idpub) {
        this.idpub = idpub;
    }

    public Integer getValoracion() {
        return valoracion;
    }

    public void setValoracion(Integer valoracion) {
        this.valoracion = valoracion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return "Valoracion{" +
                "id=" + id +
                ", idusuario=" + idusuario +
                ", idpub=" + idpub +
                ", valoracion=" + valoracion +
                ", detalle='" + detalle + '\'' +
                '}';
    }
}
