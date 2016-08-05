package com.ankoma88.vkclient.presenters;

import android.content.Context;
import android.util.Log;

import com.ankoma88.vkclient.App;
import com.ankoma88.vkclient.R;
import com.ankoma88.vkclient.interfaces.DialogsListener;
import com.ankoma88.vkclient.models.DialogModel;
import com.ankoma88.vkclient.ui.activities.DialogsActivity;
import com.ankoma88.vkclient.utils.Utils;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiGetDialogResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ankoma88 on 05.08.16.
 */
public class DialogsPresenter {
    public static final String TAG = DialogsPresenter.class.getSimpleName();
    private Context context;

    public DialogsPresenter(Context context) {
        this.context = context;
    }

    public void loadDialogs(DialogsListener listener) {
        VKRequest request = VKApi.messages().getDialogs((VKParameters.from(VKApiConst.COUNT, App.DIALOGS_NUMBER)));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                VKApiGetDialogResponse getDialogResponse = (VKApiGetDialogResponse) response.parsedModel;
                VKList<VKApiDialog> vkApiDialogs = getDialogResponse.items;

                loadUserData(vkApiDialogs, listener);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Utils.showSnackMessage(((DialogsActivity)context).getCurrentFocus(),
                        context.getString(R.string.error_msg));
                Log.e(TAG, "Error loading dialogs: " + error.errorMessage);
            }
        });
    }

    private void loadUserData(List<VKApiDialog> vkDialogs, DialogsListener listener) {
        ArrayList<DialogModel> dialogs = new ArrayList<>();

        StringBuilder ids = new StringBuilder();

        for (VKApiDialog vkDialog : vkDialogs) {
            ids.append(",").append(vkDialog.message.user_id);

            DialogModel dialogModel = new DialogModel();
            dialogModel.setId(vkDialog.getId());
            dialogModel.setUserId(vkDialog.message.user_id);
            dialogModel.setDate(vkDialog.message.date);
            dialogModel.setText(vkDialog.message.body);
            dialogModel.setOutgoing(vkDialog.message.out);

            dialogs.add(dialogModel);
        }

        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, ids.toString(),
                VKApiConst.FIELDS, "photo_50,first_name,last_name"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                VKList<VKApiUserFull> list = (VKList<VKApiUserFull>) response.parsedModel;
                final int count = list.size();
                for (int i = 0; i < count; i++) {
                    VKApiUserFull userFull = list.get(i);
                    DialogModel dialogModel = dialogs.get(i);
                    dialogModel.setAvatarUrl(userFull.photo_50);
                    dialogModel.setFirstName(userFull.first_name);
                    dialogModel.setLastName(userFull.last_name);
                }

                listener.onDialogsLoaded(dialogs);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Utils.showSnackMessage(((DialogsActivity)context).getCurrentFocus(),
                        context.getString(R.string.error_msg));
                Log.e(TAG, "Error loading photo: " + error.errorMessage);
            }
        });
    }
}
