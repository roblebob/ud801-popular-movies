package com.roblebob.ud801_popular_movies;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

public class MainViewModel extends AndroidViewModel{
    private MovieRepository movieRepository;
    private XtraRepository  xtraRepository;
    private final LiveData< List< Movie>> popularMovieListLive;
    private final LiveData< List< Movie>> topRatedMovieListLive;
    private final LiveData< String> orderedbyLive;
    public final MediatorLiveData< List< Movie>> mediatorLive;


    public MainViewModel(@NonNull Application application) {
        super(application);
        this.movieRepository = new MovieRepository( AppDatabase.getInstance( application));
        this.xtraRepository  = new XtraRepository(  AppDatabase.getInstance( application));
        this.popularMovieListLive  = this.movieRepository .getPopularMovieListLive();
        this.topRatedMovieListLive = this.movieRepository .getTopRatedMovieListLive();
        this.orderedbyLive = movieRepository.orderedbyLive;

        mediatorLive = new MediatorLiveData<>();
        mediatorLive .addSource( orderedbyLive,
            new Observer< String>() {
                @Override
                public void onChanged(String s) {
                    if (s.equals("popular")) {
                        mediatorLive.removeSource(topRatedMovieListLive);
                        mediatorLive.addSource(popularMovieListLive, value -> mediatorLive.setValue(value));
                    } else if (s.equals("top_rated")) {
                        mediatorLive.removeSource(popularMovieListLive);
                        mediatorLive.addSource(topRatedMovieListLive, value -> mediatorLive.setValue(value));
                    } else Log.e(this.getClass().getSimpleName(), "E R R O R !:   invalid  'orderedby':  " + s);
                }
            }
        );
    }


    public void start( String apiKey) {
        movieRepository.start( apiKey);
        xtraRepository.start( apiKey);
    }




    public void setOrderedbyTabPosition(int position) {
        movieRepository.insertOrderedbyTabPosition(position);
    }


    public final LiveData< Integer> countMovies() {         return movieRepository .countMovies();}
    public final LiveData< Integer> countDetailedMovies() { return xtraRepository  .countDetailedMovies();}





    public void integrateXtras(int movieID) { xtraRepository.integrate( movieID);}


}
