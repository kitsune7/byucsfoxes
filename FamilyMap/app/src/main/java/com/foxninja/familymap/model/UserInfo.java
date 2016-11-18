package com.foxninja.familymap.model;

/**
 * Holds the info for a successfully logged in user.
 */
public class UserInfo {
    private String authorization;
    private String username;
    private String personId;

    public UserInfo(String authorization, String username, String personId) {
        this.authorization = authorization;
        this.username = username;
        this.personId = personId;
    }


    public String getAuthorization() {
        return authorization;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonId() {
        return personId;
    }


    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
