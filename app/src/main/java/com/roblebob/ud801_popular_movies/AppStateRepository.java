package com.roblebob.ud801_popular_movies;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import java.io.IOException;

import static com.roblebob.ud801_popular_movies.AppUtilities.getResponseFromHttpUrl;

public class AppStateRepository {
    private AppDatabase appDatabase;
    public AppStateRepository(AppDatabase appDatabase) { this.appDatabase = appDatabase; }

    public LiveData< String> getOrderLive()  { return appDatabase .appStateDao() .loadOrder(); }
    public LiveData< String> getApiKeyLive() { return appDatabase .appStateDao() .loadApiKey(); }

    public void setOrder(String order) {
        AppExecutors .getInstance() .diskIO() .execute(  () -> {
            if (order.equals("popular") || order.equals("top_rated"))  appDatabase.appStateDao().insert( new AppState("order", order));
        });
    }

    public void setApiKey( String apiKey) {
        AppExecutors.getInstance().networkIO().execute( () -> {
            try {
                // dummy request simply to verify the apiKey
                String response = getResponseFromHttpUrl("https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey);
                // IF no IOException is thrown THEN:
                AppExecutors .getInstance() .diskIO() .execute( () -> appDatabase .appStateDao() .insert( new AppState("api_key", apiKey)));

            } catch (IOException e) {
                AppExecutors .getInstance() .diskIO() .execute( () -> appDatabase .appStateDao() .insert( new AppState("api_key", null)));
            }
        });
    }
}
