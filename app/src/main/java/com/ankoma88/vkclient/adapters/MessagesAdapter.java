package com.ankoma88.vkclient.adapters;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ankoma88.vkclient.R;
import com.ankoma88.vkclient.models.MessageModel;
import com.ankoma88.vkclient.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ankoma88 on 04.08.16.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {
    private List<MessageModel> messages = new ArrayList<>();
    private final int OUTBOX = 1;
    private final int INBOX = 0;

    public MessagesAdapter(List<MessageModel> messages) {
        if (messages != null) {
            this.messages = messages;
        }
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_msg_txt)
        public TextView tvText;
        @Bind(R.id.tv_msg_date)
        public TextView tvDate;

        public MessagesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(final MessageModel msg) {
            tvDate.setText(Utils.toDate(msg.getDate()));
            tvText.setText(msg.getText());
        }
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case OUTBOX:
                return new MessagesViewHolder(inflater.inflate(R.layout.item_message_right, parent, false));
            case INBOX:
                return new MessagesViewHolder(inflater.inflate(R.layout.item_message_left, parent, false));
            default:
                return new MessagesViewHolder(inflater.inflate(R.layout.item_message_right, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).isOutgoing()) {
            return OUTBOX;
        } else if (!messages.get(position).isOutgoing()) {
            return INBOX;
        }
        return -1;
    }

}
