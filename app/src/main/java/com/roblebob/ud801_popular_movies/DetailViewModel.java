package com.roblebob.ud801_popular_movies;
import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;


public class DetailViewModel extends AndroidViewModel {

    private MainRepository mainRepository;
    private DetailRepository detailRepository;
    private int movieID;


    public DetailViewModel( Application application, int movieID) {
        super( application);
        mainRepository   = new MainRepository(   AppDatabase.getInstance( application));
        detailRepository = new DetailRepository( AppDatabase.getInstance( application));
        this.movieID = movieID;
    }

    public LiveData<Main> getMainLive(int movieID) { return mainRepository .getMovieLive( movieID); }
    public LiveData< List<Detail>> getListLive( int movieID) { return detailRepository .getListLive( String.valueOf(movieID)); }
}
