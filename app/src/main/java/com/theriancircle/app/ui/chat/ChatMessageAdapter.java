package com.theriancircle.app.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theriancircle.app.R;
import com.theriancircle.app.data.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatViewHolder> {
    private final List<Message> items = new ArrayList<>();

    public void submitItems(List<Message> messages) {
        items.clear();
        items.addAll(messages);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = items.get(position);
        holder.author.setText(message.getAuthorName());
        holder.text.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        final TextView author;
        final TextView text;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.messageAuthor);
            text = itemView.findViewById(R.id.messageText);
        }
    }
}
