package com.foxninja.familymap.model;

import java.util.ArrayList;

/**
 * Holds all the info for a logged in user
 */
public class FamilyMapData {
    private static FamilyMapData instance;

    private UserInfo info;

    private FamilyMapData() {}

    public static FamilyMapData getInstance(UserInfo info) {
        if (instance == null) instance = new FamilyMapData();
        instance.setInfo(info);
        return instance;
    }

    public static FamilyMapData getInstance() {
        if (instance == null) instance = new FamilyMapData();
        return instance;
    }

    public void setInfo(UserInfo info) {
        this.info = info;
    }

    public UserInfo getInfo() {
        return info;
    }

    public String getAuthorization() {
        return info.getAuthorization();
    }

    public String getUsername() {
        return info.getUsername();
    }

    public String getPersonId() {
        return info.getPersonId();
    }
}
