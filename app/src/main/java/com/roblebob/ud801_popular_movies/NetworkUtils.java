package com.roblebob.ud801_popular_movies;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    // TODO: remove api_key before turning from private to public
    public static final String  API_KEY = "1fb7cc437ac29bc81a0cd83f89156d79";
    public static final String[] ORDER_BY_array = { "popular" , "top_rated"};
    public static final List< String> ORDER_BY_list = new ArrayList< String>( Arrays.asList( ORDER_BY_array));
    public static final String[] SIZE_array = { "w185", "original", "w92", "w154",  "w342", "w500", "w780" };
    public static final List< String> SIZE_list = new ArrayList< String>( Arrays.asList( SIZE_array));


    /**
     * This method generates urls for the  themoviedb.org  API
     *
     * @param requestKey    ∈  {"populars, "tops, "movie", "trailers", "reviews"}
     * @param requestValue  corresponds to PAGE  when requestKey ∈  {"populars, "tops}
     *                              ... to ID   when requestKey ∈  {"movie", "trailers", "reviews"}
     * @return url as a String, not as a URL !!!
     */
    private static String buildUrl(String requestKey, int requestValue) {
        Log.e( TAG + "::buildUrl( " + requestKey +  ", " + requestValue + ")" , "... started");
        try {
            Uri.Builder uriBuilder = Uri.parse( "https://api.themoviedb.org/3/movie") .buildUpon();

            switch (requestKey) {
                case "popular":
                case "top_rated":
                    uriBuilder.appendPath(requestKey);
                    break;
                case "movie":
                    uriBuilder.appendPath(String.valueOf(requestValue));
                    break;
                case "videos":
                case "reviews":
                    uriBuilder .appendPath( String.valueOf( requestValue));
                    uriBuilder .appendPath( requestKey);
                    break;
            }

            uriBuilder .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("language", "en-US");

            if ( requestKey.equals("popular") || requestKey.equals("top_rated"))
                uriBuilder .appendQueryParameter("page", String .valueOf( requestValue));

            return new URL( uriBuilder .build() .toString()) .toString();
        } catch ( MalformedURLException e) { e.printStackTrace(); return null; }
    }





    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param urlString The URL to fetch the HTTP response from (as a String and NOT as URL).
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    private static String getResponseFromHttpUrl(String urlString) throws IOException {
        Log.e( TAG, " === URL-REQUEST ==> " + urlString );
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }
        finally { urlConnection.disconnect(); }
    }






    private static Movie parseJsonIntoMovie(JSONObject jsonObject, JSONArray trailersJsonArray, JSONArray reviewsJsonArray) {

        try {
            // extract basics
            int id                  = jsonObject .getInt("id");
            boolean isOneOfMyFavorites = false;  // by default
            double popularity       = jsonObject .getDouble("popularity");
            double voteAverage      = jsonObject .getDouble("vote_average");
            int voteCount           = jsonObject .getInt("vote_count");
            String title            = jsonObject .getString("title");
            String posterUrl        = "http://image.tmdb.org/t/p/w185/" + jsonObject .getString("poster_path");

            // extract details
            if (trailersJsonArray != null  && reviewsJsonArray != null) {

                String originalTitle    = jsonObject .getString("original_title");
                String originalLanguage = jsonObject .getString("original_language");
                String releaseDate      = jsonObject .getString("release_date");
                String runtime          = jsonObject .getString("runtime");
                String tagline          = jsonObject .getString("tagline");
                String overview         = jsonObject .getString("overview");
                String budget           = jsonObject .getString("budget");
                String revenue          = jsonObject .getString("revenue");

                List< String> genreList = new ArrayList< String>();
                JSONArray jsonArray = jsonObject .getJSONArray("genres");
                for ( int index = 0; index < jsonArray.length(); index++)
                    genreList.add( jsonArray .getJSONObject( index) .getString("name"));

                List< String> trailerTitleList = new ArrayList< String>();
                List< String> trailerUrlList   = new ArrayList< String>();
                for (int index = 0; index < trailersJsonArray.length(); index++)
                    if (trailersJsonArray.getJSONObject(index).getString("type").equals("Trailer")) {

                        trailerTitleList .add( trailersJsonArray .getJSONObject( index) .getString("name"));
                        trailerUrlList .add( "https://www.youtube.com/watch?v=" + trailersJsonArray .getJSONObject( index) .getString("key"));
                    }

                List< String> reviewAuthorList  = new ArrayList< String>();
                List< String> reviewUrlList     = new ArrayList< String>();
                for (int index = 0; index < reviewsJsonArray.length(); index++) {

                    reviewAuthorList   .add( reviewsJsonArray .getJSONObject( index) .getString("author"));
                    reviewUrlList      .add( reviewsJsonArray .getJSONObject( index) .getString("url"));
                }

                Log.e(NetworkUtils.class.getSimpleName(), " " +  id + isOneOfMyFavorites + popularity + voteAverage + voteCount + posterUrl + title + originalTitle + originalLanguage + releaseDate + runtime + tagline + overview + genreList.toString() );

                return new Movie(   id,     isOneOfMyFavorites,     popularity,     voteAverage,    voteCount,  title,      posterUrl,   originalTitle,   originalLanguage,       releaseDate,      runtime,      tagline,     overview,      genreList.toString(),     budget,     revenue,      trailerTitleList.toString(),    trailerUrlList.toString(),  reviewAuthorList.toString(),    reviewUrlList.toString());
            } else return new Movie( id,    isOneOfMyFavorites,     popularity,     voteAverage,    voteCount,  title,      posterUrl,  null, null,    null,  null, null, null, null,             null, null, null,                null,             null,              null);
        } catch (JSONException e) { e.printStackTrace(); }
        return null;
    }






    public static void integratePageOfMovies( final AppDatabase appDatabase, final String orderBy, final int page)  {

        AppExecutors .getInstance() .networkIO() .execute( () -> {
            Log.e( TAG + "::integratePageOfMovies( " + page + ")", "\t B E G I N >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " );
            try {
                final String JSON = getResponseFromHttpUrl( (buildUrl( orderBy, page)));

                if (JSON != null)
                    try {
                        JSONObject o = new JSONObject( JSON);
                        JSONArray O_Results = o .getJSONArray("results");
                        for (int i = 0; i < O_Results.length(); i++) {

                            Movie MOVIE = NetworkUtils .parseJsonIntoMovie( O_Results .getJSONObject( i), null, null);
                            Log.e(TAG + "::integratePageOfMovies()", "------>" + MOVIE.toString());
                            AppExecutors .getInstance() .diskIO() .execute(() ->  appDatabase .movieDao() .insertMovie( MOVIE) );
                        }
                    } catch (JSONException e) { Log.e( TAG + "::integratePageOfMovies()", "<<<<<<<<<<<<<<<<<<<<\nJSONException " ); e.printStackTrace(); }
            } catch (IOException e) { Log.e( TAG + "::integratePageOfMovies()", "<<<<<<<<<<<<<<<<<<<<\nIOException " ); e.printStackTrace(); }
            Log.e( TAG + "::integratePageOfMovies()", "\tE N D <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< " );
        });
    }


    public static void integrateDetails( final AppDatabase appDatabase, final int id)  {
        AppExecutors .getInstance() .networkIO() .execute( () -> {
            Log.e( TAG + "::integrateDetails( " + id + " )", " [ B E G I N >>>>>>>>>>>>>>>>>>>> " );
            Movie movie = null;
            try {
                String json         = getResponseFromHttpUrl( ( buildUrl("movie",   id)));
                String jsonTrailers = getResponseFromHttpUrl( ( buildUrl("videos",  id)));
                String jsonReviews  = getResponseFromHttpUrl( ( buildUrl("reviews", id)));
                try {
                    movie = NetworkUtils .parseJsonIntoMovie(
                            new JSONObject( json),
                            (new JSONObject( jsonTrailers)) .getJSONArray("results"),
                            (new JSONObject( jsonReviews)) .getJSONArray("results")
                    );
                } catch (JSONException e) { e.printStackTrace();  }
            } catch (IOException e) { e.printStackTrace(); }

            final Movie MOVIE = movie;
            if (MOVIE != null) AppExecutors .getInstance() .diskIO() .execute( () -> appDatabase .movieDao() .updateMovie( MOVIE));
            Log.e( TAG + "::integrateDetails( " + id + " )", " [ E N D <<<<<<<<<<<< " + MOVIE.toString() );
        });
    }
}