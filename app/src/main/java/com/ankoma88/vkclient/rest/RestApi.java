package com.ankoma88.vkclient.rest;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;


public interface RestApi {
    @GET("messages.getHistory")
    Call<ResponseBody> loadData(@Query("access_token") String accessToken, @Query("uid") int uid, @Query("count") int count);

    @GET("messages.send")
    Call<ResponseBody> sendMessage(@Query("access_token") String accessToken, @Query("uid") int uid,  @Query("message") String msg);
}

