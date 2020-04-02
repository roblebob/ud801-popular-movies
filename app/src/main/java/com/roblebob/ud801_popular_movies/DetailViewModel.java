package com.roblebob.ud801_popular_movies;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;


public class DetailViewModel extends ViewModel {

    private AppDatabase appDatabase;
    private MainRepository mainRepository;
    private DetailRepository detailRepository;
    private int movieID;


    public DetailViewModel( AppDatabase appDatabase, int movieID) {
        this.appDatabase = appDatabase;
        mainRepository   = new MainRepository(   this.appDatabase);
        detailRepository = new DetailRepository( this.appDatabase);
        this.movieID = movieID;
    }

    public LiveData< String>        getApiKeyLive() { return appDatabase.appStateDao().loadState("api_key"); }
    public LiveData< Main>          getMainLive()   { return mainRepository .getMainLive( this.movieID);   }
    public LiveData< List< Detail>> getListLive()   { return detailRepository .getListLive( this.movieID); }
    public LiveData< Integer> getDetailsPerMovieCountLive(int movieID) { return detailRepository.getDetailsCountPerMovieLive(movieID); }

    public void integrate( String apiKey) { detailRepository.integrate( apiKey, this.movieID);}

    public void inverseFavorite() { mainRepository.inverseFavorite( this.movieID);}

    public void setIsDetailed(int movieID) { mainRepository.setMovieIsDetailed(movieID); }
}
