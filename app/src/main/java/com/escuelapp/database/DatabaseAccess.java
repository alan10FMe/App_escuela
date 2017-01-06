package com.escuelapp.database;

import com.escuelapp.model.BaseModel;
import com.escuelapp.model.Group;
import com.escuelapp.model.Groups;
import com.escuelapp.model.User;
import com.escuelapp.utility.Constants;
import com.escuelapp.utility.Transformer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alan on 10/5/16.
 */
public class DatabaseAccess {

    public static void findUserByUid(String uid, final OnDatabaseResponse callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.NODE_USERS);
        Query queryUser = databaseReference.child(uid);
        queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                callback.onDatabaseResponse(user, Constants.DB_ACTION_SEARCH_USER);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onDatabaseError(databaseError);
            }
        });
    }


    public static void findGroupByUid(String uid, final OnDatabaseResponse callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.NODE_GROUPS);
        Query queryGroup = databaseReference.child(uid);
        queryGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Group group = dataSnapshot.getValue(Group.class);
                callback.onDatabaseResponse(group, Constants.DB_ACTION_SEARCH_USER);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onDatabaseError(databaseError);
            }
        });
    }

    public static void saveNewUser(User user) {
        FirebaseDatabase.getInstance().getReference().child(Constants.NODE_USERS)
                .child(user.getUid()).setValue(user);
    }

    public static void saveNewGroup(Group group, String userUid) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String key = database.child(Constants.NODE_GROUPS).push().getKey();
        group.setUid(key);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + Constants.NODE_GROUPS + "/" + key, group);
        childUpdates.put("/" + Constants.NODE_USERS + "/" + userUid + "/" + Constants.NODE_GROUPS_CREATED + "/" + key, group);
        database.updateChildren(childUpdates);
    }

    public interface OnDatabaseResponse {
        public void onDatabaseResponse(BaseModel baseModel, String operationCode);

        public void onDatabaseError(DatabaseError databaseError);
    }

    public static List<Group> findGroupsByEmailOwner(String email, final OnDatabaseResponse callback) {
        Query queryGroups = FirebaseDatabase.getInstance()
                .getReference(Constants.NODE_USERS)
                .orderByChild(Constants.TAG_EMAIL)
                .limitToFirst(1)
                .equalTo(email.trim());
        queryGroups.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onDatabaseResponse(Transformer.dataSnapShotToGroupsCreated(dataSnapshot),
                        Constants.DB_ACTION_SEARCH_GROUPS_CREATED);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onDatabaseError(databaseError);
            }
        });
        return null;
    }

    public static void requestAccessGroups(Groups groups, User user) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        for (Group group : groups.getGroups()) {
            childUpdates.put("/" + Constants.NODE_GROUPS + "/" + group.getUid() + "/" + Constants.NODE_USERS_REQUESTERS
                    + "/" + user.getUid(), user.toMap());
        }
        database.updateChildren(childUpdates);
    }

    public static void acceptRequestUser(User user, String groupUid) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + Constants.NODE_GROUPS + "/" + groupUid + "/" + Constants.NODE_USERS_PARTICIPANTS
                + "/" + user.getUid() + "/", user);
        childUpdates.put("/" + Constants.NODE_GROUPS + "/" + groupUid + "/" + Constants.NODE_USERS_REQUESTERS
                + "/" + user.getUid() + "/", null);
        database.updateChildren(childUpdates);
    }


}
