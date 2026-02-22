package com.theriancircle.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comments")
public class CommentEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String postId;
    public String authorName;
    public String text;
    public long timestamp;

    public CommentEntity(@NonNull String id, String postId, String authorName, String text, long timestamp) {
        this.id = id;
        this.postId = postId;
        this.authorName = authorName;
        this.text = text;
        this.timestamp = timestamp;
    }
}
