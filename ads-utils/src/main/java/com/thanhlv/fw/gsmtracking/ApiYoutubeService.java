package com.thanhlv.fw.gsmtracking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiYoutubeService {

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    //videodownloaded

    ApiYoutubeService apiYoutubeSourceLink = new Retrofit.Builder()
            .baseUrl("https://api.ytbvideoly.com/api/thirdvideo/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiYoutubeService.class);
    @GET("parse")
    Call<JsonElement> getVideo(@Query("link") String link, @Query("from") String from);
}

