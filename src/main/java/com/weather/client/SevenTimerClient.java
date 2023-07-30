package com.weather.client;

import com.weather.model.SevenTimerResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SevenTimerClient {

    @GET("bin/api.pl?product=civillight&output=json")
    Call<SevenTimerResponse> getData(@Query("lat") float lat, @Query("lon") float lon);
}
