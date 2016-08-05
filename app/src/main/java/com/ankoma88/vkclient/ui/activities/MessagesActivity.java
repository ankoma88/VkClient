package com.ankoma88.vkclient.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ankoma88.vkclient.R;
import com.ankoma88.vkclient.interfaces.MessagesListener;
import com.ankoma88.vkclient.interfaces.MessagesCallback;
import com.ankoma88.vkclient.presenters.MessagesPresenter;
import com.ankoma88.vkclient.ui.fragments.MessagesFragment;

/**
 * Created by ankoma88 on 05.08.16.
 */
public class MessagesActivity extends AppCompatActivity implements MessagesCallback {
    public static final String TAG = DialogsActivity.class.getSimpleName();
    public static final String USER_ID = "vkUserId";
    public static final String USER_NAME = "vkUserName";
    private static final String TAG_MESSAGES_FRAGMENT = "messagesFragment";

    private MessagesPresenter presenter;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter = new MessagesPresenter(this);
        setContentView(R.layout.activity_dialogs);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        int vkUserId = getIntent().getIntExtra(USER_ID, 0);
        String vkUserName = getIntent().getStringExtra(USER_NAME);
        showMessagesFragment(savedInstanceState, vkUserId, vkUserName);
    }

    private void showMessagesFragment(Bundle savedInstanceState, int userId, String userName) {
        MessagesFragment messagesFragment;
        if (savedInstanceState == null) {
            messagesFragment = MessagesFragment.newInstance(userId, userName);
        } else {
            messagesFragment = (MessagesFragment) getSupportFragmentManager().findFragmentByTag(TAG_MESSAGES_FRAGMENT);
        }

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.container, messagesFragment, TAG_MESSAGES_FRAGMENT)
                .commit();
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadMessages(int userId, MessagesListener listener) {
        presenter.loadMessages(userId, listener);
    }

    @Override
    public void sendMessage(int uid, String newMsg, MessagesListener listener) {
        presenter.sendMessage(uid, newMsg, listener);
    }
}
