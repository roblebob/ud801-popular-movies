package com.roblebob.ud801_popular_movies;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DetailsViewModelFactory  extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase appDatabase;
    private final int MID;

    public DetailsViewModelFactory(AppDatabase appDatabase, int MID) {
        this.appDatabase = appDatabase;
        this.MID = MID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailsViewModel( appDatabase, MID);
    }
}
