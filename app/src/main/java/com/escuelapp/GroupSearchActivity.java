package com.escuelapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.escuelapp.adapter.GroupSearchAdapter;
import com.escuelapp.database.DatabaseAccess;
import com.escuelapp.model.BaseModel;
import com.escuelapp.model.Group;
import com.escuelapp.model.Groups;
import com.escuelapp.model.User;
import com.escuelapp.utility.Constants;
import com.escuelapp.utility.Utility;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class GroupSearchActivity extends BaseAppActivity implements SearchView.OnQueryTextListener,
        DatabaseAccess.OnDatabaseResponse, GroupSearchAdapter.RecyclerCallback {

    private RecyclerView recyclerView;
    private List<Group> dataGroupSet;
    private GroupSearchAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private Context context;
    private MenuItem menuRequest;
    private int rowsSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search);
        context = this;
        initializeComponents();
        initializeRecyclerView();
    }

    private void initializeComponents() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        ((SearchView) findViewById(R.id.search_view)).setOnQueryTextListener(this);
    }

    private void initializeRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.group_recycle);
        dataGroupSet = new ArrayList<>();
        adapter = new GroupSearchAdapter(dataGroupSet, this);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.request_button:
                requestEntrance();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestEntrance() {
        User user = new User();
        user.setUid(Utility.getUserUid(context));
        user.setName(Utility.getUserName(context));
        Groups groupsSelected = new Groups();
        for (Group group : dataGroupSet) {
            if (group.isSelected()) {
                groupsSelected.getGroups().add(group);
            }
        }
        DatabaseAccess.requestAccessGroups(groupsSelected, user);
        finish();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        menuRequest.setVisible(false);
        if (query.equals(Utility.getUserEmail(context))) {
            DatabaseAccess.findGroupsByEmailOwner(query, this);
        } else {
            Toast.makeText(context, "No el mismo correo", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_search_menu, menu);
        menuRequest = menu.getItem(0);
        menuRequest.setVisible(false);
        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public void onDatabaseResponse(BaseModel baseModel, String operationCode) {
        switch (operationCode) {
            case Constants.DB_ACTION_SEARCH_GROUPS_CREATED:
                updateGroups(baseModel);
                break;
            default:
                break;
        }
    }

    private void updateGroups(BaseModel baseModel) {
        dataGroupSet.clear();
        dataGroupSet.addAll(((Groups) baseModel).getGroups());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDatabaseError(DatabaseError databaseError) {

    }

    @Override
    public void clickRow(boolean selected) {
        if (selected) {
            rowsSelected += 1;
        } else {
            rowsSelected -= 1;
        }
        menuRequest.setVisible(rowsSelected > 0);
    }

}
