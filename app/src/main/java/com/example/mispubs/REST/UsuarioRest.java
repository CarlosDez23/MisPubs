package com.example.mispubs.REST;

import com.example.mispubs.Modelo.Usuario;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UsuarioRest {

    @GET("usuarios/{correo}/{password}")
    Call<Usuario> buscarPorCorreoPass(@Path("correo") String correo, @Path("password") String password);

}
