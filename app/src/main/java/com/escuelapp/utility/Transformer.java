package com.escuelapp.utility;

import com.escuelapp.model.Group;
import com.escuelapp.model.Groups;
import com.escuelapp.model.User;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by alan on 10/26/16.
 */
public class Transformer {

    public static List<User> dataSnapshotToUsers(DataSnapshot dataSnapshot) {
        List<User> users = new ArrayList<>();
        return users;
    }

    public static List<Group> dataSnapshotToGroups(DataSnapshot dataSnapshot) {
        List<Group> groups = new ArrayList<>();
        if (dataSnapshot.getValue() == null) {
            return groups;
        }
        Iterator it = ((HashMap<String, Object>) dataSnapshot.getValue()).entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            groups.add(new Group((HashMap<String, Object>) pair.getValue()));
            it.remove();
        }
        return groups;
    }

    public static Group dataSnapshotToGroup(DataSnapshot dataSnapshot) {
        return new Group((HashMap<String, Object>) dataSnapshot.getValue());
    }

    public static Groups    dataSnapShotToGroupsCreated(DataSnapshot dataSnapshot) {
        List<Group> groups = new ArrayList<>();
        if (dataSnapshot.getValue() == null) {
            return new Groups();
        }
        HashMap hashGroups = (HashMap) ((HashMap) ((Map.Entry) ((HashMap) dataSnapshot.getValue())
                .entrySet().iterator().next())
                .getValue())
                .get(Constants.NODE_GROUPS_CREATED);
        Iterator it = hashGroups.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            groups.add(new Group((HashMap<String, Object>) pair.getValue()));
            it.remove();
        }
        return new Groups(groups);
    }

}
