package com.roblebob.ud801_popular_movies;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface MovieExtraDao {

    @Query("SELECT * FROM MovieExtra WHERE MID = :MID")
    LiveData< List< MovieExtra>> loadExtrasByMID( int MID);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertExtra(MovieExtra movieExtra);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateExtra(MovieExtra movieExtra);

    @Delete
    void deleteExtra(MovieExtra movieExtra);
}
