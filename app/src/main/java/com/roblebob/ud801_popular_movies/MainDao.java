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
public interface MainDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)     void insert(Main main);
    @Update(onConflict = OnConflictStrategy.REPLACE)    void update(Main main);
    @Delete                                             void delete(Main main);


    @Query("SELECT * FROM Main ORDER BY favorite DESC,    CASE (SELECT `value` FROM AppState WHERE `key` = 'order') WHEN 'popular' THEN popularVAL WHEN 'top_rated' THEN voteAVG END DESC,      CASE (SELECT `value` FROM AppState WHERE `key` = 'order') WHEN 'popular' THEN voteAVG WHEN 'top_rated' THEN popularVAL  END DESC, voteCNT DESC")
    LiveData< List<Main>> loadMovieListLive();

    @Query(value = "SELECT * FROM Main ORDER BY favorite DESC, popularVAL DESC, voteAVG DESC, voteCNT DESC")
    LiveData< List<Main>> loadPopularMovieListLive();

    @Query(value = "SELECT * FROM Main ORDER BY favorite DESC, voteAVG DESC, popularVAL DESC, voteCNT DESC")
    LiveData< List<Main>> loadTopRatedMovieListLive();

    @Query(value = "SELECT * FROM Main WHERE ID = :ID")
    LiveData<Main> loadMovieLive(int ID);

    @Query(value = "SELECT COUNT(*) FROM Main WHERE ID > 1 ")
    LiveData< Integer> loadMovieCountLive();

    @Query(value = "UPDATE Main SET `favorite` = :favoriteFLAG WHERE ID = :ID")
    void updateFav(int ID, boolean favoriteFLAG);

    @Query(value = "UPDATE Main SET `favorite` = (CASE (SELECT favorite FROM Main WHERE ID = :ID) WHEN 0 THEN 1 WHEN 1 THEN 0 END) WHERE ID = :ID")
    void inverseFavorite(int ID);
}
