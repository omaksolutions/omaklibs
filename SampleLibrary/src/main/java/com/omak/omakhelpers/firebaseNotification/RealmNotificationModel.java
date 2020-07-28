package com.omak.omakhelpers.firebaseNotification;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.omak.omakhelpers.RealmHelpers.RealmHelpers;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmNotificationModel extends RealmObject {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("ongoing")
    @Expose
    private Boolean ongoing;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("channel_id")
    @Expose
    private String channel_id;
    @SerializedName("data")
    @Expose
    private String data;

    /**
     *
     * @param context
     * @param notiData
     */
    public static void createAndInsert(Context context, final HashMap<String, String> notiData) {
        Realm realm = RealmHelpers.getRealm("messages", context);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // increment index
                Number currentIdNum = realm.where(RealmNotificationModel.class).max("id");
                int nextId = (currentIdNum == null) ? 1 : currentIdNum.intValue() + 1;

                RealmNotificationModel realmNotificationModel = new RealmNotificationModel();
                realmNotificationModel.setId(nextId);
                realmNotificationModel.setOngoing(Boolean.valueOf(notiData.get("ongoing")));
                realmNotificationModel.setChannel_id(notiData.get("channel_id"));
                realmNotificationModel.setTitle(notiData.get("title"));
                realmNotificationModel.setMessage(notiData.get("message"));
                realmNotificationModel.setType(notiData.get("type"));
                realmNotificationModel.setData(notiData.get("data"));
                realm.insertOrUpdate(realmNotificationModel); // using insert API
            }
        });
    }

    /**
     *
     * @param context
     * @param notiData
     */
    public static void createAndInsert(Context context, final notiData notiData) {
        Realm realm = RealmHelpers.getRealm("messages", context);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // increment index
                Number currentIdNum = realm.where(RealmNotificationModel.class).max("id");
                int nextId = (currentIdNum == null) ? 1 : currentIdNum.intValue() + 1;
                RealmNotificationModel realmNotificationModel = new RealmNotificationModel();
                realmNotificationModel.setId(nextId);

                realmNotificationModel.setOngoing(notiData.getOngoing());
                realmNotificationModel.setChannel_id(notiData.getChannelId());
                realmNotificationModel.setTitle(notiData.getTitle());
                realmNotificationModel.setMessage(notiData.getMessage());
                realmNotificationModel.setType(notiData.getType());
                realmNotificationModel.setData(notiData.getData().toString());
                realm.insertOrUpdate(realmNotificationModel); // using insert API
            }
        });
    }

    /**
     *
     * @return
     */
    public String getData() {
        return data;
    }

    /**
     *
     * @param data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getOngoing() {
        return ongoing;
    }

    public void setOngoing(Boolean ongoing) {
        this.ongoing = ongoing;
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

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }
}