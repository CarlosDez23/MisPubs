package com.example.mispubs.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pub {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("latitud")
    @Expose
    private Long latitud;

    @SerializedName("longitud")
    @Expose
    private Long longitud;

    @SerializedName("estilo")
    @Expose
    private String estilo;

    @SerializedName("visitas")
    @Expose
    private Integer visitas;

    @SerializedName("web")
    @Expose
    private String web;

    @SerializedName("imagen")
    @Expose
    private String imagen;

    public Pub() {
    }

    public Pub(String nombre, Long latitud, Long longitud, String estilo, Integer visitas, String web, String imagen) {
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estilo = estilo;
        this.visitas = visitas;
        this.web = web;
        this.imagen = imagen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getLatitud() {
        return latitud;
    }

    public void setLatitud(Long latitud) {
        this.latitud = latitud;
    }

    public Long getLongitud() {
        return longitud;
    }

    public void setLongitud(Long longitud) {
        this.longitud = longitud;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public Integer getVisitas() {
        return visitas;
    }

    public void setVisitas(Integer visitas) {
        this.visitas = visitas;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Pub{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", estilo='" + estilo + '\'' +
                ", visitas=" + visitas +
                ", web='" + web + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}
