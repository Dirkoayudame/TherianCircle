package com.theriancircle.app.ui.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theriancircle.app.R;

import java.util.ArrayList;
import java.util.List;

public class StateListAdapter extends RecyclerView.Adapter<StateListAdapter.ItemViewHolder> {
    private final List<ListItemModel> items = new ArrayList<>();

    public void submitItems(List<ListItemModel> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_state_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ListItemModel item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.subtitle.setText(item.getSubtitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView subtitle;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            subtitle = itemView.findViewById(R.id.itemSubtitle);
        }
    }
}
