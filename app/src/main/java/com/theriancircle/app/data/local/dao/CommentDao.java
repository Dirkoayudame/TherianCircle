package com.theriancircle.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.theriancircle.app.data.local.entity.CommentEntity;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY timestamp ASC")
    List<CommentEntity> getByPostId(String postId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CommentEntity comment);

    @Query("DELETE FROM comments")
    void clearAll();
}
