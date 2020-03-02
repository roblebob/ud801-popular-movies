package com.roblebob.ud801_popular_movies;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase appDatabase;

    public MainViewModelFactory(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    @NonNull @Override public < T extends ViewModel> T create( @NonNull Class< T> modelClass) {
        //noinspection unchecked
        return (T) new MainViewModel( this.appDatabase);
    }
}
