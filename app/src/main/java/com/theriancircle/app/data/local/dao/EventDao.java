package com.theriancircle.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.theriancircle.app.data.local.entity.EventEntity;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM events")
    List<EventEntity> getAll();

    @Query("SELECT * FROM events WHERE id = :eventId LIMIT 1")
    EventEntity findById(String eventId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EventEntity event);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<EventEntity> events);

    @Query("DELETE FROM events")
    void clearAll();
}
