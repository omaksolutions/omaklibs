package com.omak.omakhelpers.CallDialog;

import android.graphics.Color;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.omak.samplelibrary.R;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CallerDialogModel extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("view_button_text")
    @Expose
    private String view_button_text;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("isContactExists")
    @Expose
    private Boolean isContactExists;
    @SerializedName("backgroundColor")
    @Expose
    private Integer backgroundColor;

    public CallerDialogModel() {
        name = "New Lead";
        view_button_text = "View";
        type = "unknown";
        id = 0;
        time = "";
        number = "";
        status = "";
        isContactExists = false;
        backgroundColor = R.color.colorPrimaryDark;
    }

    public Integer getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Integer backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Boolean getContactExists() {
        return isContactExists;
    }

    public void setContactExists(Boolean contactExists) {
        isContactExists = contactExists;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getView_button_text() {
        return view_button_text;
    }

    public void setView_button_text(String view_button_text) {
        this.view_button_text = view_button_text;
    }

}
