package com.roblebob.ud801_popular_movies;

import androidx.lifecycle.LiveData;

import java.util.List;

public  abstract class AppRepository< T> {

    public abstract List< String> ORDER();
    public abstract void start(String key);
    public abstract void integrate(String key);
    public abstract void insert(T t);
    public abstract LiveData< Integer> countMovies();
    public abstract LiveData< List< T>> getListLive(String key);

    //abstract String buildURL(String s1, String s2, int i);
}
