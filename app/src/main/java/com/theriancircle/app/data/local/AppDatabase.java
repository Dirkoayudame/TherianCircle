package com.theriancircle.app.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.theriancircle.app.data.local.dao.CommentDao;
import com.theriancircle.app.data.local.dao.EventDao;
import com.theriancircle.app.data.local.dao.GroupDao;
import com.theriancircle.app.data.local.dao.MessageDao;
import com.theriancircle.app.data.local.dao.PostDao;
import com.theriancircle.app.data.local.entity.CommentEntity;
import com.theriancircle.app.data.local.entity.EventEntity;
import com.theriancircle.app.data.local.entity.GroupEntity;
import com.theriancircle.app.data.local.entity.MessageEntity;
import com.theriancircle.app.data.local.entity.PostEntity;

@Database(
        entities = {PostEntity.class, GroupEntity.class, EventEntity.class, MessageEntity.class, CommentEntity.class},
        version = 3,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;

    public abstract PostDao postDao();
    public abstract GroupDao groupDao();
    public abstract EventDao eventDao();
    public abstract MessageDao messageDao();
    public abstract CommentDao commentDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "therian_circle.db"
                            )
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return instance;
    }
}
