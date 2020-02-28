package com.roblebob.ud801_popular_movies;


import androidx.annotation.NonNull;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


public class AppUtilities {

    public static final List< String> ORDER = new ArrayList<>( Arrays.asList( "popular", "top_rated"));

    public static String youtubeUrl( final String youtubeKey) { return "https://www.youtube.com/watch?v=" + youtubeKey; }


    /* *********************************************************************************************
     * This method returns the entire result from the HTTP response.
     *
     * @param   urlString The URL to fetch the HTTP response from (as a String and NOT as URL).
     * @return  The contents of the HTTP response.
     * @throws  IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(String urlString) throws IOException {

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



    ////////////////////////////////////////////////////////////////////////////////////////////////


    public int getDetailedItemCountValidated( List<Main> mainList) {
        return  mainList.parallelStream() .filter(movie -> movie.isDetailed())  .mapToInt(movie -> 1)  .sum();
    }

//    public static String invertOrderby( String orderby) {
//        switch (orderby) {
//            case "popular":     return "top_rated";
//            case "top_Rated":   return "popular";
//            default:            return null;
//        }
//    }
//
// switch (orderedby) {
//
//            case "popular":
//                Collections.sort(mainList, ((Comparator<Main>) (Main m1, Main m2) ->
//                        (Double.compare(m1.getPopularVAL(), m2.getPopularVAL()) != 0) ?
//                                Double.compare(m1.getPopularVAL(), m2.getPopularVAL()) :
//                                (Double.compare(m1.getFa(), m2.getFav()) != 0) ?
//                                        Double.compare(m1.getFav(), m2.getFav()) :
//                                        Double.compare(m1.getVoteAVG(), m2.getVoteAVG())
//                ).reversed());
//                break;
//
//            case "top_rated":
//                Collections.sort(mainList, ((Comparator<Main>) (Main m1, Main m2) ->
//                        (Double.compare(m1.getVoteAVG(), m2.getVoteAVG()) != 0) ?
//                                Double.compare(m1.getVoteAVG(), m2.getVoteAVG()) :
//                                (Double.compare(m1.getFav(), m2.getFav()) != 0) ?
//                                        Double.compare(m1.getFav(), m2.getFav()) :
//                                        Double.compare(m1.getPopularVAL(), m2.getPopularVAL())
//                ).reversed());
//                break;
//        }
//    public static List<Main> applyOrderedbyToMovieList(@NonNull String orderedby, @NonNull List<Main> mainList) {
//
//
//        return mainList;
//    }

}