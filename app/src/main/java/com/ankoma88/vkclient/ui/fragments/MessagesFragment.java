package com.ankoma88.vkclient.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ankoma88.vkclient.R;
import com.ankoma88.vkclient.adapters.MessagesAdapter;
import com.ankoma88.vkclient.interfaces.MessagesListener;
import com.ankoma88.vkclient.interfaces.MessagesCallback;
import com.ankoma88.vkclient.models.MessageModel;
import com.ankoma88.vkclient.ui.activities.MessagesActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ankoma88 on 04.08.16.
 */
public class MessagesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        MessagesListener, View.OnClickListener {
    private static final String MESSAGES = "vkMessages";

    private MessagesCallback messagesCallback;
    private ArrayList<MessageModel> messages;
    private int userId;
    private String userName;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Bind(R.id.rvMessages)
    RecyclerView rvMessages;

    @Bind(R.id.etSend)
    EditText etSend;

    @Bind(R.id.btnSend)
    ImageButton btnSend;

    public MessagesFragment() {
    }

    public static MessagesFragment newInstance(int userId, String userName) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MessagesActivity.USER_ID, userId);
        bundle.putString(MessagesActivity.USER_NAME, userName);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        messagesCallback = (MessagesCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        messagesCallback = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MESSAGES, messages);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userId = getArguments().getInt(MessagesActivity.USER_ID);
        this.userName = getArguments().getString(MessagesActivity.USER_NAME);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.bind(this, rootView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        rvMessages.setLayoutManager(layoutManager);
        refreshLayout.setOnRefreshListener(this);

        btnSend.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messagesCallback.loadMessages(userId, this);
        getActivity().setTitle(userName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            messages = savedInstanceState.getParcelableArrayList(MESSAGES);
            setAdapter(messages);
        } else {
            messagesCallback.loadMessages(userId, this);
        }
    }


    @Override
    public void onRefresh() {
        messagesCallback.loadMessages(userId, this);
    }

    @Override
    public void onMessagesLoaded(ArrayList<MessageModel> messages) {
        this.messages = messages;
        setAdapter(messages);

        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }

        if (!etSend.getText().toString().isEmpty()) {
            etSend.setText("");
        }
    }

    @Override
    public void onMessageSent() {
        messagesCallback.loadMessages(userId, this);
    }

    private void setAdapter(ArrayList<MessageModel> messages) {
        final MessagesAdapter adapter = new MessagesAdapter(messages);
        rvMessages.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        String newMsg = etSend.getText().toString();
        messagesCallback.sendMessage(userId, newMsg, this);
    }
}
