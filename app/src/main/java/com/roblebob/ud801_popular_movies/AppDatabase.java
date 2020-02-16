package com.roblebob.ud801_popular_movies;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Context;


@Database( entities = {  Movie.class,  Xtra.class },   version = 1,   exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {     /* singleton-pattern */

    private static final String DATABASE_NAME  = "MovieDB149";
    private static AppDatabase  sInstance;
    private static final Object LOCK  = new Object();
    public static AppDatabase   getInstance( Context context) {

        if ( sInstance == null) {  synchronized (LOCK) {
            sInstance = Room .databaseBuilder(
                    context.getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME
            ).build();
        }}
        return sInstance;
    }

    public abstract MovieDao movieDao();
    public abstract XtraDao xtraDao();
}
