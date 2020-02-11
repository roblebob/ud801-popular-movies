package com.roblebob.ud801_popular_movies;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import static com.roblebob.ud801_popular_movies.AppUtilities.getResponseFromHttpUrl;

/* **************************************************************************************************
 *
 */
public class MovieRepository  {

    private MutableLiveData< Integer>  result = new MutableLiveData<>();
    public  MutableLiveData< Integer>  getResult()  { return  this.result; }
    private final AppDatabase appDatabase;
    private String apiKEY = null;


    public MovieRepository(@NonNull AppDatabase appDatabase) { this.appDatabase = appDatabase; }


    /* *********************************************************************************************
     * Validates the given apiKey by sending an arbitrary dummy request,
     * i.e. if invalid, IOEception is thrown.
     *
     * @param apiKey
     */
    public void start( final String apiKey) {
        AppExecutors.getInstance().networkIO().execute(() -> {
            try {
                String response = getResponseFromHttpUrl( buildUrl ( apiKey, "popular", 1));
                AppExecutors.getInstance().diskIO().execute(  () -> appDatabase.movieDao() .insert( new Movie(1, false, false, 0, 0, 0 ,  apiKey)));
            } catch (IOException e) { e.printStackTrace();
                AppExecutors.getInstance().diskIO().execute(  () -> appDatabase.movieDao() .insert( new Movie(1, false, false, 0, 0, 0 , null)));
            }
        });
    }

    public LiveData< String>  getApikeyLive()  { return appDatabase.movieDao() .loadApikey();}

    public  LiveData< Movie>  getMovieLive( int movieID)  { return  appDatabase.movieDao() .loadMovieByMovieIDLive( movieID); }

    public  LiveData< List< Movie>>  getMovieListLive( String orderedby)  {
        if (      orderedby.equals("popular"  ))   return appDatabase.movieDao() .loadPopularMoviesLive();
        else if ( orderedby.equals("top_rated"))   return appDatabase.movieDao() .loadTopRatedMoviesLive();
        else { Log .e(this.getClass().getSimpleName(), "invalid argument ORDERBY: " + orderedby);  return null; }
    }



    /* *********************************************************************************************
     * Integrates all the basics of all movies accessible from the 'popular' and 'top_rated' orderedbys
     *
     * @param apiKey
     */
    public void integrate(final String apiKey) {  AppExecutors.getInstance().networkIO().execute(() -> {
            boolean condition;
            int page;
            for (String orderby : AppUtilities.ORDEREDBY_list) {         page = 0;
                do { try { try {                                         page++;

                    JSONObject jsonObject = new JSONObject( Objects.requireNonNull(
                                                    AppUtilities .getResponseFromHttpUrl(
                                                        buildUrl(apiKey,orderby,page))));

                    condition = page == jsonObject.getInt("page")   &&   page < jsonObject.getInt("total_pages");

                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        insert( new Movie(
                                jsonArray .getJSONObject( i).getInt("id"),
                                false,
                                false,
                                jsonArray .getJSONObject( i).getDouble("popularity"),
                                jsonArray .getJSONObject( i).getDouble("vote_average"),
                                jsonArray .getJSONObject( i).getInt("vote_count"),
                                jsonArray .getJSONObject( i).getString("poster_path")
                        ));
                    }
                    result .postValue( 1);

                } catch (JSONException e) { e.printStackTrace(); result .postValue( 0); return; }
                } catch (IOException e)   { e.printStackTrace(); result .postValue( 0); return; }
                } while (condition);
            }
    });}

    /* *********************************************************************************************
     *
     * @param movie
     */
    private void insert(           Movie movie) { insertExec(movie);}
    private void insertAsync(final Movie movie) { new Thread(                                    () -> appDatabase .movieDao() .insert(movie)) .start(); }
    private void insertExec( final Movie movie) { AppExecutors.getInstance().diskIO() .execute(  () -> appDatabase .movieDao() .insert(movie))         ; }




    /* *********************************************************************************************
     * Generates validated urls as strings for the  themoviedb.org  API
     *
     * @param orderedby    ∈  {"popular", "top_rated"}
     * @param page
     * @return url as a String, not as a URL !!!
     */
    public static String buildUrl( @NonNull String apiKey, @NonNull String orderedby, @NonNull int page) {

        try { return    new URL ( Uri
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
