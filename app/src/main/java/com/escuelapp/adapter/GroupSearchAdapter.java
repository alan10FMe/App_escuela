package com.escuelapp.adapter;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.escuelapp.GroupSearchActivity;
import com.escuelapp.R;
import com.escuelapp.model.Group;
import com.escuelapp.model.Groups;

import java.util.List;

/**
 * Created by alan on 11/8/16.
 */
public class GroupSearchAdapter extends RecyclerView.Adapter<GroupSearchAdapter.ViewHolder> {

    private List<Group> groupDataSet;
    private GroupSearchAdapter.RecyclerCallback callback;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout mainLinear;
        protected TextView nameGroupText;
        protected AppCompatCheckBox selectCheckBox;

        public ViewHolder(View view) {
            super(view);
            nameGroupText = (TextView) view.findViewById(R.id.name_text);
            selectCheckBox = (AppCompatCheckBox) view.findViewById(R.id.select_check_box);
            mainLinear = (LinearLayout) view.findViewById(R.id.main_linear);
        }
    }

    public GroupSearchAdapter(List<Group> groupDataSet, GroupSearchAdapter.RecyclerCallback callback) {
        this.groupDataSet = groupDataSet;
        this.callback = callback;
    }

    public GroupSearchAdapter(List<Group> groupDataSet) {
        this.groupDataSet = groupDataSet;
    }

    @Override
    public GroupSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_select_group, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final GroupSearchAdapter.ViewHolder holder, final int position) {
        holder.nameGroupText.setText(groupDataSet.get(position).getName());
        holder.selectCheckBox.setChecked(groupDataSet.get(position).isSelected());
        holder.selectCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean selected = !groupDataSet.get(position).isSelected();
                groupDataSet.get(position).setSelected(selected);
                callback.clickRow(selected);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupDataSet != null ? groupDataSet.size() : 0;
    }

    public interface RecyclerCallback {
        public void clickRow(boolean selected);
    }
}
