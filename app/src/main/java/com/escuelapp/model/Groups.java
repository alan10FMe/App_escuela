package com.escuelapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 11/8/16.
 */
public class Groups extends BaseModel {
    private List<Group> groups = new ArrayList<>();

    public Groups(List<Group> groups) {
        this.groups = groups;
    }

    public Groups() {
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
