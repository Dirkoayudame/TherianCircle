package com.theriancircle.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "posts")
public class PostEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String authorName;
    public String authorSpecies;
    public String content;
    public int likesCount;
    public int likedByMe;
    public int commentsCount;

    public PostEntity(
            @NonNull String id,
            String authorName,
            String authorSpecies,
            String content,
            int likesCount,
            int likedByMe,
            int commentsCount
    ) {
        this.id = id;
        this.authorName = authorName;
        this.authorSpecies = authorSpecies;
        this.content = content;
        this.likesCount = likesCount;
        this.likedByMe = likedByMe;
        this.commentsCount = commentsCount;
    }
}
