package com.example.mispubs.REST;

import com.example.mispubs.Modelo.Pub;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PubRest {


    @GET("pubs")
    Call<List<Pub>> findAllPubs();

    @GET("pubs/buscar/{nombre}")
    Call<Pub> buscarPorNombreDelPub(@Path("nombre") String nombre);

    @POST("pubs")
    Call<Pub> insertarPub(@Body Pub guardarPub);
}
