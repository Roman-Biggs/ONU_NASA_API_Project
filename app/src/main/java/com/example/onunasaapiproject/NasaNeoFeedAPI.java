package com.example.onunasaapiproject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NasaNeoFeedAPI {
    //Source endpoint
    @GET("neo/rest/v1/feed")
    Call<NeoResponse> getAsteroids(
      //Parameters
      @Query("start_date") String startDate,
      @Query("end_date") String endDate,
      @Query("api_key") String apiKey
    );
}
