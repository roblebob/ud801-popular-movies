package com.roblebob.ud801_popular_movies;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;


public class DetailViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;
    private MainRepository mainRepository;
    private DetailRepository detailRepository;
    private int movieID;


    public DetailViewModel( Application application, int movieID) {
        super( application);
        appDatabase      = AppDatabase.getInstance( application);
        mainRepository   = new MainRepository(   AppDatabase.getInstance( application));
        detailRepository = new DetailRepository( AppDatabase.getInstance( application));
        this.movieID = movieID;
    }

    public LiveData<String> getApiKeyLive() { return appDatabase.appStateDao().loadState("api_key"); }
    public LiveData<Main> getMainLive() { return mainRepository .getMovieLive( this.movieID); }
    public LiveData< List<Detail>> getListLive() { return detailRepository .getListLive( this.movieID); }

    public void integrate(String apiKey) { detailRepository.integrate( apiKey, this.movieID);}
}
