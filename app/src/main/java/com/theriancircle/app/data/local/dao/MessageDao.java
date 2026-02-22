package com.theriancircle.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.theriancircle.app.data.local.entity.MessageEntity;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    List<MessageEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MessageEntity message);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MessageEntity> messages);

    @Query("DELETE FROM messages")
    void clearAll();
}
