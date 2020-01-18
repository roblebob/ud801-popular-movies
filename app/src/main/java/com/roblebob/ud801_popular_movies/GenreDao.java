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
public interface GenreDao {

    @Query("SELECT * FROM Genre")
    LiveData< List< Genre>> loadAll();

    @Query("SELECT label FROM Genre WHERE :id = id")
    LiveData< String> loadGenreLabel(int id);

    @Insert
    void insertGenre(Genre genre);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateGenre(Genre genre);

    @Delete
    void deleteGenre(Genre genre);

}
