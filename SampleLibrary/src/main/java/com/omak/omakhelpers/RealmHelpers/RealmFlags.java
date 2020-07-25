package com.omak.omakhelpers.RealmHelpers;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmFlags extends RealmObject {
    @PrimaryKey
    private String key;
    private String stringValue;
    private Boolean booleanValue;
    private Integer counterValue;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

}

