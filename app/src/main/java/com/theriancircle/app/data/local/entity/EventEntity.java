package com.theriancircle.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class EventEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String title;
    public String location;
    public String dateLabel;
    public int attendeesCount;
    public int attendingByMe;

    public EventEntity(
            @NonNull String id,
            String title,
            String location,
            String dateLabel,
            int attendeesCount,
            int attendingByMe
    ) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.dateLabel = dateLabel;
        this.attendeesCount = attendeesCount;
        this.attendingByMe = attendingByMe;
    }
}
