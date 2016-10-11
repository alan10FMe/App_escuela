package com.escuelapp.database;

import com.escuelapp.model.BaseModel;
import com.escuelapp.model.Student;
import com.escuelapp.model.Teacher;
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

    public static void findTeacherByUid(String uid, final OnDatabaseResponse callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.NODE_TEACHERS);
        Query queryUser = databaseReference.child(uid);
        queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Teacher teacher = dataSnapshot.getValue(Teacher.class);
                callback.onDatabaseResponse(teacher, Constants.SEARCH_TEACHER);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onDatabaseError(databaseError);
            }
        });
    }

    public static void findStudentByUid(String uid, final OnDatabaseResponse callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.NODE_STUDENTS);
        Query queryUser = databaseReference.child(uid);
        queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                callback.onDatabaseResponse(student, Constants.SEARCH_STUDENT);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onDatabaseError(databaseError);
            }
        });
    }

    public static void saveNewTeacher(Teacher teacher, String uid) {
        FirebaseDatabase.getInstance().getReference().child(Constants.NODE_TEACHERS).child(uid).setValue(teacher);
    }

    public static void saveNewStudent(Student student, String uid) {
        FirebaseDatabase.getInstance().getReference().child(Constants.NODE_STUDENTS).child(uid).setValue(student);
    }

    public interface OnDatabaseResponse {
        public void onDatabaseResponse(BaseModel baseModel, String operationCode);

        public void onDatabaseError(DatabaseError databaseError);
    }
}
