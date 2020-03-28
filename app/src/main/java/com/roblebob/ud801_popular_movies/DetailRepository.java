package com.roblebob.ud801_popular_movies;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import static com.roblebob.ud801_popular_movies.AppUtilities.getResponseFromHttpUrl;

public class DetailRepository {
    private AppDatabase appDatabase;
    public DetailRepository( @NonNull AppDatabase appDatabase) { this.appDatabase = appDatabase; }
    public LiveData< List<Detail>> getListLive( int movieID)  { return this.appDatabase .detailDao() .loadList( movieID); }


    public  void integrate( final String apiKey, final Integer movieID)  {
        Log.e(this.getClass().getSimpleName() + "::integrate(\t", "\t" + "apiKey=" + apiKey + "\t,\t" + "movieID=" + movieID + "\t)" );

        if (apiKey != null)
            AppExecutors .getInstance() .networkIO() .execute( () -> {
                try { try {
                    JSONObject  jsonObject = new JSONObject( Objects.requireNonNull(
                            getResponseFromHttpUrl( buildUrl(   apiKey,  String.valueOf(movieID)))));

                    for (String context : Detail.CONTEXTs) {
                        switch (context) {
                            case "title":
                            case "release_date":
                            case "runtime":
                            case "tagline":
                            case "overview":
                                insert(  new Detail( movieID, context, jsonObject.getString( context), ""));
                                break;

                            case "original_title":
                            case "original_language":
                                if ( ! jsonObject.getString("title") .equals( jsonObject.getString("original_title")))
                                    insert(  new Detail( movieID, context, jsonObject.getString( context), ""));
                                break;

                            case "genres":
                                JSONArray jsonArray = jsonObject.getJSONArray( context);
                                String content = jsonArray .getJSONObject(0) .getString("name");
                                for (int i = 1; i < jsonArray.length(); i++) {
                                    content += "\n" + jsonArray .getJSONObject( i) .getString("name");
                                }
                                insert(  new Detail( movieID, context, content, ""));
                                break;

                            case "budget":
                            case "revenue":
                                if (jsonObject.getInt(context) > 0)
                                    insert(  new Detail( movieID, context, jsonObject.getString( context), ""));
                                break;

                            case "homepage":
                            case "imdb_id":
                                insert(  new Detail( movieID, context, jsonObject.getString( context), ""));
                                break;

                        }
                    }


                    for (String context : Detail.CONTEXTs.subList( 12 /* videos , reviews */, 14 )) {

                        String res = getResponseFromHttpUrl(  buildUrl( apiKey, (movieID + "/" + context)));
                        JSONArray jsonArray = (new JSONObject ( Objects.requireNonNull (res))).getJSONArray("results");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray .getJSONObject (i);
                            switch (context) {

                                case "videos":
                                    if (jsonObj.getString("type").equals("Trailer"))
                                        insert( new Detail(  movieID,  context,  /*content*/ jsonObj.getString("name"),  /*link*/ jsonObj.getString("key")));
                                    break;

                                case "reviews":
                                    insert( new Detail(  movieID,  context,  /*content*/ jsonObj.getString("author"),
                                            /*link*/ ((jsonObj.getString("url"))
                                            .replace("https://www.themoviedb.org/review/", "")
                                            .equals( jsonObj.getString("id")))
                                            ?   jsonObj.getString("id")
                                            :   jsonObj.getString("url")
                                    ));
                                    break;
                            }
                        }
                    }
                } catch (JSONException e) { e.printStackTrace();  Log .e(this.getClass().getSimpleName(), "E R R O R  in  integrate("+ movieID + "):\tJSONException"); }
                } catch (IOException e)   { e.printStackTrace();  Log .e(this.getClass().getSimpleName(), "E R R O R  in  integrate("+ movieID + "):\tIOException"); }
            });
    }


    public void insert( Detail detail) { AppExecutors.getInstance().diskIO().execute( () -> this.appDatabase .detailDao() .insert(detail)); }


    public String buildUrl( @NonNull String apiKey, String key) {
        try { return  new URL( Uri
                .parse( "https://api.themoviedb.org/3/movie")
                .buildUpon()
                .appendEncodedPath( key)
                .appendQueryParameter("api_key", apiKey)
                .appendQueryParameter("language", "en-US")
                .build()
                .toString()
        ).toString();
        } catch ( MalformedURLException e) {
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName() + "::buildUrl()\t", "\tMalformedURLException");
            return null;
        }
    }
}
