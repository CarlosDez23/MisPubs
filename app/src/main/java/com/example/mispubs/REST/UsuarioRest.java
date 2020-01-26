package com.example.mispubs.REST;

import com.example.mispubs.Modelo.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsuarioRest {

    @GET("usuarios/{correo}/{password}")
    Call<Usuario> buscarPorCorreoPass(@Path("correo") String correo, @Path("password") String password);

    @GET("usuarios/registro/{nombre}")
    Call<Usuario> buscarPorNombreUsuario(@Path("nombre") String nombre);

    @POST("usuarios")
    Call<Usuario> insertarUsuario(@Body Usuario usuario);

    @DELETE("usuarios/{id}")
    Call<Usuario> borrarUsuario(@Path("id") Integer id);

}
