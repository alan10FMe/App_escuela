package com.escuelapp.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.escuelapp.R;
import com.escuelapp.database.DatabaseAccess;
import com.escuelapp.model.Group;
import com.escuelapp.model.User;

import java.util.List;

/**
 * Created by alan on 11/12/16.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> userDataSet;
    private Context context;
    private String groupUid;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout mainLinear;
        protected TextView nameGroupText;
        protected AppCompatButton acceptButton;

        public ViewHolder(View view) {
            super(view);
            nameGroupText = (TextView) view.findViewById(R.id.name_text);
            acceptButton = (AppCompatButton) view.findViewById(R.id.accept_button);
        }
    }

    public UserAdapter(List<User> userDataSet, Context context, String groupUid) {
        this.userDataSet = userDataSet;
        this.context = context;
        this.groupUid = groupUid;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_group, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final UserAdapter.ViewHolder holder, final int position) {
        holder.nameGroupText.setText(userDataSet.get(position).getName());
        if ((userDataSet.get(position).isRequester())) {
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseAccess.acceptRequestUser(userDataSet.get(position), groupUid);
                }
            });
        } else {
            holder.acceptButton.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return userDataSet != null ? userDataSet.size() : 0;
    }
}
