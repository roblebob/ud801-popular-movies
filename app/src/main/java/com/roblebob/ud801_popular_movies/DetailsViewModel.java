package com.roblebob.ud801_popular_movies;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;


public class DetailsViewModel extends ViewModel {


    private AppDatabase appDatabase;
    private int MID;
    private LiveData< Movie> mMovieLive;
    private LiveData< List< MovieExtra>> mMovieExtraLive;



    public DetailsViewModel(@NonNull AppDatabase appDatabase, int MID) {

        this.appDatabase = appDatabase;
        this.MID = MID;
        mMovieLive = appDatabase .movieDao() .loadMovieByMID( MID);
        mMovieExtraLive = appDatabase .movieExtraDao() .loadExtrasByMID( MID);
    }

    public LiveData< Movie> getMovieLive() { return mMovieLive; }
    public LiveData< List< MovieExtra>> getExtraListLive() { return appDatabase .movieExtraDao(). loadExtrasByMID( MID); }
}
