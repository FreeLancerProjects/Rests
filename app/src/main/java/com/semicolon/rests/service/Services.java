package com.semicolon.rests.service;

import com.semicolon.rests.models.CityModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Services {

    @GET("AppUser/Cites")
    Call<List<CityModel>> getCity();
}
