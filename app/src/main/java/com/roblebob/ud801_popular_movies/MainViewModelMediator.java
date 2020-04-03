package com.roblebob.ud801_popular_movies;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MainViewModelMediator extends ViewModel {
    private final AppStateRepository appStateRepo;
    private final MainRepository     mainRepo;
    private final DetailRepository   detailRepo;
    public  final LiveData< String>      orderLive;
    public  final LiveData< String>      apiKeyLive;
    public  final LiveData< String>      lastPositionLive;
    public  final LiveData< List< Main>> mainListByDatabaseLive;
    public  final LiveData< Integer>     movieCountLive;
    public  final LiveData< Integer>     detailedMovieCountLive;
    private final MediatorLiveData< List< Main>> movieListLiveByMediator;


    public MainViewModelMediator(@NonNull final AppDatabase appDatabase) {
        this.appStateRepo =  new AppStateRepository( appDatabase);
        this.mainRepo     =  new MainRepository(     appDatabase);
        this.detailRepo   =  new DetailRepository(   appDatabase);
        this.orderLive              =  appStateRepo .getOrderLive();
        this.apiKeyLive             =  appStateRepo .getApiKeyLive();
        this.lastPositionLive       =  appStateRepo .getLastPosition();
        this.mainListByDatabaseLive =  mainRepo     .getListLive();
        this.movieCountLive         =  mainRepo     .getMovieCount();
        this.detailedMovieCountLive =  detailRepo   .getMovieCountLive();


        final LiveData< List< Main>>  popularListLive  =  mainRepo .getPopularListLive();
        final LiveData< List< Main>>  topRatedListLive =  mainRepo .getTopRatedListLive();
        movieListLiveByMediator =  new MediatorLiveData<>() ;
        movieListLiveByMediator .addSource( orderLive,          (value) -> movieListLiveByMediator .setValue(   combine( orderLive, popularListLive, topRatedListLive)));
        movieListLiveByMediator .addSource( popularListLive,    (value) -> movieListLiveByMediator .setValue(   combine( orderLive, popularListLive, topRatedListLive)));
        movieListLiveByMediator .addSource( topRatedListLive,   (value) -> movieListLiveByMediator .setValue(   combine( orderLive, popularListLive, topRatedListLive)));
    }

    public List<Main> combine(
            LiveData< String>     orderLive,
            LiveData< List<Main>> popularMovieListLive,
            LiveData< List<Main>> topRatedMovieListLive) {

        if (orderLive.getValue() != null)
            switch (orderLive.getValue()) {
                case "popular":     return popularMovieListLive.getValue();
                case "top_rated":   return topRatedMovieListLive.getValue();
            }
        return null;
    }

    public final MediatorLiveData< List< Main>> getMovieListLiveByMediator()  { return  movieListLiveByMediator; }


    public void start(    String apiKey) { mainRepo .integrate(apiKey); }
    public void setOrder( String order)  { appStateRepo.setOrder(order); }
    public void setApiKey(String apiKey) { appStateRepo.setApiKey(apiKey); }
    public void setLastPosition(String position) { appStateRepo.setLastPosition(position); }
}
