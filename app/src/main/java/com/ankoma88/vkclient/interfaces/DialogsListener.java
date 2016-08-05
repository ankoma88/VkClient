package com.ankoma88.vkclient.interfaces;

import com.ankoma88.vkclient.models.DialogModel;

import java.util.ArrayList;

/**
 * Created by ankoma88 on 04.08.16.
 */
public interface DialogsListener {
    void onDialogsLoaded(ArrayList<DialogModel> dialogs);
}
