package com.roblebob.ud801_popular_movies;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

import java.util.List;


@Dao
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)     void insert(Movie movie);
    @Update(onConflict = OnConflictStrategy.REPLACE)    void update(Movie movie);
    @Delete                                             void delete(Movie movie);


    @Query("SELECT * FROM Movie WHERE movieID > 1 ORDER BY   CASE WHEN  (SELECT voteCNT WHERE movieID = 1) = 0 THEN popularVAL WHEN (SELECT voteCNT WHERE movieID = 1) = 1 THEN voteAVG END DESC")
    LiveData< List<Movie>> loadMovieListLive();

    @Query(value = "SELECT * FROM Movie WHERE movieID > 1 ORDER BY fav DESC, popularVAL DESC, voteAVG DESC, voteCNT DESC")
    LiveData< List<Movie>> loadPopularMovieListLive();

    @Query(value = "SELECT * FROM Movie WHERE movieID > 1 ORDER BY fav DESC, voteAVG DESC, popularVAL DESC, voteCNT DESC")
    LiveData< List<Movie>> loadTopRatedMovieListLive();

    @Query(value = "SELECT * FROM Movie WHERE movieID = :movieID")
    LiveData<Movie> loadMovieLive(int movieID);

    @Query(value = "SELECT COUNT(*) FROM Movie WHERE movieID > 1 ")
    LiveData< Integer> countMovies();


    /* *********************************************************************************************
     *   a p i K e y
     */
    @Query(value = "UPDATE Movie SET `key` = :key WHERE movieID = 1;")
    void updateApiKey(String key);


    /* *********************************************************************************************
     *   o r d e r e d b y T a b P o s i t i o n             (0)->"popular"  ,  (1)->"top_rated"
     */
    @Query(value = "SELECT voteCNT FROM Movie WHERE movieID = 1")
    LiveData<Integer> loadOrderedbyTabPositionLive();

    @Query(value = "UPDATE Movie SET voteCNT = :pos WHERE movieID = 1;")
    void updateOrderedbyTabPosition(int pos);
}
