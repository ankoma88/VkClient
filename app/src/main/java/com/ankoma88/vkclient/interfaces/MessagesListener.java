package com.ankoma88.vkclient.interfaces;

import com.ankoma88.vkclient.models.MessageModel;

import java.util.ArrayList;

/**
 * Created by ankoma88 on 04.08.16.
 */
public interface MessagesListener {
    void onMessagesLoaded(ArrayList<MessageModel> messages);
    void onMessageSent();
}
