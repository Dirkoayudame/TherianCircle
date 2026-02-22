package com.theriancircle.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.theriancircle.app.data.local.entity.PostEntity;

import java.util.List;

@Dao
public interface PostDao {
    @Query("SELECT * FROM posts")
    List<PostEntity> getAll();

    @Query("SELECT * FROM posts WHERE id = :postId LIMIT 1")
    PostEntity findById(String postId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PostEntity post);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PostEntity> posts);

    @Query("DELETE FROM posts")
    void clearAll();
}
