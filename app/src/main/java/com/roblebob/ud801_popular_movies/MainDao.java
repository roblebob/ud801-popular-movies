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


    @Query("SELECT * FROM Main ORDER BY favorite DESC,    CASE (SELECT `value` FROM AppState WHERE `key` = 'order') WHEN 'popular' THEN popularVAL WHEN 'top_rated' THEN voteAVG END DESC,       voteCNT DESC,      CASE (SELECT `value` FROM AppState WHERE `key` = 'order') WHEN 'popular' THEN voteAVG WHEN 'top_rated' THEN popularVAL  END DESC")
    LiveData< List<Main>> loadMainListLive();

    @Query(value = "SELECT * FROM Main ORDER BY favorite DESC, popularVAL DESC, voteAVG DESC, voteCNT DESC")
    LiveData< List<Main>> loadPopularListLive();

    @Query(value = "SELECT * FROM Main ORDER BY favorite DESC, voteAVG DESC, popularVAL DESC, voteCNT DESC")
    LiveData< List<Main>> loadTopRatedListLive();

    @Query(value = "SELECT * FROM Main WHERE movieID = :movieID")
    LiveData<Main> loadMainLive(int movieID);

    @Query(value = "SELECT COUNT(*) FROM Main")
    LiveData< Integer> loadMainCountLive();


    @Query(value = "UPDATE Main SET `favorite` = :favorite WHERE movieID = :movieID")
    void updateFavorite(int movieID, boolean favorite);

    @Query(value = "UPDATE Main SET `favorite` = (CASE (SELECT favorite FROM Main WHERE movieID = :movieID) WHEN 0 THEN 1 WHEN 1 THEN 0 END) WHERE movieID = :movieID")
    void inverseFavorite(int movieID);

    @Query(value = "UPDATE Main SET `detailed` = 1 WHERE movieID = :movieID")
    void setMovieIsDetails(int movieID);
}
