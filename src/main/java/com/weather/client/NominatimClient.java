package com.weather.client;

import com.weather.model.Place;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface NominatimClient {

    @GET("search?format=json")
    Call<List<Place>> listPlaces(@Query("city") String city);
}
