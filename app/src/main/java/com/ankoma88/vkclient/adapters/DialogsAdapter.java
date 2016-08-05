package com.ankoma88.vkclient.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ankoma88.vkclient.App;
import com.ankoma88.vkclient.R;
import com.ankoma88.vkclient.models.DialogModel;
import com.ankoma88.vkclient.utils.Utils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DialogsAdapter extends RecyclerView.Adapter<DialogsAdapter.DialogsViewHolder> {
    private List<DialogModel> dialogs = new ArrayList<>();
    private final OnItemClickListener listener;
    private Context context;

    public DialogsAdapter(List<DialogModel> dialogs, OnItemClickListener listener, Context context) {
        if (dialogs != null) {
            this.dialogs = dialogs;
        }
        this.context = context;
        this.listener = listener;
    }

    public class DialogsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_user_name)
        public TextView tvUserName;
        @Bind(R.id.tv_msg_date)
        public TextView tvDate;
        @Bind(R.id.tv_last_msg)
        public TextView tvText;
        @Bind(R.id.iv_avatar)
        public SimpleDraweeView ivAvatar;
        @Bind(R.id.iv_my_avatar)
        public SimpleDraweeView ivMyAvatar;

        public DialogsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(final DialogModel dialog, final OnItemClickListener listener) {
            tvUserName.setText(dialog.getFirstName() + " " + dialog.getLastName());
            tvDate.setText(Utils.toDate(dialog.getDate()));
            tvText.setText(dialog.getText());

            String avatarUrl = dialog.getAvatarUrl();
            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                ivAvatar.setImageURI(Uri.parse(dialog.getAvatarUrl()));
            }

            if (dialog.isOutgoing()) {
                final String myAvatarUrl = getMyAvatarUrl();
                if (!myAvatarUrl.equals("")) {
                    ivMyAvatar.setVisibility(View.VISIBLE);
                    ivMyAvatar.setImageURI(Uri.parse(getMyAvatarUrl()));
                }
            }

            itemView.setOnClickListener(v -> listener.onItemClick(dialog));
        }
    }

    private String getMyAvatarUrl() {
        SharedPreferences prefs = context.getSharedPreferences(App.APP_PREFS, Context.MODE_PRIVATE);
        return prefs.getString(context.getString(R.string.my_avatar), "");
    }

    @Override
    public DialogsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dialog, parent, false);

        return new DialogsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DialogsViewHolder holder, int position) {
        holder.bind(dialogs.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return dialogs.size();
    }

    public interface OnItemClickListener {
        void onItemClick(DialogModel post);
    }

}
