package com.escuelapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.escuelapp.adapter.UserAdapter;
import com.escuelapp.model.Group;
import com.escuelapp.model.User;
import com.escuelapp.utility.Constants;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class GroupAdministratorActivity extends BaseAppActivity {

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final static int INVITE_REQUEST = 84;
    private RecyclerView requesterRecyler;
    private RecyclerView participantRecycler;
    private List<User> requestersList;
    private List<User> participantsList;
    private UserAdapter requestersAdapter;
    private UserAdapter participantsAdapter;
    private LinearLayoutManager requesterLayoutManager;
    private LinearLayoutManager participantLayoutManager;
    private Context context;
    private String groupUid;
    private ChildEventListener childRequesterEventListener;
    private ChildEventListener childParticipantEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_administrator);
        context = this;
        if (getIntent() == null && getIntent().getExtras() == null) {
            finish();
        }
        groupUid = getIntent().getExtras().getCharSequence(Constants.EXTRA_GROUP_UID).toString();
        initializeComponents();
        initializeRecyclerViews();
        connectRequestersListener();
        connectParticipantsListener();
    }

    private void initializeComponents() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            getSupportActionBar().setTitle(getIntent().getExtras().getCharSequence(Constants.EXTRA_GROUP_NAME));
        }
    }

    private void initializeRecyclerViews() {
        requesterRecyler = (RecyclerView) findViewById(R.id.requester_recycle);
        participantRecycler = (RecyclerView) findViewById(R.id.participant_recycle);
        requestersList = new ArrayList<>();
        participantsList = new ArrayList<>();
        requestersAdapter = new UserAdapter(requestersList, context, groupUid);
        participantsAdapter = new UserAdapter(participantsList, context, groupUid);
        requesterLayoutManager = new LinearLayoutManager(context);
        participantLayoutManager = new LinearLayoutManager(context);
        requesterRecyler.setLayoutManager(requesterLayoutManager);
        requesterRecyler.setAdapter(requestersAdapter);
        participantRecycler.setLayoutManager(participantLayoutManager);
        participantRecycler.setAdapter(participantsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.share_button:
                createInvitation();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createInvitation() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invite_text_title))
                .setMessage(getString(R.string.invite_text_message))
                .setDeepLink(Uri.parse(Constants.INVITATION_DEEP_LINK))
                .setCustomImage(Uri.parse(Constants.INVITATION_URL_IMAGE))
                .setCallToActionText(getText(R.string.invite_button_email))
                .build();
        startActivityForResult(intent, INVITE_REQUEST);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_administrator_menu, menu);
        return true;
    }

    private void connectRequestersListener() {
        Query requestersQuery = databaseReference
                .child(Constants.NODE_GROUPS)
                .child(groupUid)
                .child(Constants.NODE_USERS_REQUESTERS)
                .orderByChild(Constants.TAG_NAME);
        childRequesterEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                User user = new User((HashMap<String, Object>) dataSnapshot.getValue());
                user.setUid(dataSnapshot.getKey());
                user.setRequester(true);
                requestersList.add(user);
                requestersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //No action
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for (Iterator<User> iterator = requestersList.listIterator(); iterator.hasNext(); ) {
                    User user = iterator.next();
                    if (user.getUid().equals(dataSnapshot.getKey())) {
                        iterator.remove();
                        break;
                    }
                }
                requestersAdapter.notifyDataSetChanged();
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
        requestersQuery.addChildEventListener(childRequesterEventListener);
    }

    private void connectParticipantsListener() {
        Query participantsQuery = databaseReference
                .child(Constants.NODE_GROUPS)
                .child(groupUid)
                .child(Constants.NODE_USERS_PARTICIPANTS)
                .orderByChild(Constants.TAG_NAME);
        childParticipantEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                User user = new User((HashMap<String, Object>) dataSnapshot.getValue());
                user.setUid(dataSnapshot.getKey());
                user.setRequester(false);
                participantsList.add(user);
                participantsAdapter.notifyDataSetChanged();
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
        participantsQuery.addChildEventListener(childParticipantEventListener);
    }
}
