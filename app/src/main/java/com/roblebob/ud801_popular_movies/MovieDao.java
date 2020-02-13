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
    @Query(value = "SELECT `key` FROM Movie WHERE movieID = 1")
    LiveData< String> loadPrime();

    @Query(value = "SELECT * FROM Movie WHERE movieID > 1 ORDER BY fav DESC, popularVAL DESC, voteAVG DESC, voteCNT DESC")
    LiveData< List<Movie>> loadPopularMoviesLive();

    @Query(value = "SELECT * FROM Movie WHERE movieID > 1 ORDER BY fav DESC, voteAVG DESC, popularVAL DESC, voteCNT DESC")
    LiveData< List<Movie>> loadTopRatedMoviesLive();

    @Query(value = "SELECT * FROM Movie WHERE movieID = :movieID")
    LiveData<Movie> loadMovieByMovieIDLive(int movieID);



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Movie movie);


    @Delete
    void delete(Movie movie);


    @Query(value = "SELECT COUNT(*) FROM Movie WHERE movieID > 1 ")
    LiveData< Integer> countMovies();


    // (0)->"popular"  ,  (1)->"top_rated"
    @Query(value = "SELECT voteCNT FROM Movie WHERE movieID = 1")
    LiveData<Integer> loadOrderedbyTabPositionLive();


    @Query(value = "UPDATE Movie SET voteCNT = :pos WHERE movieID = 1;")
    void updateOrderedbyTabPosition(int pos);


}
