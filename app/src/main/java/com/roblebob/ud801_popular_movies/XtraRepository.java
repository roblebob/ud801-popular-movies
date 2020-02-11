package com.roblebob.ud801_popular_movies;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

    private MutableLiveData< Integer>  result =  new MutableLiveData<>();
    public  MutableLiveData< Integer>  getResult()  { return  this.result; }
    private final AppDatabase appDatabase;
    private String apiKEY = null;



    public XtraRepository(@NonNull AppDatabase appDatabase) { this.appDatabase = appDatabase; }


    /* **********************************************************************************************
     * Validates the given apiKey by sending an arbitrary dummy request,
     * i.e. if invalid, IOEception is thrown.
     *
     * @param apiKey
     */
    public void start( final String apiKey) {
        AppExecutors.getInstance().networkIO().execute(() -> {
            try {
                String response = getResponseFromHttpUrl( buildUrl(apiKey, 2, null));
                AppExecutors.getInstance().diskIO().execute(  () -> appDatabase.xtraDao() .insert( new Xtra(1,  apiKey)));
            } catch (IOException e) { e.printStackTrace();
                AppExecutors.getInstance().diskIO().execute(  () -> appDatabase.xtraDao() .insert( new Xtra(1, null)));
            }
        });
    }


    public LiveData< String>  getApikeyLive()  { return appDatabase.xtraDao() .loadApikey();}

    public LiveData< List<Xtra>>  getXtraListLive( int movieID)  { return appDatabase.xtraDao() .loadXtraList( movieID); }
    public LiveData< List<Xtra>>  getNonlinksXtraListLive( int movieID)  { return appDatabase.xtraDao() .loadNonlinksXtraList( movieID); }
    public LiveData< List<Xtra>>  getLinksXtraListLive( int movieID)  { return appDatabase.xtraDao() .loadLinksXtraList( movieID); }




    public LiveData< Integer> countDetailedMovies() { return appDatabase.xtraDao().countDetailedMovies(); }


    /* **********************************************************************************************
     *
     * @param movieID
     */
    public  void integrate( final int movieID)  {
        AppExecutors .getInstance() .networkIO() .execute( () -> {
            try { try { try {

                        JSONObject  jsonObject = new JSONObject(  Objects .requireNonNull(
                                getResponseFromHttpUrl( buildUrl ( Objects.requireNonNull(
                                        getApikeyLive().getValue()),movieID, null))));

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
                                                                getApikeyLive().getValue(), movieID, attribute))))
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
                        result.postValue (1);

            } catch (NullPointerException e) { e.printStackTrace(); result.postValue (0); }
            } catch (JSONException e)        { e.printStackTrace(); result.postValue (0); }
            } catch (IOException e)          { e.printStackTrace(); result.postValue (0); }
        });
    }




    /* *********************************************************************************************
     *
     * @param xtra
     */
    private  void insert( Xtra xtra) { insertExec(xtra);}
    private void insertAsync( final Xtra xtra) { new Thread(                                  () ->  appDatabase.xtraDao() .insert(xtra)).start(); }
    private void insertExec(  final Xtra xtra) { AppExecutors.getInstance().diskIO().execute( () ->  appDatabase.xtraDao() .insert(xtra)); }




    /* *********************************************************************************************
     * Generates validated urls as strings for the  themoviedb.org  API
     *
     * @param apiKey
     * @param movieID
     * @param xtraKEY ∈  { null, "videos", "reviews"}
     * @return url as a String, not as a URL !!!
     */
    public static String buildUrl( @NonNull String apiKey, int movieID, String xtraKEY) {
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
        } catch ( MalformedURLException e) { e.printStackTrace(); return null; }
    }
}
