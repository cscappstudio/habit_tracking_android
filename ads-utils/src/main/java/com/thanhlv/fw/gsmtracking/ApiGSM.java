package com.thanhlv.fw.gsmtracking;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiGSM {

    @Headers({"Accept: application/json"})
    @POST("/api/auth/login")
    Call<JsonObject> login(
            @Body JsonObject params
    );


    @Headers({"Accept: application/json"})
    @POST("/api/iap/check")
    Call<JsonObject> verifyIAP(
            @Header("Authorization") String token,
            @Body JsonObject params

    );

    @Headers({"Accept: application/json"})
    @POST("/api/iap/subcription/check")
    Call<JsonObject> verifySubs(
            @Header("Authorization") String token,
            @Body JsonObject params

    );

}
