package com.example.mispubs.REST;

import com.example.mispubs.Modelo.Sesion;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SesionRest {

    @GET("sesiones/{idusuario}")
    Call<Sesion> buscarSesion(@Path("idusuario") Integer id_usuario);

    @POST("sesiones/nueva")
    Call<Sesion> nuevaSesion(@Body Sesion sesion);

    @DELETE("sesiones/cerrar/{id}")
    Call<Sesion> eliminarSesion(@Path("id") Integer id);


}
