package com.ankoma88.vkclient;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;

import com.ankoma88.vkclient.ui.activities.AuthActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ankoma88 on 04.08.16.
 */
public class App extends Application {

    public static final int DIALOGS_NUMBER = 40;
    public static final int MESSAGES_NUMBER = 40;
    public static final String APP_PREFS = "vkClientPrefs";

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                Intent intent = new Intent(App.this, AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
        Fresco.initialize(this);
    }

}
