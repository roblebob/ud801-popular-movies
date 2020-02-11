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

    //@Query(value = "SELECT value FROM Xtra WHERE ID = 1")
    @Query(value = "SELECT posterID FROM Movie WHERE movieID = 1")
    LiveData< String> loadApikey();

    @Query("SELECT * FROM Movie ORDER BY popularVAL DESC, fav DESC, voteAVG DESC")
    LiveData< List<Movie>> loadPopularMoviesLive();

    @Query("SELECT * FROM Movie ORDER BY voteAVG DESC, fav DESC, popularVAL DESC")
    LiveData< List<Movie>> loadTopRatedMoviesLive();

    @Query("SELECT * FROM Movie WHERE movieID = :movieID")
    LiveData<Movie> loadMovieByMovieIDLive(int movieID);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Movie movie);

    @Delete
    void delete(Movie movie);
}
