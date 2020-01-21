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
public interface MovieDao {

    @Query("SELECT * FROM Movie ORDER BY popularity DESC")
    LiveData<List< Movie>> loadPopularMovies();

    @Query("SELECT * FROM Movie ORDER BY vote_average DESC")
    LiveData<List< Movie>> loadTopRatedMovies();

    @Query("SELECT * FROM Movie WHERE id = :id")
    LiveData< Movie> loadMovieById(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);
}
