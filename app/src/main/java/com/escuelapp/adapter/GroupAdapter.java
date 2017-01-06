package com.escuelapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.escuelapp.GroupAdministratorActivity;
import com.escuelapp.R;
import com.escuelapp.model.Group;
import com.escuelapp.utility.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 10/30/16.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private List<Group> groupDataSet;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView groupTextView;
        protected LinearLayout mainLinear;

        public ViewHolder(View view) {
            super(view);
            groupTextView = (TextView) view.findViewById(R.id.group_name_text);
            mainLinear = (LinearLayout) view.findViewById(R.id.main_linear);
        }
    }

    public GroupAdapter(List<Group> groupDataSet, Context context) {
        this.groupDataSet = groupDataSet;
        this.context = context;
    }

    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_group, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GroupAdapter.ViewHolder holder, final int position) {
        holder.groupTextView.setText(groupDataSet.get(position).getName());
        holder.mainLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GroupAdministratorActivity.class);
                intent.putExtra(Constants.EXTRA_GROUP_NAME, groupDataSet.get(position).getName());
                intent.putExtra(Constants.EXTRA_GROUP_UID, groupDataSet.get(position).getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupDataSet != null ? groupDataSet.size() : 0;
    }

    public void addElements(List<Group> newGroups) {
        groupDataSet.addAll(newGroups);
        this.notifyDataSetChanged();
    }

    public void addElements(Group newGroup) {
        groupDataSet.add(newGroup);
        this.notifyDataSetChanged();
    }

    public void replaceElements(List<Group> newGroups) {
        groupDataSet.clear();
        groupDataSet.addAll(newGroups);
        this.notifyDataSetChanged();
    }
}
