package com.escuelapp.model;

import com.escuelapp.utility.Constants;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by alan on 10/4/16.
 */
public class User extends BaseModel {

    private String uid;
    private String name;
    private String email;
    private HashMap<String, Object> groupsCreated;
    private HashMap<String, Object> groupsParticipant;
    private boolean requester;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Object> getGroupsCreated() {
        return groupsCreated;
    }

    public void setGroupsCreated(HashMap<String, Object> groupsCreated) {
        this.groupsCreated = groupsCreated;
    }

    public HashMap<String, Object> getGroupsParticipant() {
        return groupsParticipant;
    }

    public void setGroupsParticipant(HashMap<String, Object> groupsParticipant) {
        this.groupsParticipant = groupsParticipant;
    }

    public boolean isRequester() {
        return requester;
    }

    public void setRequester(boolean requester) {
        this.requester = requester;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constants.TAG_UID, uid);
        result.put(Constants.TAG_NAME, name);
        result.put(Constants.TAG_EMAIL, email);
        return result;
    }

    public User(HashMap<String, Object> map) {
        uid = (String) map.get(Constants.TAG_UID);
        name = (String) map.get(Constants.TAG_NAME);
        email = (String) map.get(Constants.TAG_EMAIL);
    }

    public User() {
    }
}
