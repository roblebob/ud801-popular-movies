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
        // Log.d( TAG + "::buildUrl( " + requestKey +  ", " + requestValue + ")" , "... started");
        try {
            Uri.Builder uriBuilder = Uri.parse( "https://api.themoviedb.org/3/movie") .buildUpon();
            switch (requestKey) {
                case "popular":     /* request for a list of the most popular movies  */
                case "top_rated":   /* request for a list of the best rated movies  */
                    uriBuilder.appendPath(requestKey);
                    break;
                case "movie":       /* request for detailed info of a single specific movie  */
                    uriBuilder.appendPath(String.valueOf(requestValue));
                    break;
                case "videos":      /* SUBCLASS(movie) : request for a list of YouTube trailers of a single specific movie */
                case "reviews":     /* SUBCLASS(movie) : request for a list of reviews dedicated to a single specific movie */
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




    private static Movie parseJsonIntoMovieBasics( JSONObject jsonObject) {
        Movie mBasic = null;
        try {
            int movieID         = jsonObject .getInt("id");
            double popularVAL   = jsonObject .getDouble("popularity");
            double voteAVG      = jsonObject .getDouble("vote_average");
            int voteCNT         = jsonObject .getInt("vote_count");
            String posterID     = jsonObject .getString("poster_path");
            mBasic = new Movie( movieID, false, popularVAL, voteAVG, voteCNT, posterID, null, null, null, null, null, null, null, null, null, null, null, null);
        } catch (JSONException e) { e.printStackTrace(); }
        return mBasic;
    }

    private static Movie parseJsonIntoMovieDetails( JSONObject jsonObject) {
        Movie mDetails = null;
        try {
            int movieID         = jsonObject .getInt("id");
            double popularVAL   = jsonObject .getDouble("popularity");
            double voteAVG      = jsonObject .getDouble("vote_average");
            int voteCNT         = jsonObject .getInt("vote_count");
            String posterID     = jsonObject .getString("poster_path");
            String budgetVAL    = jsonObject .getString("budget");
            String title        = jsonObject .getString("title");
            String titleORIG    = jsonObject .getString("original_title");
            String langORIG     = jsonObject .getString("original_language");
            String releasePIT   = jsonObject .getString("release_date");
            String runtimeVAL   = jsonObject .getString("runtime");
            String tagline      = jsonObject .getString("tagline");
            String overview     = jsonObject .getString("overview");
            String revenueVAL   = jsonObject .getString("revenue");
            String homepageURL  = jsonObject .getString("homepage");
            String imdbID       = jsonObject  .getString("imdb_id");

            List< String> genreList = new ArrayList< String>();
            JSONArray jsonArray = jsonObject .getJSONArray("genres");
            for ( int i = 0; i < jsonArray.length(); i++)   genreList.add( jsonArray .getJSONObject( i) .getString("name"));

            mDetails = new Movie( movieID, false, popularVAL, voteAVG, voteCNT, posterID, title, titleORIG, langORIG, releasePIT, runtimeVAL, tagline, overview, genreList.toString(), budgetVAL, revenueVAL, homepageURL, imdbID);
        } catch (JSONException e) { e.printStackTrace(); Log .e(TAG + "\t::parseJsonIntoMovieDetails()\t", "ERROR");  }
        return mDetails;
    }

    private static MovieExtra parseJsonIntoMovieExtra( JSONObject jsonObject, int movieID) {
        try{
            String type =       jsonObject.getString("type").toLowerCase();
            type =              (type.equals("trailer") || type.equals("review"))  ? type  : null ;

            String name =       (type.equals("trailer")) ?  jsonObject.getString("name")  :
                                (type.equals("review"))  ? jsonObject.getString("author") : null ;

            String url =        (type.equals("trailer")) ? "https://www.youtube.com/watch?v=" + jsonObject.getString("key")  :
                                (type.equals("review"))  ? jsonObject.getString("url")  : null ;

            String additions =  (type.equals("trailer")) ? null  :
                                (type.equals("review"))  ? jsonObject.getString("content")  : null ;

            MovieExtra movieExtra = new MovieExtra(movieID, type, name, url, additions);
            Log .e(TAG + "\t::parseJsonIntoMovieExtra()\t", movieExtra.toString());
            return movieExtra;
        } catch (JSONException e) { e.printStackTrace(); return null; }
    }




    public static void integrateAllBasics( final AppDatabase appDatabase) {
        AppExecutors.getInstance().networkIO().execute(() -> {
            for (String orderBy : ORDER_BY_array) {
                boolean condition = true;
                int page = 0;
                do { try { try {
                            String response = getResponseFromHttpUrl(buildUrl(orderBy, ++page));
                            JSONObject jsonObject = new JSONObject(response);

                            condition = page == jsonObject.getInt("page") &&
                                    page < jsonObject.getInt("total_pages");

                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Movie movie = parseJsonIntoMovieBasics(jsonArray.getJSONObject(i));
                                if (movie != null) {
                                    AppExecutors.getInstance().diskIO().execute(
                                            () -> appDatabase.movieDao().insertMovie(movie));
                                }
                            }
                        } catch (JSONException e) { e.printStackTrace(); return; }
                    } catch (IOException e) { e.printStackTrace(); return; }
                } while (condition);
            }
        });
    }



    public static void integrateDetails( AppDatabase appDatabase, int movieID)  {
        AppExecutors .getInstance() .networkIO() .execute( () -> {
            try {
                String response = getResponseFromHttpUrl((buildUrl("movie", movieID)));
                if (response != null)
                    try {
                        Movie movie = parseJsonIntoMovieDetails(new JSONObject(response));
                        if (movie != null)
                            AppExecutors.getInstance().diskIO().execute(
                                    () -> appDatabase.movieDao().updateMovie(movie));

                    } catch (JSONException e) { e.printStackTrace(); }
            } catch (IOException e) { e.printStackTrace(); }
        });
    }



    public static void integrateExtras( AppDatabase appDatabase, int movieID) {
        AppExecutors.getInstance().networkIO().execute(() -> {
            for (String requestKey : new String[]{"videos", "reviews"}) {
                try {
                    String response = getResponseFromHttpUrl((buildUrl(requestKey, movieID)));
                    if (response != null)
                        try {
                            MovieExtra movieExtra = parseJsonIntoMovieExtra(new JSONObject(response), movieID);
                            if (movieExtra != null) {
                                Log.d(TAG + "::integrateExtras()", "------>" + movieExtra.toString());
                                AppExecutors.getInstance().diskIO().execute(() -> appDatabase.movieExtraDao().insertExtra(movieExtra));
                            }
                        } catch (JSONException e) { e.printStackTrace(); }
                } catch (IOException e) { e.printStackTrace(); }
            }
        });
    }
}
