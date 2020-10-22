package com.omak.omakhelpers.firebaseNotification;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.io.Serializable;

public class notiData implements Serializable {

    RemoteMessage remoteMessage;
    String type;
    String title;
    String message;
    String btn_title = "View Now";
    String longImageUrl;
    String smallImageUrl;
    String smallIconUrl;
    Boolean ongoing = false;
    Boolean showNotification;
    String goTo;
    String channelId;
    public notiData(RemoteMessage remoteMessage) {
        this.remoteMessage = null;
        type = getDataKey(remoteMessage, "type");
        title = getDataKey(remoteMessage, "title");
        message = getDataKey(remoteMessage, "message");
        btn_title = getDataKey(remoteMessage, "btn_title");
        longImageUrl = getDataKey(remoteMessage, "img_big");
        smallImageUrl = getDataKey(remoteMessage, "img_icon");
        smallIconUrl = getDataKey(remoteMessage, "small_icon");
        showNotification = getDataKey(remoteMessage, "remoteShowNotification") != "false";
        goTo = getDataKey(remoteMessage, "goto");
        channelId = getDataKey(remoteMessage, "channel_id");

        if (channelId.isEmpty()) channelId = "channel_id_general";
    }

    public static notiData getFromIntent(String jsonString) {
        return new Gson().fromJson(jsonString, notiData.class);
    }

    public RemoteMessage getRemoteMessage() {
        return remoteMessage;
    }

    public void setRemoteMessage(RemoteMessage remoteMessage) {
        this.remoteMessage = remoteMessage;
    }

    public Boolean getOngoing() {
        return ongoing;
    }

    public void setOngoing(Boolean ongoing) {
        this.ongoing = ongoing;
    }

    public String getDataKey(RemoteMessage remoteMessage, String key) {
        return (remoteMessage.getData().get(key) != null) ? remoteMessage.getData().get(key) : "";
    }

    public String getDataKey(String key) {
        return (remoteMessage.getData().get(key) != null) ? remoteMessage.getData().get(key) : "";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBtn_title() {
        return btn_title;
    }

    public void setBtn_title(String btn_title) {
        this.btn_title = btn_title;
    }

    public String getLongImageUrl() {
        return longImageUrl;
    }

    public void setLongImageUrl(String longImageUrl) {
        this.longImageUrl = longImageUrl;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public String getSmallIconUrl() {
        return smallIconUrl;
    }

    public void setSmallIconUrl(String smallIconUrl) {
        this.smallIconUrl = smallIconUrl;
    }

    public Boolean getShowNotification() {
        return showNotification;
    }

    public void setShowNotification(Boolean showNotification) {
        this.showNotification = showNotification;
    }

    public String getGoTo() {
        return goTo;
    }

    public void setGoTo(String goTo) {
        this.goTo = goTo;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }


}
