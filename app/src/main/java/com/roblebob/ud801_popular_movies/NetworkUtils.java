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
        Log.d( TAG + "::buildUrl( " + requestKey +  ", " + requestValue + ")" , "... started");
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
        Log.d( TAG, " === URL-REQUEST ==> " + urlString );
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


    private static Movie parseJsonIntoMovie( JSONObject jsonObject) {

        Movie mBasic;

        try {
            int movieID         = jsonObject .getInt("id");
            double popularVAL   = jsonObject .getDouble("popularity");
            double voteAVG      = jsonObject .getDouble("vote_average");
            int voteCNT         = jsonObject .getInt("vote_count");
            String posterID     = jsonObject .getString("poster_path");
            mBasic = new Movie(movieID, false, popularVAL, voteAVG, voteCNT, posterID, null, null, null, null, null, null, null, null, null, null, null, null);
        } catch (JSONException e) { e.printStackTrace(); return null; }


        Movie mDetails = new Movie( mBasic);

        try {
            mDetails .setTitle(     jsonObject  .getString("title"));
            mDetails .setTitleORIG( jsonObject  .getString("original_title"));
            mDetails .setLangORIG(  jsonObject  .getString("original_language"));
            mDetails .setReleasePIT( jsonObject .getString("release_date"));
            mDetails .setRuntimeVAL( jsonObject .getString("runtime"));
            mDetails .setTagline(   jsonObject  .getString("tagline"));
            mDetails .setOverview(  jsonObject  .getString("overview"));
            mDetails .setBudgetVAL( jsonObject  .getString("budget"));
            mDetails .setRevenueVAL( jsonObject .getString("revenue"));
            mDetails .setHomepageURL( jsonObject.getString("homepage"));
            mDetails .setImdbID(    jsonObject  .getString("imdb_key"));

            List< String> genreList = new ArrayList< String>();
            JSONArray jsonArray = jsonObject .getJSONArray("genres");
            for ( int i = 0; i < jsonArray.length(); i++)
                genreList.add( jsonArray .getJSONObject( i) .getString("name"));
            mDetails .setGenres( genreList.toString());

        } catch (JSONException e) { e.printStackTrace(); return mBasic; }


        return mDetails;
}


    private static MovieExtra parseJsonIntoMovieExtra( int movieID, JSONObject jsonObject) {
        try{
            String type =       jsonObject.getString("type").toLowerCase();
            type =              (type.equals("trailer") || type.equals("review"))  ? type  : null ;

            String name =       (type.equals("trailer")) ?  jsonObject.getString("name")  :
                                (type.equals("review"))  ? jsonObject.getString("author") : null ;

            String url =        (type.equals("trailer")) ? "https://www.youtube.com/watch?v=" + jsonObject.getString("key")  :
                                (type.equals("review"))  ? jsonObject.getString("url")  : null ;

            String additions =  (type.equals("trailer")) ? null  :
                                (type.equals("review"))  ? jsonObject.getString("content")  : null ;

            return new MovieExtra(movieID, type, name, url, additions);

        } catch (JSONException e) { e.printStackTrace(); return null; }
    }





    public static void integrateAllBasics( final AppDatabase appDatabase) {


        for (String orderBy : ORDER_BY_array) {

            AppExecutors.getInstance().networkIO().execute(() -> {

                boolean condition = true;
                int page = 0;

                do {
                    try {
                        try {

                            String response = getResponseFromHttpUrl(buildUrl(orderBy, ++page));
                            JSONObject jsonObject = new JSONObject(response);

                            condition = page == jsonObject.getInt("page") &&
                                        page < jsonObject.getInt("total_pages");

                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                Movie movie = parseJsonIntoMovie(jsonArray.getJSONObject(i));
                                if (movie != null) {
                                    final Movie MOVIE = movie;
                                    Log.e(TAG + "::integrateAllBasics()", MOVIE.toString());
                                    AppExecutors.getInstance().diskIO().execute(() -> appDatabase.movieDao().insertMovie(MOVIE));
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG + "::integrateAllBasics()", "!!! JSONException !!!");
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG + "::integrateAllBasics()  @(" + orderBy + ", " + page + ")", "!!! IOException !!!");
                        return;
                    }
                } while (condition);
            });
        }

    }






//
//
//
//        public static void integrateDetails( AppDatabase appDatabase, int movieID)  {
//
//        AppExecutors .getInstance() .networkIO() .execute( () -> {
//            Log.d( TAG + "::integratePageOfMovies( " + page + ")", "\t B E G I N >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " );
//            try {
//                final String JSON = getResponseFromHttpUrl( (buildUrl( orderBy, page)));
//
//                if (JSON != null) try {
//
//                    JSONArray results = new JSONObject( JSON) .getJSONArray("results");
//                    for (int i = 0; i < results.length(); i++) {
//
//                        final int MID = results .getJSONObject( i). getInt("id");
//
//
//                        AppExecutors .getInstance() .networkIO() .execute( () -> {
//                            try{
//                                final String JSONdetails = getResponseFromHttpUrl( buildUrl("movie",   MID));
//                                try {
//                                    final Movie MOVIE = parseJsonIntoMovie( new JSONObject( JSONdetails));
//                                    if (MOVIE != null) {
//                                        Log.d(TAG + "::integratePageOfMovies()", "------>" + MOVIE.toString());
//                                        AppExecutors.getInstance().diskIO().execute(() -> appDatabase.movieDao().insertMovie(MOVIE));
//                                    }
//                                } catch (JSONException e) { Log.e( TAG + "::integratePageOfMovies()", "!!! JSONException !!!" ); e.printStackTrace(); }
//                            } catch (IOException e) { Log.e( TAG + "::integratePageOfMovies()", "!!! IOException !!!" ); e.printStackTrace(); }
//                        });
//
//
//
//
//                        AppExecutors .getInstance() .networkIO() .execute( () -> {
//                            try{
//                                String JSONtrailers = getResponseFromHttpUrl( buildUrl("videos",   MID));
//                                try {
//                                    JSONArray jsonArray = ( new JSONObject( JSONtrailers)) .getJSONArray("results");
//                                    for (int index = 0; index < jsonArray.length(); index++ )
//                                    {
//                                        JSONObject jO = (JSONObject) jsonArray.get( index);
//                                        final MovieExtra MOVIE_EXTRA = parseJsonIntoMovieExtra( MID, jO);
//                                        if (MOVIE_EXTRA != null) {
//                                            Log.d(TAG + "::integratePageOfMovies()", "------>" + MOVIE_EXTRA.toString());
//                                            AppExecutors.getInstance().diskIO().execute(() -> appDatabase.movieExtraDao() .insertMovie(MOVIE_EXTRA));
//                                        }
//                                    }
//
//                                } catch (JSONException e) { Log.e( TAG + "::integratePageOfMovies()", "!!! JSONException !!!" ); e.printStackTrace(); }
//                            } catch (IOException e) { Log.e( TAG + "::integratePageOfMovies()", "!!! IOException !!!" ); e.printStackTrace(); }
//                        });
//
//                    }
//                } catch (JSONException e) { Log.e( TAG + "::integratePageOfMovies()", "!!! JSONException !!!" ); e.printStackTrace(); }
//            } catch (IOException e) { Log.e( TAG + "::integratePageOfMovies()", "!!! IOException !!!" ); e.printStackTrace(); }
//            Log.d( TAG + "::integratePageOfMovies()", "\tE N D <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< " );
//        });
//    }
//
//


}