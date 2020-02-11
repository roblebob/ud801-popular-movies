package com.roblebob.ud801_popular_movies;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class DetailsViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;
    private XtraRepository xtraRepository;
    private int movieID;


    public DetailsViewModel( Application application, int movieID) {
        super( application);
        movieRepository = new MovieRepository( AppDatabase.getInstance(application) );
        xtraRepository =  new XtraRepository(  AppDatabase.getInstance(application) );
        this.movieID = movieID;
    }

    public LiveData<Movie> getMovieLive(int movieID) { return movieRepository.getMovieLive( movieID); }
    public LiveData< List<Xtra>> getNonlinksXtraListLive(int movieID) { return xtraRepository.getNonlinksXtraListLive( movieID); }
    public LiveData< List<Xtra>> getLinksXtraListLive(int movieID) { return xtraRepository.getLinksXtraListLive( movieID); }
}
