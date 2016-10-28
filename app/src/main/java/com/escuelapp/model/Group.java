package com.escuelapp.model;

import com.escuelapp.utility.Constants;
import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by alan on 10/18/16.
 */
@IgnoreExtraProperties
public class Group extends BaseModel {

    private String uid;
    private String name;
    private String creator;
    private String creatorName;
    private HashMap<String, Object> requesters;
    private HashMap<String, Object> participants;
    private boolean isSelected;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public HashMap<String, Object> getRequesters() {
        return requesters;
    }

    public void setRequesters(HashMap<String, Object> requesters) {
        this.requesters = requesters;
    }

    public HashMap<String, Object> getParticipants() {
        return participants;
    }

    public void setParticipants(HashMap<String, Object> participants) {
        this.participants = participants;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Group() {
    }

    public Group(HashMap<String, Object> map) {
        uid = (String) map.get(Constants.TAG_UID);
        name = (String) map.get(Constants.TAG_NAME);
        creator = (String) map.get(Constants.TAG_CREATOR);
        creatorName = (String) map.get(Constants.TAG_CREATOR_NAME);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constants.TAG_UID, uid);
        result.put(Constants.TAG_NAME, name);
        result.put(Constants.TAG_CREATOR, creator);
        result.put(Constants.TAG_CREATOR_NAME, creatorName);
        result.put(Constants.NODE_GROUPS_PARTICIPANT, participants);
        result.put(Constants.NODE_USERS_REQUESTERS, requesters);
        return result;
    }

}
