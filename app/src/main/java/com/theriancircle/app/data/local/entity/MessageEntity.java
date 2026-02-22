package com.theriancircle.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages")
public class MessageEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String authorName;
    public String text;
    public long timestamp;

    public MessageEntity(@NonNull String id, String authorName, String text, long timestamp) {
        this.id = id;
        this.authorName = authorName;
        this.text = text;
        this.timestamp = timestamp;
    }
}
