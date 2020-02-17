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

    @Query("SELECT * FROM Detail WHERE parent = :movieId AND `order` < 10")
    LiveData< List<Detail>> loadList(int movieId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert( Detail detail);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update( Detail detail);

    @Delete
    void delete( Detail detail);

    @Query(value = "SELECT COUNT( DISTINCT parent) FROM Detail")
    LiveData< Integer> countMovies();

}
