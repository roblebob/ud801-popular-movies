package com.roblebob.ud801_popular_movies;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

public class MainViewModel extends AndroidViewModel{
    private AppDatabase mAppDatabase;
    private MovieRepository movieRepository;
    private XtraRepository  xtraRepository;

    public  LiveData< List< Movie>> movieListLive;
    private LiveData< List< Movie>> popularMovieListLive;
    private LiveData< List< Movie>> topRatedMovieListLive;
    private LiveData< Integer> orderedbyTabPositionLive;
    private MediatorLiveData movieListLiveMediated;


    public LiveData<List<Movie>> getPopularMovieListLive() {
        return popularMovieListLive;
    }
    public LiveData<List<Movie>> getTopRatedMovieListLive() {
        return topRatedMovieListLive;
    }
    public LiveData<Integer> getOrderedbyTabPositionLive() {
        return orderedbyTabPositionLive;
    }
    public MediatorLiveData< List< Movie>> getMovieListLiveMediated() { return movieListLiveMediated; }





    public MainViewModel( @NonNull Application application) {
        super( application);
        mAppDatabase = AppDatabase.getInstance( application);
        movieRepository = new MovieRepository( mAppDatabase);
        xtraRepository  = new XtraRepository(  mAppDatabase);

        popularMovieListLive  = movieRepository.getPopularMovieListLive();
        topRatedMovieListLive = movieRepository.getTopRatedMovieListLive();
        orderedbyTabPositionLive = movieRepository.getOrderedbyTabPositionLive();

        movieListLiveMediated = new MediatorLiveData< List< Movie>>() ;
        movieListLiveMediated.addSource(orderedbyTabPositionLive, value -> movieListLiveMediated.setValue(combine( orderedbyTabPositionLive, popularMovieListLive, topRatedMovieListLive )));
        movieListLiveMediated.addSource(popularMovieListLive,     value -> movieListLiveMediated.setValue(combine( orderedbyTabPositionLive, popularMovieListLive, topRatedMovieListLive )));
        movieListLiveMediated.addSource(topRatedMovieListLive,    value -> movieListLiveMediated.setValue(combine( orderedbyTabPositionLive, popularMovieListLive, topRatedMovieListLive )));

    }

    public List< Movie> combine(
            LiveData< Integer> orderedbyTabPositionLive,
            LiveData< List< Movie>> popularMovieListLive,
            LiveData< List< Movie>> topRatedMovieListLive) {

        if (orderedbyTabPositionLive.getValue() != null) if (orderedbyTabPositionLive.getValue() == 1)
            return topRatedMovieListLive.getValue();

        return popularMovieListLive.getValue();
    }



    public void start( String apiKey) {
        movieRepository .start( apiKey);
        xtraRepository  .start( apiKey);

    }

    public void setOrderedbyTabPosition( int position) {                                Log.e(this.getClass().getSimpleName(), " ----> setOrderedbyTabPosition( " + position + " )");
        movieRepository .insertOrderedbyTabPosition( position);
    }

    public final LiveData< Integer> countMovies() {         return movieRepository .countMovies();}
    public final LiveData< Integer> countDetailedMovies() { return xtraRepository  .countMovies();}

    public void integrateXtras( Integer movieID) { xtraRepository .integrate(movieID.toString());}
}
