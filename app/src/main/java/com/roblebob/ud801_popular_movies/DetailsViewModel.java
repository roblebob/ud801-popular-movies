package com.roblebob.ud801_popular_movies;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


public class DetailsViewModel extends ViewModel {

    private LiveData< Movie> mMovieLive;
    private AppDatabase mAppDatabase;
    private int mId;

    public DetailsViewModel(@NonNull AppDatabase appDatabase, int id) {

        mAppDatabase = appDatabase;
        mId = id;
    }

    public LiveData< Movie> getMovieLive() { return mAppDatabase .movieDao() .loadMovieById(mId); }
}
