package com.example.mispubs.REST;

import com.example.mispubs.Modelo.Valoracion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ValoracionRest {

    @GET("valoraciones/{idpub}")
    Call<List<Valoracion>> findValoracionesPub(@Path("idpub") Integer id);

    @POST("valoraciones")
    Call<Valoracion> insertarValoracion(@Body Valoracion valoracion);
}
