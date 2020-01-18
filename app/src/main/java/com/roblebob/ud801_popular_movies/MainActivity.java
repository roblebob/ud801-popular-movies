package com.roblebob.ud801_popular_movies;

import androidx.appcompat.app.AppCompatActivity;


import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    AppDatabase appDatabase;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_main);
        appDatabase = AppDatabase .getInstance( getApplicationContext());

        ////////////////////////////////////////////////////////////////////////////////////////////
        for (String orderPath : NetworkUtils.ORDER_PATH_array) {


            final String ORDER_PATH = orderPath;
            Log .e(TAG, "----------->  " + ORDER_PATH);
            AppExecutors.getInstance().networkIO().execute(new Runnable() {
                @Override
                public void run() {


                    //----------------------------------------------------------------------------------
                    String json;
                    try {
                        URL url = new URL( NetworkUtils .buildUrl( ORDER_PATH, 1).toString());
                        json = NetworkUtils .getResponseFromHttpUrl( url);
                    } catch(IOException exception) { exception.printStackTrace(); json = null; }

                    int totalPages = 0;
                    if (json != null) if (json.length() != 0) {
                        try {
                            JSONObject o = new JSONObject(json);
                            totalPages = o.getInt("total_pages");
                        } catch (JSONException e) { e.printStackTrace(); }
                    }
                    final int TOTAL_PAGES = totalPages;

                    //----------------------------------------------------------------------------------
                    for (int page = 1; page <= TOTAL_PAGES; page++) {
                        if (page != 1) {
                            try {
                                URL url = new URL(NetworkUtils.buildUrl( ORDER_PATH, page).toString());
                                json = NetworkUtils.getResponseFromHttpUrl(url);
                            } catch (IOException exception) {
                                exception.printStackTrace();
                                json = null;
                            }
                        }

                        final String JSON = json;
                        if (JSON != null)
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        JSONObject o = new JSONObject(JSON);
                                        JSONArray O_Results = o.getJSONArray("results");
                                        for (int i = 0; i < O_Results.length(); i++) {

                                            long id                 = O_Results. getJSONObject(i) .getLong("id");
                                            double popularity       = O_Results. getJSONObject(i) .getDouble("popularity");
                                            long voteCount          = O_Results. getJSONObject(i) .getLong("vote_count");
                                            boolean video           = O_Results. getJSONObject(i) .getBoolean("video"); ;
                                            String posterPath       = O_Results. getJSONObject(i) .getString("poster_path");
                                            boolean adlult          = O_Results. getJSONObject(i) .getBoolean("adult");
                                            String backdropPath     = O_Results. getJSONObject(i) .getString("backdrop_path");
                                            String originalLanguage = O_Results. getJSONObject(i) .getString("original_language");
                                            String originalTitle    = O_Results. getJSONObject(i) .getString("original_title");
                                            String title            = O_Results. getJSONObject(i) .getString("title");
                                            double voteAverage      = O_Results. getJSONObject(i) .getDouble("vote_average");
                                            String overview         = O_Results. getJSONObject(i) .getString("overview");
                                            String releaseDate      = O_Results. getJSONObject(i) .getString("release_date");

                                            String genreIds         = O_Results. getJSONObject(i) .getJSONArray("genre_ids").toString();

                                            Movie movie = new Movie(id, popularity, voteCount, video, posterPath, adlult, backdropPath,
                                                    originalLanguage, originalTitle, genreIds, title, voteAverage, overview, releaseDate);

                                            appDatabase.movieDao().insertMovie( movie);

                                        }
                                        //------------------------------------------------------------------
                                    } catch (JSONException e) { e.printStackTrace(); }
                                    Log .e(TAG, "---->  finished:  " + JSON );
                                    finish();
                                }});
                    }
                    finish();
                }});
        }

        /* TODO:
            Picasso-ImageLibrary
                Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
            --------------------------------------------------------------------
            themoviedb.org API
                URI-path: (3 x parts concatenated):   <BASE> + <SIZE> + <IMAGE>
                        <BASE> = "http://image.tmdb.org/t/p/"
                        <SIZE> ∈ { "original/", "w92/", "w154/", "w185/", "w342/", "w500/", "w780/" }
                        <SIZE*> = "w185/"  (recommended)
                        <IMAGE> = "/<...>.jpg"
                URL-request:
                        "http://api.themoviedb.org/3/movie/popular?api_key=" + <API_KEY>
         */

    }
}
