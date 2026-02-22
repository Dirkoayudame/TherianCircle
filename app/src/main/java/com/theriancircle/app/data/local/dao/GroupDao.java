package com.theriancircle.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.theriancircle.app.data.local.entity.GroupEntity;

import java.util.List;

@Dao
public interface GroupDao {
    @Query("SELECT * FROM groups")
    List<GroupEntity> getAll();

    @Query("SELECT * FROM groups WHERE id = :groupId LIMIT 1")
    GroupEntity findById(String groupId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GroupEntity group);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<GroupEntity> groups);

    @Query("DELETE FROM groups")
    void clearAll();
}
