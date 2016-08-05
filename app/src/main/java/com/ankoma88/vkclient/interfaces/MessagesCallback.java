package com.ankoma88.vkclient.interfaces;


/**
 * Created by ankoma88 on 04.08.16.
 */
public interface MessagesCallback {
    void loadMessages(int userId, MessagesListener listener);
    void sendMessage(int userId, String newMsg, MessagesListener listener);
}
