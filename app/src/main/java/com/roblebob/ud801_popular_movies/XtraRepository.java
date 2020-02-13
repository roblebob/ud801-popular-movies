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
public class XtraRepository {
    private AppDatabase appDatabase;
    public XtraRepository(@NonNull AppDatabase appDatabase) {  this.appDatabase = appDatabase; }

    /* **********************************************************************************************
     * Validates the given apiKey by sending an arbitrary dummy request,
     * i.e. if invalid, IOEception is thrown.
     *
     * @param apiKey
     */

    public void start(String apiKEY) {
        Log.e(this.getClass().getSimpleName(), "----->  Xtra Repository started with apikey: " + apiKEY);
        AppExecutors.getInstance().networkIO().execute(() -> {
            try {
                // dummy request
                String response = getResponseFromHttpUrl( buildUrl(apiKEY, 2, null));

                // if IOEception is NOT thrown DO:

                AppExecutors.getInstance().diskIO().execute(  () -> {
                    this.appDatabase .xtraDao() .update( new Xtra(1,  apiKEY));
                    Log.e (this.getClass().getSimpleName(), "R e p o s i t y -->  apiKey: " + apiKEY +  " accepted");
                });
            } catch (IOException e) { e.printStackTrace();
                AppExecutors.getInstance().diskIO().execute(  () -> {
                    this.appDatabase .xtraDao() .insert( new Xtra(1, null));
                    Log.e (this.getClass().getSimpleName(), "R e p o s i t y -->  apiKey: " + apiKEY + "  rejected");
                });
            }
        });

        Log.e (this.getClass().getSimpleName(), "R e p o s i t y -->  S t a r t e d   w i t h   A P I  K E Y :   " + apiKEY);
    }


    /* *********************************************************************************************
     *
     * @param movieID
     * @return
     */
    public LiveData< List<Xtra>>  getXtraListLive( int movieID)  { return this.appDatabase .xtraDao() .loadXtraList( movieID); }
    public LiveData< List<Xtra>>  getNonlinksXtraListLive( int movieID)  { return this.appDatabase .xtraDao() .loadNonlinksXtraList( movieID); }
    public LiveData< List<Xtra>>  getLinksXtraListLive( int movieID)  { return this.appDatabase .xtraDao() .loadLinksXtraList( movieID); }




    public LiveData< Integer> countDetailedMovies() { return this.appDatabase .xtraDao().countMovies(); }


    /* **********************************************************************************************
     *
     * @param movieID
     */
    public  void integrate( int movieID)  {
        final String apiKEY = this.appDatabase .xtraDao().loadPrime().getValue();
        AppExecutors .getInstance() .networkIO() .execute( () -> {
            try { try {

                        JSONObject  jsonObject = new JSONObject(  Objects .requireNonNull(
                                getResponseFromHttpUrl( buildUrl ( Objects.requireNonNull(
                                        this.appDatabase
                                                .xtraDao() .loadPrime().getValue()),movieID, null))));

                        for (String attribute : Xtra.ATTRIBUTE_list) {
                            switch (attribute) {
                                case "title":
                                case "release_date":
                                case "runtime":
                                case "tagline":
                                case "overview":
                                case "homepage":
                                case "imdb_key":
                                    insert( new Xtra(movieID,attribute,   jsonObject.getString(attribute)));
                                    break;
                                case "budget":
                                case "revenue":  if (jsonObject.getInt(attribute) > 0)
                                    insert( new Xtra(movieID,attribute,   jsonObject.getString(attribute)));
                                    break;
                                case "original_title":
                                case "original_language":  if ( ! jsonObject.getString("title") .equals( jsonObject.getString("original_title")))
                                    insert( new Xtra(movieID,attribute,   jsonObject.getString(attribute)));
                                    break;
                                case ("genres"):
                                    insert( new Xtra(movieID,attribute,   jsonObject.getJSONArray(attribute).toString()));
                                    break;
                            }
                        }

                        ////////////////////////////////////////////////////////////////////////////
                        for (String attribute : Xtra.multi_ATTRIBUTE_list) {

                            JSONArray  jsonArray  = (new JSONObject ( Objects.requireNonNull (
                                                            getResponseFromHttpUrl ( buildUrl (
                                                                    apiKEY, movieID, attribute))))
                                                    ).getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObj = jsonArray .getJSONObject (i);

                                switch (attribute) {

                                    case "videos":
                                        if (jsonObj.getString("type").equals("Trailer")) {
                                            String s = jsonObj.getString("key")     + "," +    jsonObj.getString("name");
                                            insert( new Xtra(movieID,attribute,i,  s));
                                        }
                                        break;
                                    case "review":
                                        String s =  jsonObj.getString("url")     + "," +    jsonObj.getString("author");
                                        insert( new Xtra( movieID,attribute,i,  s));
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
     * @param xtra
     */
    public  void insert( Xtra xtra) { insertExec(xtra);}
    private void insertAsync( final Xtra xtra) { new Thread(                                  () -> this.appDatabase .xtraDao() .insert(xtra)).start(); }
    private void insertExec(  final Xtra xtra) { AppExecutors.getInstance().diskIO().execute( () -> this.appDatabase .xtraDao() .insert(xtra)); }




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
