package com.theriancircle.app.data.repository;

import android.content.Context;

import com.theriancircle.app.data.source.InMemoryCache;
import com.theriancircle.app.data.source.MockRemoteDataSource;
import com.theriancircle.app.data.source.RoomLocalDataSource;

public final class RepositoryProvider {
    private static AppRepository repository;

    private RepositoryProvider() {
    }

    public static AppRepository getRepository(Context context) {
        if (repository == null) {
            repository = new AppRepository(
                    new InMemoryCache(),
                    new RoomLocalDataSource(context),
                    new MockRemoteDataSource()
            );
        }
        return repository;
    }
}
