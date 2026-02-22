package com.theriancircle.app.ui.groups;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.theriancircle.app.R;
import com.theriancircle.app.data.model.CommunityGroup;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    public interface GroupActions {
        void onToggleMembership(CommunityGroup group);
    }

    private final List<CommunityGroup> items = new ArrayList<>();
    private final GroupActions actions;

    public GroupAdapter(GroupActions actions) {
        this.actions = actions;
    }

    public void submitItems(List<CommunityGroup> groups) {
        items.clear();
        items.addAll(groups);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        CommunityGroup group = items.get(position);
        holder.groupName.setText(group.getName());
        holder.groupMembers.setText(holder.itemView.getContext().getString(
                R.string.members_count,
                group.getMembersCount()
        ));
        holder.actionButton.setText(group.isJoinedByMe() ? R.string.btn_leave : R.string.btn_join);
        holder.actionButton.setOnClickListener(v -> actions.onToggleMembership(group));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        final TextView groupName;
        final TextView groupMembers;
        final MaterialButton actionButton;

        GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            groupMembers = itemView.findViewById(R.id.groupMembers);
            actionButton = itemView.findViewById(R.id.groupActionButton);
        }
    }
}
