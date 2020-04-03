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


    @Query("SELECT value FROM AppState WHERE `key` = :key")
    LiveData< String> loadState(String key);

    @Query("SELECT value FROM AppState WHERE `key` = 'order'")
    LiveData< String> loadOrder();

    @Query("SELECT value FROM AppState WHERE `key` = 'api_key'")
    LiveData< String> loadApiKey();

    @Query("SELECT value FROM AppState WHERE `key` = 'last_position'")
    LiveData< String> loadLastPosition();


    @Query("UPDATE AppState SET `value` =:order WHERE `key` = 'order'")
    void updateOrder(String order);

    @Query("UPDATE AppState SET `value` =:apiKey WHERE `key` = 'api_key'")
    void updateApiKey(String apiKey);

    @Insert(onConflict = OnConflictStrategy.REPLACE)    void insert(AppState appState);
    @Update(onConflict = OnConflictStrategy.REPLACE)    void update(AppState appState);
    @Delete                                             void delete(AppState appState);
}









