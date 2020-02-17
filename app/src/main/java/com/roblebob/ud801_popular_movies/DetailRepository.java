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

/***************************************************************************************************
 *
 */
public class DetailRepository extends AppAbstractRepository<Detail> {
    private AppDatabase appDatabase;

    public DetailRepository(@NonNull AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }


    /* *********************************************************************************************
     *
     * @param movieID
     * @return
     */
    public LiveData< List<Detail>> getListLive(String key)  {
        Integer movieID = Integer.parseInt(key);
        return this.appDatabase .detailDao() .loadList( movieID); }




    public LiveData< Integer> countMovies() { return this.appDatabase .detailDao() .countMovies(); }


    /* **********************************************************************************************
     *
     * @param movieID
     */
    public  void integrate( String key)  {
        @NonNull int movieID = Integer .parseInt( key);
        @NonNull final String apiKey = this.appDatabase .appStateDao().loadState("api_key").getValue();
        AppExecutors .getInstance() .networkIO() .execute( () -> {
            try { try {

                        JSONObject  jsonObject = new JSONObject(  Objects .requireNonNull(
                                getResponseFromHttpUrl( buildUrl ( Objects.requireNonNull(
                                        this.appDatabase .appStateDao() .loadState("api_key").getValue()),movieID, null))));


                        for (String category : Detail.ORDER) {
                            switch (category) {
                                case "title":
                                case "release_date":
                                case "runtime":
                                case "tagline":
                                case "overview":
                                case "homepage":
                                case "imdb_key":

                                    insert( new Detail(movieID,category,0,    jsonObject.getString(category)));
                                    break;
                                case "budget":
                                case "revenue":  if (jsonObject.getInt(category) > 0)
                                    insert( new Detail(movieID,category,0,    jsonObject.getString(category)));
                                    break;
                                case "original_title":
                                case "original_language":  if ( ! jsonObject.getString("title") .equals( jsonObject.getString("original_title")))
                                    insert( new Detail(movieID, category,0,   jsonObject.getString(category)));
                                    break;
                                case ("genres"):
                                    insert( new Detail(movieID, category,0,   jsonObject.getJSONArray(category).toString()));
                                    break;
                            }
                        }

                        ////////////////////////////////////////////////////////////////////////////
                        for (String category : Detail.ORDER) {

                            JSONArray  jsonArray  = (new JSONObject ( Objects.requireNonNull (
                                                            getResponseFromHttpUrl ( buildUrl (
                                                                    apiKey, movieID, category))))
                                                    ).getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObj = jsonArray .getJSONObject (i);

                                switch (category) {

                                    case "videos":
                                        if (jsonObj.getString("type").equals("Trailer")) {
                                            String s = jsonObj.getString("key")     + "," +    jsonObj.getString("name");
                                            insert( new Detail(movieID,category,i,  s));
                                        }
                                        break;
                                    case "review":
                                        String s =  jsonObj.getString("url")     + "," +    jsonObj.getString("author");
                                        insert( new Detail( movieID,category,i,  s));
                                        break;
                                }
                            }
                        }

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
    public String buildUrl( @NonNull String apiKey, int movieID, String xtraKEY) {
        try { return  new URL( Uri
                                .parse( "https://api.themoviedb.org/3/movie")
                                .buildUpon()
                                .appendPath( String.valueOf( movieID))
                                .appendPath( xtraKEY)
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
