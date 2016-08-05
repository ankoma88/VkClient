package com.ankoma88.vkclient.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ankoma88.vkclient.R;
import com.ankoma88.vkclient.interfaces.DialogsListener;
import com.ankoma88.vkclient.interfaces.DialogsCallback;
import com.ankoma88.vkclient.presenters.DialogsPresenter;
import com.ankoma88.vkclient.ui.fragments.DialogsFragment;


/**
 * Created by ankoma88 on 04.08.16.
 */
public class DialogsActivity extends AppCompatActivity implements DialogsCallback {

    public static final String TAG = DialogsActivity.class.getSimpleName();
    private static final String TAG_DIALOGS_FRAGMENT = "dialogsFragment";

    private DialogsPresenter presenter;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        presenter = new DialogsPresenter(this);
        showDialogsFragment(savedInstanceState);
    }

    private void showDialogsFragment(@Nullable Bundle savedInstanceState) {
        DialogsFragment dialogsFragment;
        if (savedInstanceState == null) {
            dialogsFragment = new DialogsFragment();
        } else {
            dialogsFragment = (DialogsFragment) getSupportFragmentManager().findFragmentByTag(TAG_DIALOGS_FRAGMENT);
        }

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.container, dialogsFragment, TAG_DIALOGS_FRAGMENT)
                .commit();
    }

    @Override
    public void openMessages(int userId, String userName) {
        Intent intent = new Intent(this, MessagesActivity.class);
        intent.putExtra(MessagesActivity.USER_ID, userId);
        intent.putExtra(MessagesActivity.USER_NAME, userName);
        startActivity(intent);
    }


    @Override
    public void loadDialogs(DialogsListener listener) {
        presenter.loadDialogs(listener);
    }

}


