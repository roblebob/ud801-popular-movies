package com.roblebob.ud801_popular_movies;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.io.IOException;
import java.util.List;

import static com.roblebob.ud801_popular_movies.AppUtilities.getResponseFromHttpUrl;

public class MainViewModel extends AndroidViewModel{

    private AppDatabase mAppDatabase;

    private MainRepository mainRepository;
    private DetailRepository detailRepository;

    private LiveData< String> apiKeyLive;
    private LiveData< String> orderLive;

    private LiveData< List<Main>> popularMovieListLive;
    private LiveData< List<Main>> topRatedMovieListLive;
    private LiveData< List<Main>> movieListLiveByDatabase;
    private MediatorLiveData< List<Main>> movieListLiveByMediator;

    public LiveData< List<Main>> getMovieListLiveByDatabase() { return movieListLiveByDatabase; }
    public MediatorLiveData< List<Main>> getMovieListLiveByMediator() { return movieListLiveByMediator; }


    public MainViewModel(@NonNull Application application) {
        super( application);
        mAppDatabase = AppDatabase.getInstance( application);
        //AppExecutors.getInstance().diskIO().execute( () -> mAppDatabase.appStateDao().insert(new AppState("api_key", null)));
        AppExecutors.getInstance().diskIO().execute( () -> mAppDatabase.appStateDao().insert(new AppState("order", "popular")));
        mainRepository = new MainRepository( mAppDatabase);
        detailRepository = new DetailRepository(  mAppDatabase);

        ////////////////////////////////////////////////////////////////////////////////////////////

        orderLive = mAppDatabase.appStateDao().loadState("order");
        apiKeyLive = mAppDatabase.appStateDao().loadState("api_key");

        popularMovieListLive  = mainRepository.getPopularListLive();
        topRatedMovieListLive = mainRepository.getTopRatedListLive();

        movieListLiveByDatabase = mainRepository.getListLive(null);

        movieListLiveByMediator = new MediatorLiveData<>() ;
        movieListLiveByMediator .addSource( orderLive,             value -> movieListLiveByMediator .setValue( combine( orderLive, popularMovieListLive, topRatedMovieListLive )));
        movieListLiveByMediator .addSource( popularMovieListLive,  value -> movieListLiveByMediator .setValue( combine( orderLive, popularMovieListLive, topRatedMovieListLive )));
        movieListLiveByMediator .addSource( topRatedMovieListLive, value -> movieListLiveByMediator .setValue( combine( orderLive, popularMovieListLive, topRatedMovieListLive )));
    }

    public List<Main> combine(
            LiveData< String> orderLive,
            LiveData< List<Main>> popularMovieListLive,
            LiveData< List<Main>> topRatedMovieListLive) {

        if (orderLive.getValue() != null)
            switch (orderLive.getValue()) {
                case "popular":     return popularMovieListLive.getValue();
                case "top_rated":   return topRatedMovieListLive.getValue();
            }
        return null;
    }


    public void setOrder(String order) { AppExecutors.getInstance().diskIO().execute( () ->{
        mAppDatabase .appStateDao() .update( new AppState( "order", order ));
        Log.e(this.getClass().getSimpleName() + " :: setOrder(...", "--->  " + order );
    }); }

    public LiveData< String> getOrder(){
        return mAppDatabase.appStateDao().loadState("order");
    }
    /* *********************************************************************************************
     *
     *
     * @param apiKey
     */
    public LiveData< String> getApiKeyLive() { return apiKeyLive; }

    public void start( String apiKey) {
        AppExecutors.getInstance().networkIO().execute( () -> {
            try {
                // dummy request simply to verify the apiKey
                String response = getResponseFromHttpUrl("https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey);

                // IF no IOException is thrown THEN:
                AppExecutors.getInstance().diskIO().execute( () ->
                        mAppDatabase.appStateDao() .update( new AppState("api_key", apiKey)));

                AppExecutors.getInstance().mainThread().execute( () ->
                        Toast.makeText(  getApplication().getApplicationContext(), "apiKey accepted", Toast.LENGTH_SHORT).show());

                mainRepository .integrate( apiKey);


            } catch (IOException e) { e.printStackTrace();


                AppExecutors .getInstance() .diskIO() .execute( () ->
                        mAppDatabase.appStateDao().insert(new AppState("api_key", null)));

                AppExecutors.getInstance().mainThread().execute( () ->
                        Toast.makeText(  getApplication().getApplicationContext(), "apiKey rejected", Toast.LENGTH_SHORT).show());
            }
        });
    }


    public final LiveData< Integer> countMovies() {         return mainRepository.countMovies();}
    public final LiveData< Integer> countDetailedMovies() { return detailRepository.countMovies();}
}
