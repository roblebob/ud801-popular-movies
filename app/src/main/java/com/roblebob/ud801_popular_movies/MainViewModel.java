package com.roblebob.ud801_popular_movies;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MainViewModel extends ViewModel {

    private LiveData< List< Movie>> mPopularMovieListLive;
    private LiveData< List< Movie>> mTopRatedMovieListLive;
    private AppDatabase mAppDatabase;

    public MainViewModel(@NonNull AppDatabase appDatabase) {

        mAppDatabase = appDatabase;
    }

    public LiveData< List< Movie>> getPopularMovieListLive() { return mAppDatabase .movieDao() .loadPopularMovies(); }
    public LiveData< List< Movie>> getTopRatedMovieListLive() { return mAppDatabase .movieDao() .loadTopRatedMovies(); }
}
