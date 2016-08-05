package com.ankoma88.vkclient.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ankoma88 on 04.08.16.
 */
public class MessageModel implements Parcelable {
    private int id;
    private long date;
    private String text;
    private boolean isOutgoing;

    public MessageModel() {
    }

    public MessageModel(int id, long date, String text, boolean isOutgoing) {
        this.id = id;
        this.date = date;
        this.text = text;
        this.isOutgoing = isOutgoing;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isOutgoing() {
        return isOutgoing;
    }

    public void setOutgoing(boolean outgoing) {
        isOutgoing = outgoing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageModel that = (MessageModel) o;

        if (id != that.id) return false;
        if (date != that.date) return false;
        if (isOutgoing != that.isOutgoing) return false;
        return text != null ? text.equals(that.text) : that.text == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (int) (date ^ (date >>> 32));
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (isOutgoing ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", isOutgoing=" + isOutgoing +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeLong(date);
        parcel.writeString(text);
        parcel.writeByte((byte) (isOutgoing ? 1:0));
    }

    protected MessageModel(Parcel in) {
        id = in.readInt();
        date = in.readLong();
        text = in.readString();
        isOutgoing = in.readByte() != 0;
    }

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel in) {
            return new MessageModel(in);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };
}
