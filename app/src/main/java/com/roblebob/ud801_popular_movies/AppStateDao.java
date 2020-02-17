package com.roblebob.ud801_popular_movies;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AppStateDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)     void insert(AppState appState);
    @Update(onConflict = OnConflictStrategy.REPLACE)    void update(AppState appState);
    @Delete                                             void delete(Main main);

    @Query("SELECT value FROM AppState WHERE `key` = :key")
    LiveData< String> loadState(String key);
}









