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
public interface DetailDao {

    @Query("SELECT * FROM Detail  WHERE movieID = :movieID ORDER BY `_ID`")
    LiveData< List<Detail>> loadList(int movieID);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert( Detail detail);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update( Detail detail);

    @Delete
    void delete( Detail detail);

    @Query(value = "SELECT COUNT( DISTINCT movieID) FROM Detail")
    LiveData< Integer> countMovies();

}
