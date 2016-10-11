package com.escuelapp.model;

import java.util.List;

/**
 * Created by alan on 10/4/16.
 */
public class Users extends BaseModel {

    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

}
