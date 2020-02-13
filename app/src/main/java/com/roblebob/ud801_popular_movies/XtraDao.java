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
public interface XtraDao {

    @Query(value = "SELECT value FROM Xtra WHERE ID = 1")
    LiveData< String> loadPrime();

    @Query("SELECT * FROM Xtra WHERE (ID / 100) = :movieID")
    LiveData< List<Xtra>> loadXtraList(int movieID);

    @Query("SELECT * FROM Xtra WHERE ((ID % 100) < 18) and (ID / 100) = :movieID")
    LiveData< List<Xtra>> loadNonlinksXtraList(int movieID);

    @Query("SELECT * FROM Xtra WHERE ((ID % 100) >= 18) and (ID / 100) = :movieID")
    LiveData< List<Xtra>> loadLinksXtraList(int movieID);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert( Xtra xtra);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update( Xtra xtra);

    @Delete
    void delete( Xtra xtra);

    @Query(value = "SELECT COUNT( DISTINCT ( (ID / 100))) FROM Xtra WHERE ID > 1")
    LiveData< Integer> countMovies();
}
