package com.example.mispubs.REST;

import com.example.mispubs.Modelo.Pub;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PubRest {


    @GET("pubs")
    Call<List<Pub>> findAllPubs();

    @GET("pubs/buscar/{nombre}")
    Call<Pub> buscarPorNombreDelPub(@Path("nombre") String nombre);

    @POST("pubs")
    Call<Pub> insertarPub(@Body Pub guardarPub);

    @PUT("pubs/{id}")
    Call<Pub> modificarPub(@Path("id") Integer id, @Body Pub pub);

    @DELETE("pubs/{id}")
    Call<Pub> eliminarPub(@Path("id") Integer id);
}
