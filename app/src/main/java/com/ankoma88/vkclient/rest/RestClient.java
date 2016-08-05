package com.ankoma88.vkclient.rest;

import com.ankoma88.vkclient.App;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

public class RestClient {
    private static final String REST_BASE_URL = "https://api.vk.com/method/";
    private RestApi restApi;
    private Callback<ResponseBody> restCallback;

    public RestClient(Callback<ResponseBody> restCallback) {
        this.restCallback = restCallback;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(REST_BASE_URL)
                .build();

        restApi = retrofit.create(RestApi.class);

    }

    public void loadMessages(int userId, String token) {
        Call<ResponseBody> call = restApi.loadData(token, userId, App.MESSAGES_NUMBER);
        call.enqueue(restCallback);
    }

    public void sendMessage(String token, int userId, String msg) {
        Call<ResponseBody> call = restApi.sendMessage(token, userId, msg);
        call.enqueue(restCallback);
    }

}
