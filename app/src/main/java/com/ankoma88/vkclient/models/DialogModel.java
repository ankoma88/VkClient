package com.ankoma88.vkclient.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ankoma88 on 04.08.16.
 */
public class DialogModel implements Parcelable {

    private int id;
    private int userId;
    private String firstName;
    private String lastName;
    private long date;
    private String text;
    private String avatarUrl;
    private boolean isOutgoing;


    public DialogModel() {
    }

    public DialogModel(int id, int userId, String firstName, String lastName, long date, String text, String avatarUrl, boolean isOutgoing) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        if (text.length() > 50) {
            text = text.substring(0,50) + "...";
        }
        this.text = text;
        this.avatarUrl = avatarUrl;
        this.isOutgoing = isOutgoing;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        if (text.length() > 50) {
            text = text.substring(0,50) + "...";
        }
        this.text = text;
    }

    public boolean isOutgoing() {
        return isOutgoing;
    }

    public void setOutgoing(boolean outgoing) {
        isOutgoing = outgoing;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DialogModel that = (DialogModel) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        if (date != that.date) return false;
        return text != null ? text.equals(that.text) : that.text == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userId;
        result = 31 * result + (int) (date ^ (date >>> 32));
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DialogModel{" +
                "id=" + id +
                ", userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(userId);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeLong(date);
        parcel.writeString(text);
        parcel.writeString(avatarUrl);
        parcel.writeByte((byte) (isOutgoing ? 1:0));
    }


    protected DialogModel(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        date = in.readLong();
        text = in.readString();
        avatarUrl = in.readString();
        isOutgoing = in.readByte() != 0;
    }

    public static final Creator<DialogModel> CREATOR = new Creator<DialogModel>() {
        @Override
        public DialogModel createFromParcel(Parcel in) {
            return new DialogModel(in);
        }

        @Override
        public DialogModel[] newArray(int size) {
            return new DialogModel[size];
        }
    };
}
