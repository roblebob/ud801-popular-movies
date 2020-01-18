package com.roblebob.ud801_popular_movies;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Context;
import androidx.room.TypeConverters;
import android.util.Log;


@Database(entities = {Movie.class, Genre.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "MovieDB2";

    /* singleton-pattern */
    private static AppDatabase sInstance;
    private static final Object LOCK = new Object();
    public static AppDatabase getInstance( Context context) {
        if ( sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room
                        .databaseBuilder(
                                context.getApplicationContext(),
                                AppDatabase.class,
                                AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Accessing the existing database instance");
        return sInstance;
    }

    public abstract MovieDao movieDao();
    public abstract GenreDao genreDao();
}
