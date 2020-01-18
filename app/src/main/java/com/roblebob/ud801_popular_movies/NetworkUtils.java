package com.roblebob.ud801_popular_movies;

import android.net.Uri;
import android.util.Log;

import androidx.room.PrimaryKey;

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
    private final static String TAG = NetworkUtils.class.getSimpleName();

    // TODO: remove api_key before turning from private to public
    private static final String  API_KEY = "1fb7cc437ac29bc81a0cd83f89156d79";

    public static URL buildUrl(String orderPath, int page) {
        URL url = null;
        final String BASE_URL = "https://api.themoviedb.org/3/movie";
        final String[] ORDER_PATH_array = { "popular", "top_Rated"};
        final List< String> ORDER_PATH_list = new ArrayList< String>( Arrays.asList( ORDER_PATH_array));
        final String ORDER_PATH_default = "popular";
        final String LANGUAGE_default = "en-US";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath( ( ORDER_PATH_list .contains( orderPath)) ? orderPath : ORDER_PATH_default)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", LANGUAGE_default)
                .appendQueryParameter("page", String.valueOf(((0 < page) ? page : 1)))
                .build();

        try { url = new URL(builtUri.toString()); }
        catch (MalformedURLException e) { e.printStackTrace(); }

    return url;
    }

    public static URL buildUrlImage(String posterPath, String size) {
        URL url = null;
        final String BASE_URL = "http://image.tmdb.org/t/p/";;
        final String[] SIZE_array = { "original/", "w92/", "w154/", "w185/", "w342/", "w500/", "w780/" };
        final ArrayList< String> SIZE_list = new ArrayList<>( Arrays.asList( SIZE_array));
        final String SIZE_default = "w185/";

        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath( (SIZE_list .contains( size)) ? size : SIZE_default )
                .appendPath( posterPath)
                .build();

        try { url = new URL(builtUri.toString()); }
        catch (MalformedURLException e) { e.printStackTrace(); }

        return url;
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
        } finally {
            urlConnection.disconnect();
        }
    }
}