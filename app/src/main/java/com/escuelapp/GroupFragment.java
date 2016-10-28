package com.escuelapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.escuelapp.adapter.GroupAdapter;
import com.escuelapp.database.DatabaseAccess;
import com.escuelapp.model.Group;
import com.escuelapp.utility.Constants;
import com.escuelapp.utility.Transformer;
import com.escuelapp.utility.Utility;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment implements View.OnClickListener {

    private CoordinatorLayout coordinatorLayout;
    private ChildEventListener childEventListenerCreated;
    private ChildEventListener childEventListenerParticipating;
    private RecyclerView recycleView;
    private GroupAdapter groupCreatedAdapter;
    private List<Group> groupsCreatedList;
    private GroupAdapter groupParticipantAdapter;
    private List<Group> groupsParticipantList;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton actionButton;
    private BottomNavigationView bottomNavigationView;
    private TextView textTitleLinear;
    private boolean isCreation = true;
    private Query groupsCreated;
    private Query groupsParticipating;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public GroupFragment() {
    }

    public static GroupFragment newInstance() {
        return new GroupFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        coordinatorLayout = (CoordinatorLayout) inflater.inflate(R.layout.fragment_group, container, false);
        initializeScreen();
        initializeRecyclerView();
        initializeBottom();
        connectListenerGroupsCreated();
        connectListenerGroupsParticipant();
        return coordinatorLayout;
    }

    private void initializeScreen() {
        actionButton = (FloatingActionButton) coordinatorLayout.findViewById(R.id.action_button);
        actionButton.setOnClickListener(this);
        textTitleLinear = (TextView) coordinatorLayout.findViewById(R.id.title_recycler_text);
    }

    private void initializeRecyclerView() {
        recycleView = (RecyclerView) coordinatorLayout.findViewById(R.id.group_recycle);
        groupsCreatedList = new ArrayList<>();
        groupsParticipantList = new ArrayList<>();
        groupCreatedAdapter = new GroupAdapter(groupsCreatedList, getActivity());
        groupParticipantAdapter = new GroupAdapter(groupsParticipantList, getContext());
        layoutManager = new LinearLayoutManager(getContext());
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(groupCreatedAdapter);
        recycleView.setNestedScrollingEnabled(false);
    }

    private void initializeBottom() {
        bottomNavigationView = (BottomNavigationView) coordinatorLayout.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.button_action_created:
                        actionGroupCreatedClicked();
                        break;
                    case R.id.button_action_participant:
                        actionGroupParticipantClicked();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void actionGroupCreatedClicked() {
        recycleView.swapAdapter(groupCreatedAdapter, true);
        textTitleLinear.setText(R.string.group_text_created_groups);
        actionButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_add));
        isCreation = true;
    }

    private void actionGroupParticipantClicked() {
        recycleView.swapAdapter(groupParticipantAdapter, true);
        textTitleLinear.setText(R.string.group_text_participant_groups);
        actionButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_search));
        isCreation = false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.action_button && isCreation) {
            createNewGroup();
        } else if (view.getId() == R.id.action_button && !isCreation) {
            searchGroup();
        }
    }

    private void createNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.group_text_dialog_title)
                .setView(R.layout.dialog_create_group)
                .setPositiveButton(R.string.group_text_dialog_button_save, null)
                .setNegativeButton(R.string.group_text_dialog_button_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button buttonCreate = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                buttonCreate.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        EditText editText = (EditText) ((AlertDialog) dialog).findViewById(R.id.group_edit);
                        TextInputLayout textInputLayout = (TextInputLayout) ((AlertDialog) dialog)
                                .findViewById(R.id.group_input_layout);
                        textInputLayout.setError(null);
                        if (validateText(editText)) {
                            dialog.dismiss();
                            textInputLayout.setErrorEnabled(false);
                            createGroup(editText.getText().toString().trim());
                        } else {
                            textInputLayout.setError(getString(R.string.group_text_dialog_error));
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    private void createGroup(String groupName) {
        Group group = new Group();
        group.setName(groupName.trim());
        group.setCreator(Utility.getUserUid(getContext()));
        group.setCreatorName(Utility.getUserName(getContext()));
        DatabaseAccess.saveNewGroup(group, Utility.getUserUid(getContext()));
        displaySnack(getString(R.string.group_text_created));
    }

    private void searchGroup() {
        startActivity(new Intent(getContext(), GroupSearchActivity.class));
    }

    private boolean validateText(EditText editText) {
        return !TextUtils.isEmpty(editText.getText().toString().trim());
    }

    private void displaySnack(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }


    private void connectListenerGroupsCreated() {
        if (groupsCreated == null) {
            groupsCreated = databaseReference
                    .child(Constants.NODE_USERS)
                    .child(Utility.getUserUid(getContext()))
                    .child(Constants.NODE_GROUPS_CREATED)
                    .orderByChild(Constants.TAG_NAME);
        }
        if (childEventListenerCreated == null) {
            childEventListenerCreated = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    groupsCreatedList.add(Transformer.dataSnapshotToGroup(dataSnapshot));
                    groupCreatedAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    //No action
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    //No action
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    //No action
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //No action
                }
            };
        }
        groupsCreated.addChildEventListener(childEventListenerCreated);
    }

    private void connectListenerGroupsParticipant() {
        if (groupsParticipating == null) {
            groupsParticipating = databaseReference
                    .child(Constants.NODE_USERS)
                    .child(Utility.getUserUid(getContext()))
                    .child(Constants.NODE_GROUPS_PARTICIPANT)
                    .orderByChild(Constants.TAG_NAME);
        }

        if (childEventListenerParticipating == null)
            childEventListenerParticipating = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    groupsParticipantList.add(Transformer.dataSnapshotToGroup(dataSnapshot));
                    groupParticipantAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    //No action
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    //No action
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    //No action
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //No action
                }
            };
        groupsParticipating.addChildEventListener(childEventListenerParticipating);
    }
}
