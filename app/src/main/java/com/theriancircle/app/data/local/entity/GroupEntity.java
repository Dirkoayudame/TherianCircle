package com.theriancircle.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "groups")
public class GroupEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public int membersCount;
    public int joinedByMe;

    public GroupEntity(@NonNull String id, String name, int membersCount, int joinedByMe) {
        this.id = id;
        this.name = name;
        this.membersCount = membersCount;
        this.joinedByMe = joinedByMe;
    }
}
