package com.roblebob.ud801_popular_movies;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import java.io.IOException;

import static com.roblebob.ud801_popular_movies.AppUtilities.getResponseFromHttpUrl;

public class AppStateRepository {
    private AppDatabase appDatabase;

    public AppStateRepository(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        //setOrder( AppUtilities.ORDER.get(0));
        //AppExecutors .getInstance() .diskIO() .execute( () -> appDatabase .appStateDao() .insert( new AppState("api_key", null)));

    }

    public LiveData< String> getOrderLive()  { return appDatabase .appStateDao() .loadOrder(); }
    public LiveData< String> getApiKeyLive() { return appDatabase .appStateDao() .loadApiKey(); }


    public void setOrder(String order) { AppExecutors .getInstance() .diskIO() .execute(  () -> {
        appDatabase.appStateDao().update(new AppState("order", order));
        Log.e(this.getClass().getSimpleName(), "-------->\t" + order);
    }); }


    public void setApiKey( String apiKey) {

        Log.e( this.getClass().getSimpleName() + "::setApiKey()\t", "\t--->\t" + apiKey);

        AppExecutors.getInstance().networkIO().execute( () -> {
            try {
                // dummy request simply to verify the apiKey
                String response = getResponseFromHttpUrl("https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey);
                // IF no IOException is thrown THEN:
                AppExecutors .getInstance() .diskIO() .execute( () -> appDatabase .appStateDao() .insert( new AppState("api_key", apiKey)));
                Log.e( this.getClass().getSimpleName() + "::setApiKey(\t" + apiKey + "\t)\t", "\taccepted");


            } catch (IOException e) {
                AppExecutors .getInstance() .diskIO() .execute( () -> appDatabase .appStateDao() .insert( new AppState("api_key", null)));
                Log.e( this.getClass().getSimpleName() + "::setApiKey(\t" + apiKey + "\t)\t", "\trejected");
            }
        });
    }
}
