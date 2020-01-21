package com.roblebob.ud801_popular_movies;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class NetworkUtils {
    // TODO: remove api_key before turning from private to public
    public static final String  API_KEY = "1fb7cc437ac29bc81a0cd83f89156d79";
    public static final String[] ORDER_BY_array = { "popular" , "top_rated"};
    public static final List< String> ORDER_BY_list = new ArrayList< String>( Arrays.asList( ORDER_BY_array));
    public static final String[] SIZE_array = { "w185", "original", "w92", "w154",  "w342", "w500", "w780" };
    public static final List< String> SIZE_list = new ArrayList< String>( Arrays.asList( SIZE_array));

    public static URL buildUrlForMainMovieData(String specification, int page) {
        try { return new URL(Uri
                    .parse("https://api.themoviedb.org")
                    .buildUpon()
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(specification)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("language", "en-US")
                    .appendQueryParameter("page", String.valueOf(((0 < page) ? page : 1)))
                    .build()
                    .toString()
            );
        } catch (MalformedURLException e) { e.printStackTrace(); return null; }
    }
    public static URL buildUrlForDetailMovieData(String specification, int id) {
        try { return new URL(Uri
                .parse("https://api.themoviedb.org")
                .buildUpon()
                .appendPath("3")
                .appendPath("movie")
                .appendPath( String .valueOf( id))
                .appendQueryParameter( "api_key", API_KEY)
                .appendQueryParameter( "language", "en-US")
                .build()
                .toString()
        );
        } catch (MalformedURLException e) { e.printStackTrace(); return null; }
    }
    public static URL buildUrlForMoviePosterImage(String posterPath, String size) {
        try { return new URL(Uri
                .parse("http://image.tmdb.org")
                .buildUpon()
                .appendEncodedPath("t")
                .appendEncodedPath("p")
                .appendEncodedPath( ( SIZE_list .contains( size)) ? size : SIZE_list.get(0))
                .appendEncodedPath( posterPath)
                .build()
                .toString()
        );
        } catch (MalformedURLException e) { e.printStackTrace(); return null; }
    }
    public static URL buildUrlForMovieTrailerYoutubed(String key) {
        try { return new URL(Uri
                .parse("https://www.youtube.com/?v=")
                .buildUpon()
                .appendPath("watch")
                .appendQueryParameter("v", key)
                .build()
                .toString()
        );
        } catch (MalformedURLException e) { e.printStackTrace(); return null; }
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
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

    public static Movie parseJSONObjectIntoMovie(JSONObject jsonObject) {

        try {
            int id                  = jsonObject .getInt("id");
            double popularity       = jsonObject .getDouble("popularity");
            int voteCount           = jsonObject .getInt("vote_count");
            boolean video           = jsonObject .getBoolean("video"); ;
            String posterPath       = jsonObject .getString("poster_path");
            boolean adlult          = jsonObject .getBoolean("adult");
            String backdropPath     = jsonObject .getString("backdrop_path");
            String originalLanguage = jsonObject .getString("original_language");
            String originalTitle    = jsonObject .getString("original_title");
            String title            = jsonObject .getString("title");
            double voteAverage      = jsonObject .getDouble("vote_average");
            String overview         = jsonObject .getString("overview");
            String releaseDate      = jsonObject .getString("release_date");

            String genreIds         = jsonObject .getJSONArray("genre_ids").toString();

            return new Movie(id, popularity, voteCount, video, posterPath, adlult, backdropPath,
                    originalLanguage, originalTitle, genreIds, title, voteAverage, overview, releaseDate);

        } catch (JSONException e) { e.printStackTrace(); return null; }
    }


    public static Movie parseDetailsJSONObjectIntoMovie(JSONObject jsonObject) {

        try {
            int id                  = jsonObject .getInt("id");
            double popularity       = jsonObject .getDouble("popularity");
            int voteCount           = jsonObject .getInt("vote_count");
            boolean video           = jsonObject .getBoolean("video"); ;
            String posterPath       = jsonObject .getString("poster_path");
            boolean adlult          = jsonObject .getBoolean("adult");
            String backdropPath     = jsonObject .getString("backdrop_path");
            String originalLanguage = jsonObject .getString("original_language");
            String originalTitle    = jsonObject .getString("original_title");
            String title            = jsonObject .getString("title");
            double voteAverage      = jsonObject .getDouble("vote_average");
            String overview         = jsonObject .getString("overview");
            String releaseDate      = jsonObject .getString("release_date");

            String genreIds         = jsonObject .getJSONArray("genre_ids").toString();

            return new Movie(id, popularity, voteCount, video, posterPath, adlult, backdropPath,
                    originalLanguage, originalTitle, genreIds, title, voteAverage, overview, releaseDate);

        } catch (JSONException e) { e.printStackTrace(); return null; }
    }


    public static void integratePageOfMovies(AppDatabase appDatabase, String orderBy, int page)  {
        URL url = buildUrlForMainMovieData( orderBy, page);
        if ( url != null)
            AppExecutors .getInstance() .networkIO() .execute( () -> {
                    try {
                        String json = getResponseFromHttpUrl( url);

                        if ( json != null)
                            if ( json .length() != 0)
                                AppExecutors .getInstance() .diskIO() .execute( () -> {

                                        try {
                                            JSONObject o = new JSONObject( json);
                                            JSONArray O_Results = o .getJSONArray("results");
                                            for (int i = 0; i < O_Results.length(); i++) {

                                                Movie movie = NetworkUtils .parseJSONObjectIntoMovie( O_Results .getJSONObject( i));
                                                appDatabase .movieDao() .insertMovie(movie);
                                            }
                                        } catch (JSONException e) { e.printStackTrace(); }
                                    });

                    } catch (IOException e) { e.printStackTrace(); }
            });

    }
}