package com.roblebob.ud801_popular_movies;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;


public class MainViewModel extends AndroidViewModel{
    private MovieRepository movieRepository;
    private XtraRepository xtraRepository;
    private Application application;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        this.movieRepository = new MovieRepository( AppDatabase.getInstance( application));
        this.xtraRepository  = new XtraRepository(  AppDatabase.getInstance( application));
    }


    // I N I T I A L I N G   -  E N T R Y    -    P O I N T
    public void start( String apiKey) { apiKeyLiveInput .setValue( apiKey); }
    private final MutableLiveData< String> apiKeyLiveInput = new MutableLiveData< String>();
    public  final LiveData< String>  apiKeyLive = Transformations.switchMap(  apiKeyLiveInput,  (apiKey) -> {
        movieRepository.start( apiKey);
        xtraRepository .start( apiKey);
        return xtraRepository .getApikeyLive();
    });





    public void integrateMovies() { movieRepository.integrate(apiKeyLive.getValue());}
    public void integrateXtras( int movieID) { xtraRepository.integrate(movieID);}



    public void setOrderedbyLiveInput( String orderedby) { orderedbyLiveInput.setValue( orderedby);}
    private final MutableLiveData< String> orderedbyLiveInput = new MutableLiveData< String>();
    public  final LiveData< List< Movie>> movieListLive  =  Transformations.switchMap( orderedbyLiveInput,  (orderedby) -> movieRepository .getMovieListLive( orderedby));


    public final LiveData< Integer> countDetailedMovies() { return xtraRepository.countDetailedMovies();}

}
