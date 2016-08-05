package com.ankoma88.vkclient.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ankoma88.vkclient.App;
import com.ankoma88.vkclient.R;
import com.ankoma88.vkclient.utils.Utils;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

public class AuthActivity extends AppCompatActivity {
    public static final String TAG = AuthActivity.class.getSimpleName();

    private String[] scope = new String[]{VKScope.MESSAGES, VKScope.FRIENDS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        checkConnection();
    }

    private void checkConnection() {
        if (!Utils.isOnline(this)) {
            askToTurnOnInternet(this);
        } else {
            saveCurrentUserAvatar();
        }
    }

    private void saveCurrentUserAvatar() {
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_50"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKList result = (VKList) response.parsedModel;
                if (result.isEmpty()) {
                    VKSdk.login(AuthActivity.this, scope);
                    return;
                }
                VKApiUserFull user = (VKApiUserFull) result.get(0);
                String myAvatar = user.photo_50;

                SharedPreferences sharedPref = getSharedPreferences(App.APP_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.my_avatar), myAvatar);
                editor.apply();

                VKSdk.login(AuthActivity.this, scope);
            }
        });
    }

    private void askToTurnOnInternet(Context context) {
        Activity activity = (Activity) context;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.app_name));
        builder.setMessage(context.getString(R.string.turn_on_wifi));
        builder.setPositiveButton(context.getString(android.R.string.yes),
                (dialogInterface, i) -> {
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                    checkConnection();
                });
        builder.setNegativeButton(context.getString(android.R.string.no),
                (dialogInterface, i) -> {
                    activity.finish();
                });
        builder.setCancelable(false);
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            finish();
        }

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
                    @Override
                    public void onResult(VKAccessToken res) {
                        Log.d(TAG, "onResult vkAccessToken: " + res);
                        String accessToken = res.accessToken;
                        openDialogsActivity(accessToken);
                    }

                    @Override
                    public void onError(VKError error) {
                        Utils.showSnackMessage(getCurrentFocus(), getString(R.string.error_msg));
                        Log.e(TAG, "onResult vkError: " + error);
                    }
                }
        )) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void openDialogsActivity(String accessToken) {
        Intent intent = new Intent(this, DialogsActivity.class);
        intent.putExtra("accessToken", accessToken);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
