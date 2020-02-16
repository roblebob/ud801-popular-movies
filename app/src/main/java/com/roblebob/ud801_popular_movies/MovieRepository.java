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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.roblebob.ud801_popular_movies.AppUtilities.getResponseFromHttpUrl;

/* **************************************************************************************************
 *
 */
public class MovieRepository extends AppRepository<Movie> {


    public List< String> ORDER() { return new ArrayList< String>( Arrays.asList( "popular", "top_rated")); }
    private AppDatabase appDatabase;
    private LiveData< Integer> orderedbyTabPositionLive;
    public  LiveData< String> orderedbyLive;


    public MovieRepository( AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        AppExecutors.getInstance().diskIO().execute(  () -> this.appDatabase .movieDao() .insert(
                new Movie(1, false, false, 0, 0, 0, null)));

    }


    /* *********************************************************************************************
     * Validates the given apiKey by sending an arbitrary dummy request,
     * i.e. if invalid, IOEception is thrown.
     *
     * @param apiKey
     */
    public void start( String apiKey) {

        orderedbyTabPositionLive = this.appDatabase .movieDao() .loadOrderedbyTabPositionLive();

        //Log.e(this.getClass().getSimpleName(), "----->  MovieRepository started with apikey: " + apiKey);
        AppExecutors.getInstance().networkIO().execute(() -> {
            try {
                String response = getResponseFromHttpUrl( buildUrl ( apiKey, "popular", 1));
                AppExecutors .getInstance() .diskIO() .execute( () -> appDatabase .movieDao() .updateApiKey( apiKey));
                Log.e (this.getClass().getSimpleName(), "R e p o s i t y -->  apiKey: " + apiKey +  " accepted");
                integrate(apiKey);
            } catch (IOException e) { e.printStackTrace();
                AppExecutors .getInstance() .diskIO() .execute( () -> appDatabase .movieDao() .updateApiKey( null));
                Log.e (this.getClass().getSimpleName(), "R e p o s i t y -->  apiKey: " + apiKey +  " rejected");
            }
        });
    }


    /* *********************************************************************************************
     *
     *
     */


    public LiveData< List< Movie>> getListLive(String s)  { return this.appDatabase .movieDao() .loadMovieListLive(); }

    public LiveData<  Integer>  getOrderedbyTabPositionLive()  { return this.appDatabase .movieDao() .loadOrderedbyTabPositionLive(); }

    public  LiveData< List< Movie>> getListLive(int orderedbyTabPosition)  {
        Log .e(this.getClass().getSimpleName(), " ---------->> " + orderedbyTabPosition);
        if ( orderedbyTabPosition == 1)       return this.appDatabase .movieDao() .loadTopRatedMovieListLive();
        else /* orderedbyTabPosition == 0 */  return this.appDatabase .movieDao() .loadPopularMovieListLive();
    }
    public LiveData< List< Movie>>  getPopularMovieListLive()  { return this.appDatabase .movieDao() .loadPopularMovieListLive(); }
    public LiveData< List< Movie>>  getTopRatedMovieListLive()  { return this.appDatabase .movieDao() .loadTopRatedMovieListLive(); }







    public void insertOrderedbyTabPosition(int position) {
        AppExecutors .getInstance() .diskIO() .execute(
                () -> appDatabase .movieDao() .updateOrderedbyTabPosition( position)); }




    public LiveData< Integer> countMovies() { return this.appDatabase.movieDao().countMovies(); }
    public  LiveData< Movie>  getMovieLive( int movieID)  { return  this.appDatabase .movieDao() .loadMovieLive( movieID); }



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
                do { try { try {                            page++;

                    JSONObject jsonObject = new JSONObject( Objects.requireNonNull(
                                                    AppUtilities .getResponseFromHttpUrl(
                                                        buildUrl(apiKey,order,page))));

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
                } catch (JSONException e) { e.printStackTrace();  Log .e(this.getClass().getSimpleName(), "E R R O R  in  integrate():\tJSONException"); }
                } catch (IOException e)   { e.printStackTrace();  Log .e(this.getClass().getSimpleName(), "E R R O R  in  integrate():\tIOException"); }
                } while (condition);
            }
    });}

    /* *********************************************************************************************
     *
     * @param movie
     */
    //private void insert(           Movie movie) { insertExec(movie);}
    //private void insertAsync(final Movie movie) { new Thread(                                    () -> this.appDatabase  .movieDao() .insert(movie)) .start(); }
    @WorkerThread
    public void insert( Movie movie) { AppExecutors.getInstance().diskIO() .execute(  () -> this.appDatabase  .movieDao() .insert(movie))         ; }




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
