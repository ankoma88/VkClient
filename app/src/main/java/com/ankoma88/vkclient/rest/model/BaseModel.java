package com.ankoma88.vkclient.rest.model;

public class BaseModel {

    public String body;
    public int mid;
    public int uid;
    public int fromId;
    public int date;
    public int readState;
    public int out;

    @Override
    public String toString() {
        return "BaseModel{" +
                "body='" + body + '\'' +
                ", mid=" + mid +
                ", uid=" + uid +
                ", fromId=" + fromId +
                ", date=" + date +
                ", readState=" + readState +
                ", out=" + out +
                '}';
    }
}
