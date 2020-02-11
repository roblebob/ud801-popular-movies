package com.roblebob.ud801_popular_movies;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MainViewModelFactory  extends ViewModelProvider.AndroidViewModelFactory {

    private final Application application;

    public MainViewModelFactory( Application application) {
        super(application);
        this.application = application;
    }

    @NonNull @Override public < T extends ViewModel> T create( @NonNull Class< T> modelClass) {
        //noinspection unchecked
        return (T) new MainViewModel( this.application);
    }
}
