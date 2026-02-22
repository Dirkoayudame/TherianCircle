package com.theriancircle.app.ui.events;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.theriancircle.app.R;
import com.theriancircle.app.data.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    public interface EventActions {
        void onToggleAttendance(Event event);
    }

    private final List<Event> items = new ArrayList<>();
    private final EventActions actions;

    public EventAdapter(EventActions actions) {
        this.actions = actions;
    }

    public void submitItems(List<Event> events) {
        items.clear();
        items.addAll(events);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = items.get(position);
        holder.title.setText(event.getTitle());
        holder.details.setText(event.getLocation() + "\n" + event.getDateLabel());
        holder.attendees.setText(holder.itemView.getContext().getString(
                R.string.attendees_count,
                event.getAttendeesCount()
        ));
        holder.actionButton.setText(event.isAttendingByMe() ? R.string.btn_cancel_rsvp : R.string.btn_rsvp);
        holder.actionButton.setOnClickListener(v -> actions.onToggleAttendance(event));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView details;
        final TextView attendees;
        final MaterialButton actionButton;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.eventTitle);
            details = itemView.findViewById(R.id.eventDetails);
            attendees = itemView.findViewById(R.id.eventAttendees);
            actionButton = itemView.findViewById(R.id.eventActionButton);
        }
    }
}
