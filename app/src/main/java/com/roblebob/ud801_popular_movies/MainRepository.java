package com.roblebob.ud801_popular_movies;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

/* **************************************************************************************************
 *
 */
public class MainRepository  {

    public List< String> ORDER() { return AppUtilities.ORDER;}
    private AppDatabase appDatabase;

    public MainRepository(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }


    public LiveData< List<Main>>  getListLive(String s)       { return this.appDatabase .mainDao() .loadMainListLive(); }
    public LiveData< List<Main>>  getPopularListLive()        { return this.appDatabase .mainDao() .loadPopularListLive(); }
    public LiveData< List<Main>>  getTopRatedListLive()       { return this.appDatabase .mainDao() .loadTopRatedListLive(); }
    public LiveData< Integer>     countMovies()               { return this.appDatabase .mainDao() .loadMainCountLive(); }
    public LiveData< Main>        getMainLive(int movieID)    { return this.appDatabase .mainDao() .loadMainLive( movieID); }

    public void inverseFavorite(int movieID) {
        AppExecutors.getInstance().diskIO().execute(() -> this.appDatabase.mainDao().inverseFavorite( movieID)); }


    /* *********************************************************************************************
     * Integrates all the basics of all movies accessible from the 'popular' and 'top_rated' orderedbys
     *
     * @param apiKey
     */
    @WorkerThread
    public void integrate(String apiKey) {  AppExecutors.getInstance().networkIO().execute(() -> {

            boolean condition = true;
            int page;
            for (String order : ORDER()) {         page = 0;
                do { try { try {                   page++;

                    JSONObject jsonObject = new JSONObject( Objects.requireNonNull(
                                                    AppUtilities .getResponseFromHttpUrl(
                                                        buildUrl(apiKey,order,page))));

                    condition = page == jsonObject.getInt("page")   &&   page < jsonObject.getInt("total_pages");

                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        insert( new Main(
                                jsonArray .getJSONObject( i).getInt("id"),
                                false,
                                false,
                                jsonArray .getJSONObject( i).getDouble("popularity"),
                                jsonArray .getJSONObject( i).getDouble("vote_average"),
                                jsonArray .getJSONObject( i).getInt("vote_count"),
                                jsonArray .getJSONObject( i).getString("poster_path")
                        ));
                    }
                } catch (JSONException e) { e.printStackTrace();  Log .e(this.getClass().getSimpleName(), "E R R O R  in  integrate():\tJSONException"); }
                } catch (IOException e)   { e.printStackTrace();  Log .e(this.getClass().getSimpleName(), "E R R O R  in  integrate():\tIOException"); }
                } while (condition);
            }
    });}



    @WorkerThread public void insert( Main main) { AppExecutors.getInstance().diskIO().execute( () -> this.appDatabase.mainDao().insert( main)); }




    /* *********************************************************************************************
     * Generates validated urls as strings for the  themoviedb.org  API
     *
     * @param orderedby    ∈  {"popular", "top_rated"}
     * @param page
     * @return url as a String, not as a URL !!!
     */
    public static String buildUrl( @NonNull String apiKey, @NonNull String orderedby,  int page) {

        try { return    new URL( Uri
                                    .parse( "https://api.themoviedb.org/3/movie")
                                    .buildUpon()
                                    .appendPath( orderedby)
                                    .appendQueryParameter("api_key", apiKey)
                                    .appendQueryParameter("language", "en-US")
                                    .appendQueryParameter("page", String .valueOf( page))
                                    .build()
                                    .toString()
                        ).toString();

        } catch ( MalformedURLException e) { e.printStackTrace(); return null; }
    }






}
