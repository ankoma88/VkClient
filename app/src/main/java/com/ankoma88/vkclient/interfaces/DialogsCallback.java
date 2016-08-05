package com.ankoma88.vkclient.interfaces;


/**
 * Created by ankoma88 on 04.08.16.
 */
public interface DialogsCallback {
    void loadDialogs(DialogsListener listener);
    void openMessages(int userId, String userName);
}
