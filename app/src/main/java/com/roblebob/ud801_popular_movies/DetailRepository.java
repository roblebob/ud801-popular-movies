package com.roblebob.ud801_popular_movies;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static com.roblebob.ud801_popular_movies.AppUtilities.getResponseFromHttpUrl;

/***************************************************************************************************
 *
 */
public class DetailRepository {

    private AppDatabase appDatabase;

    public DetailRepository(@NonNull AppDatabase appDatabase) {

        this.appDatabase = appDatabase;
    }


    /* *********************************************************************************************
     *
     */
    public LiveData< List<Detail>> getListLive(int movieID)  { return this.appDatabase .detailDao() .loadList( movieID); }

    public LiveData< Integer> countMovies() { return this.appDatabase .detailDao() .countMovies(); }



    /* **********************************************************************************************
     *
     * @param movieID
     */
    public  void integrate(final String apiKey, final Integer movieID)  {


        Log.e(this.getClass().getSimpleName() + "::integrate() ", "----API-KEY----> " + apiKey );


        if (apiKey != null)
            AppExecutors .getInstance() .networkIO() .execute( () -> {
                try { try {
                    JSONObject  jsonObject = new JSONObject( Objects.requireNonNull(
                            getResponseFromHttpUrl( buildUrl(   apiKey,  String.valueOf(movieID)))));


                    for (String order : Detail.ORDER) {
                        switch (order) {

                            case "title":
                            case "release_date":
                            case "runtime":
                            case "tagline":
                            case "overview":
                                insert(  new Detail( movieID, order, jsonObject.getString( order), null));

                                break;


                            case "original_title":
                            case "original_language":
                                if ( ! jsonObject.getString("title") .equals( jsonObject.getString("original_title")))
                                    insert(  new Detail( movieID, order, jsonObject.getString( order), null));
                                break;


//                            case "genres":
//                                JSONArray jsonArray = new JSONArray( jsonObject.getJSONArray( order));
//
//                                String content = jsonArray .getJSONObject(0) .getString("name");
//                                for (int i = 1; i < jsonArray.length(); i++) {
//                                    content += "\n" + jsonArray .getJSONObject( i) .getString("name");
//                                }
//
//                                insert(  new Detail( movieID, order, content, null));
//                                break;


                            case "budget":
                            case "revenue":
                                if (jsonObject.getInt(order) > 0)
                                    insert(  new Detail( movieID, order, jsonObject.getString( order), null));
                                break;


                            case "homepage":
                            case "imdb_key":
                                insert(  new Detail( movieID, order, order, jsonObject.getString( order)));
                                break;

                        }
                    }

                    ////////////////////////////////////////////////////////////////////////////
//                        for (String order : Detail.ORDER.subList( 12 /*videos*/, 13 /*reviews*/)) {
//
//                            JSONArray  jsonArray  = (new JSONObject ( Objects.requireNonNull (
//                                                            getResponseFromHttpUrl ( buildUrl (
//                                                                    apiKey, movieID, order))))
//                                                    ).getJSONArray("results");
//
//                            for (int i = 0; i < jsonArray.length(); i++) {
//
//                                JSONObject jsonObj = jsonArray .getJSONObject (i);
//
//                                switch (order) {
//
//                                    case "videos":
//                                        if (jsonObj.getString("type").equals("Trailer"))
//                                            insert( new Detail(  movieID,  order,  /*content*/ jsonObj.getString("name"),  /*link*/ jsonObj.getString("key")));
//                                        break;
//
//                                    case "reviews":
//                                        insert( new Detail(  movieID,  order,  /*content*/ jsonObj.getString("author"),
//                                                        /*link*/ ((jsonObj.getString("url"))
//                                                                     .replace("https://www.themoviedb.org/review/", "")
//                                                                        .equals( jsonObj.getString("id")))
//                                                            ?   jsonObj.getString("id")
//                                                            :   jsonObj.getString("url")
//                                        ));
//                                        break;
//                                }
//                            }
//                        }

                } catch (JSONException e)        { e.printStackTrace(); Log .e(this.getClass().getSimpleName(), "E R R O R  in  integrate("+ movieID + "):\tJSONException"); }
                } catch (IOException e)          { e.printStackTrace(); Log .e(this.getClass().getSimpleName(), "E R R O R  in  integrate("+ movieID + "):\tIOException"); }
            });
    }




    /* *********************************************************************************************
     *
     * @param detail
     */
    public void insert( Detail detail) { AppExecutors.getInstance().diskIO().execute( () -> this.appDatabase .detailDao() .insert(detail)); }




    /* *********************************************************************************************
     * Generates validated urls as strings for the  themoviedb.org  API
     *
     * @param apiKey
     * @param movieID
     * @param xtraKEY ∈  { null, "videos", "reviews"}
     * @return url as a String, not as a URL !!!
     */
    public String buildUrl( @NonNull String apiKey, String key) {
        try { return  new URL( Uri
                .parse( "https://api.themoviedb.org/3/movie")
                .buildUpon()
                .appendPath( key)
                .appendQueryParameter("api_key", apiKey)
                .appendQueryParameter("language", "en-US")
                .build()
                .toString()
        ).toString();
        } catch ( MalformedURLException e) {
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName(), "E R R O R  in  buildUrl():\tMalformedURLException");
            return null;
        }
    }
}
