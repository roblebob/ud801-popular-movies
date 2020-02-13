package com.roblebob.ud801_popular_movies;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

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
public class MovieRepository  {
    public static final List< String> ORDEREDBY_list =     new ArrayList< String>( Arrays.asList( "popular", "top_rated"));
    private AppDatabase appDatabase;

    private LiveData< Integer> orderedbyTabPositionLive;
    public LiveData< String> orderedbyLive;




    public MovieRepository(@NonNull AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        orderedbyTabPositionLive = this.appDatabase .movieDao().loadOrderedbyTabPositionLive();
        orderedbyLive = Transformations.map(orderedbyTabPositionLive, pos ->  ORDEREDBY_list.get(pos));

    }


    /* *********************************************************************************************
     * Validates the given apiKey by sending an arbitrary dummy request,
     * i.e. if invalid, IOEception is thrown.
     *
     * @param apiKey
     */
    public void start( String apiKEY) {
        //Log.e(this.getClass().getSimpleName(), "----->  MovieRepository started with apikey: " + apiKEY);
        AppExecutors.getInstance().networkIO().execute(() -> {
            try {
                String response = getResponseFromHttpUrl( buildUrl ( apiKEY, "popular", 1));

                AppExecutors.getInstance().diskIO().execute(  () -> this.appDatabase .movieDao() .update( new Movie(1, false, false, 1, 0, 0, apiKEY)));
                //Log.e (this.getClass().getSimpleName(), "R e p o s i t y -->  apiKey: " + apiKEY +  " accepted");
                integrate(apiKEY);
            } catch (IOException e) { e.printStackTrace();
                AppExecutors.getInstance().diskIO().execute(  () -> this.appDatabase .movieDao() .insert( new Movie(1, false, false, 1, 0, 0, null)));
                //Log.e (this.getClass().getSimpleName(), "R e p o s i t y -->  apiKey: " + apiKEY +  " rejected");
            }
        });
    }


    /* *********************************************************************************************
     *
     *
     */
    public LiveData< Integer> countMovies() { return this.appDatabase.movieDao().countMovies(); }

    public  LiveData< Movie>  getMovieLive( int movieID)  { return  this.appDatabase .movieDao() .loadMovieByMovieIDLive( movieID); }


    public  LiveData< List< Movie>>  getMovieListLive( String orderedby)  {
        if (      orderedby.equals("popular"  ))   return this.appDatabase .movieDao() .loadPopularMoviesLive();
        else if ( orderedby.equals("top_rated"))   return this.appDatabase .movieDao() .loadTopRatedMoviesLive();
        else { Log .e(this.getClass().getSimpleName(), "invalid argument ORDERBY: " + orderedby);  return null; }
    }

    public LiveData< List< Movie>>  getPopularMovieListLive()  { return this.appDatabase .movieDao() .loadPopularMoviesLive(); }
    public LiveData< List< Movie>>  getTopRatedMovieListLive()  { return this.appDatabase .movieDao() .loadTopRatedMoviesLive(); }



    /* *********************************************************************************************
     * Integrates all the basics of all movies accessible from the 'popular' and 'top_rated' orderedbys
     *
     * @param apiKey
     */
    public void integrate(String apiKey) {  AppExecutors.getInstance().networkIO().execute(() -> {
            boolean condition = true;
            int page;
            for (String orderby : ORDEREDBY_list) {         page = 0;
                do { try { try {                            page++;

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
                } catch (JSONException e) { e.printStackTrace();  Log .e(this.getClass().getSimpleName(), "E R R O R  in  integrate():\tJSONException"); }
                } catch (IOException e)   { e.printStackTrace();  Log .e(this.getClass().getSimpleName(), "E R R O R  in  integrate():\tIOException"); }
                } while (condition);
            }
    });}

    /* *********************************************************************************************
     *
     * @param movie
     */
    private void insert(           Movie movie) { insertExec(movie);}
    private void insertAsync(final Movie movie) { new Thread(                                    () -> this.appDatabase  .movieDao() .insert(movie)) .start(); }
    private void insertExec( final Movie movie) { AppExecutors.getInstance().diskIO() .execute(  () -> this.appDatabase  .movieDao() .insert(movie))         ; }




    /* *********************************************************************************************
     * Generates validated urls as strings for the  themoviedb.org  API
     *
     * @param orderedby    ∈  {"popular", "top_rated"}
     * @param page
     * @return url as a String, not as a URL !!!
     */
    public static String buildUrl( @NonNull String apiKey, @NonNull String orderedby, @NonNull int page) {

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


    public void insertOrderedbyTabPosition(int position) {
        AppExecutors.getInstance().diskIO().execute(
                () -> appDatabase.movieDao().updateOrderedbyTabPosition(position));
    }
}
