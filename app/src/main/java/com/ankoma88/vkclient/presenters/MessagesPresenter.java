package com.ankoma88.vkclient.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ankoma88.vkclient.App;
import com.ankoma88.vkclient.R;
import com.ankoma88.vkclient.interfaces.MessagesListener;
import com.ankoma88.vkclient.models.MessageModel;
import com.ankoma88.vkclient.rest.RestClient;
import com.ankoma88.vkclient.rest.model.BaseModel;
import com.ankoma88.vkclient.ui.activities.MessagesActivity;
import com.ankoma88.vkclient.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.squareup.okhttp.ResponseBody;
import com.vk.sdk.VKAccessToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ankoma88 on 05.08.16.
 */
public class MessagesPresenter implements Callback<ResponseBody> {
    public static final String TAG = MessagesPresenter.class.getSimpleName();
    private Context context;
    private RestClient restClient;
    private MessagesListener listener;

    public MessagesPresenter(Context context) {
        this.context = context;
        this.restClient = new RestClient(this);
    }

    public void loadMessages(int userId, MessagesListener listener) {
        this.listener = listener;

        String token = getToken();
        if (!token.equals("")) restClient.loadMessages(userId, token);
        else {
            Log.e(TAG, "Error no token!");
        }
    }


    @NonNull
    private String getToken() {
            return VKAccessToken.currentToken().accessToken;
    }

    @Override
    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
        try {
            ArrayList<MessageModel> messages = new ArrayList<>();

            Gson gson = new Gson();
            String data = new String(response.body().bytes());
            JsonParser parser = new JsonParser();
            Object responseResult =  parser.parse(data).getAsJsonObject().get("response");

            if (responseResult instanceof JsonPrimitive) {
                handleMessageSentResponse();
            } else if (responseResult instanceof JsonArray){
                handleLoadMessagesResponse(messages, gson, (JsonArray) responseResult);
            }

        } catch (IOException e) {
            Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Log.e(TAG, t.getLocalizedMessage());
    }

    private void handleMessageSentResponse() {
        ((MessagesActivity)context).hideKeyboard();
        listener.onMessageSent();
    }

    private void handleLoadMessagesResponse(ArrayList<MessageModel> messages, Gson gson, JsonArray responseResult) {
        List<BaseModel> baseModelList = new ArrayList<>();
        boolean firstElement= true;
        for (JsonElement element : responseResult) {
            if (firstElement) {
                firstElement = false;
                continue;
            }
            BaseModel baseModel = gson.fromJson(element, BaseModel.class);
            baseModelList.add(baseModel);
        }

        for (BaseModel bm : baseModelList) {
            boolean isOutgoing = false;
            if (bm.out == 1) {
                isOutgoing = true;
            }
            MessageModel message = new MessageModel(bm.mid, bm.date, bm.body, isOutgoing);
            messages.add(message);
        }

        if (listener == null) {
            Log.e(TAG, "Error loading messages");
        } else {
            listener.onMessagesLoaded(messages);
        }
    }

    public void sendMessage(int uid, String newMsg, MessagesListener listener) {
        String token = getToken();
        if (!token.equals("")) {
            if (!Utils.isOnline(context)) {
                Utils.showSnackMessage(((MessagesActivity)context).getCurrentFocus(),
                        context.getString(R.string.turn_on_wifi));
                return;
            }
            restClient.sendMessage(token, uid, newMsg);
        } else {
            Log.e(TAG, "Error no token!");
        }
    }

}
