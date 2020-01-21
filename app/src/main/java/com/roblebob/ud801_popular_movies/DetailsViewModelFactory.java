package com.roblebob.ud801_popular_movies;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DetailsViewModelFactory  extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mAppDatabase;
    private final int mId;

    public DetailsViewModelFactory(AppDatabase appDatabase, int id) {
        mAppDatabase = appDatabase;
        mId = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailsViewModel( mAppDatabase, mId);
    }
}
