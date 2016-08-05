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

import com.ankoma88.vkclient.R;
import com.ankoma88.vkclient.adapters.DialogsAdapter;
import com.ankoma88.vkclient.interfaces.DialogsListener;
import com.ankoma88.vkclient.interfaces.DialogsCallback;
import com.ankoma88.vkclient.models.DialogModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ankoma88 on 04.08.16.
 */
public class DialogsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DialogsListener {
    private static final String TAG = DialogsFragment.class.getSimpleName();
    private static final String DIALOGS = "vkDialogs";

    private DialogsCallback dataLoader;
    private ArrayList<DialogModel> dialogs;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Bind(R.id.rvDialogs)
    RecyclerView rvDialogs;

    public DialogsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataLoader = (DialogsCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dataLoader = null;
    }

    @Override
    public void onRefresh() {
        dataLoader.loadDialogs(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialogs, container, false);
        ButterKnife.bind(this, rootView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvDialogs.setLayoutManager(layoutManager);
        refreshLayout.setOnRefreshListener(this);

        return rootView;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            dialogs = savedInstanceState.getParcelableArrayList(DIALOGS);
            setAdapter(dialogs);
        } else {
            dataLoader.loadDialogs(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(DIALOGS, dialogs);
    }

    private void setAdapter(ArrayList<DialogModel> dialogs) {
        final DialogsAdapter adapter = new DialogsAdapter(dialogs, this::openMessages, getActivity());
        rvDialogs.setAdapter(adapter);
    }

    private void openMessages(DialogModel dialogModel) {
        String userName = dialogModel.getFirstName() + " " + dialogModel.getLastName();
        dataLoader.openMessages(dialogModel.getUserId(), userName);
    }

    @Override
    public void onDialogsLoaded(ArrayList<DialogModel> dialogs) {
        this.dialogs = dialogs;
        setAdapter(dialogs);

        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }
}
