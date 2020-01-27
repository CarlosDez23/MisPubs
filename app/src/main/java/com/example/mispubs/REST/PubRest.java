package com.example.mispubs.REST;

import com.example.mispubs.Modelo.Pub;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PubRest {

    @GET("pubs")
    Call<List<Pub>> findAllPubs();
}
