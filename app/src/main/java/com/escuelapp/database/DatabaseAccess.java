package com.escuelapp.database;

import com.escuelapp.model.BaseModel;
import com.escuelapp.model.User;
import com.escuelapp.utility.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
                callback.onDatabaseResponse(user, Constants.SEARCH_USER);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onDatabaseError(databaseError);
            }
        });
    }

    public static void saveNewUser(User user, String uid) {
        FirebaseDatabase.getInstance().getReference().child(Constants.NODE_USERS).child(uid).setValue(user);
    }

    public interface OnDatabaseResponse {
        public void onDatabaseResponse(BaseModel baseModel, String operationCode);

        public void onDatabaseError(DatabaseError databaseError);
    }
}
