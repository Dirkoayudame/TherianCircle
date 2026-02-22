package com.theriancircle.app.ui.feed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.theriancircle.app.R;
import com.theriancircle.app.data.model.Post;

import java.util.ArrayList;
import java.util.List;

public class FeedPostAdapter extends RecyclerView.Adapter<FeedPostAdapter.PostViewHolder> {
    public interface FeedPostActions {
        void onLikeClicked(Post post);
        void onCommentClicked(Post post);
    }

    private final List<Post> items = new ArrayList<>();
    private final FeedPostActions actions;

    public FeedPostAdapter(FeedPostActions actions) {
        this.actions = actions;
    }

    public void submitPosts(List<Post> posts) {
        items.clear();
        items.addAll(posts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feed_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = items.get(position);
        String author = post.getAuthor().getUsername() + " (" + post.getAuthor().getSpecies() + ")";
        holder.authorText.setText(author);
        holder.contentText.setText(post.getContent());
        String meta = holder.itemView.getContext().getString(
                R.string.likes_count,
                post.getLikesCount()
        ) + " · " + holder.itemView.getContext().getString(
                R.string.comments_count,
                post.getCommentsCount()
        );
        holder.metaText.setText(meta);
        holder.likeButton.setText(post.isLikedByMe() ? R.string.btn_unlike : R.string.btn_like);
        holder.likeButton.setOnClickListener(v -> actions.onLikeClicked(post));
        holder.commentButton.setOnClickListener(v -> actions.onCommentClicked(post));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        final TextView authorText;
        final TextView contentText;
        final TextView metaText;
        final MaterialButton likeButton;
        final MaterialButton commentButton;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            authorText = itemView.findViewById(R.id.postAuthor);
            contentText = itemView.findViewById(R.id.postContent);
            metaText = itemView.findViewById(R.id.postMeta);
            likeButton = itemView.findViewById(R.id.likeButton);
            commentButton = itemView.findViewById(R.id.commentButton);
        }
    }
}
