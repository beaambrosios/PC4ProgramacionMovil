package com.example.pc4progmovil.service;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JokeApiService {

    @GET("joke/{category}")
    Call<JsonObject> getJokeCall(
            @Path("category") String category,
            @Query("lang") String lang);
}