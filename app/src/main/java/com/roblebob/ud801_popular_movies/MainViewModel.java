package com.roblebob.ud801_popular_movies;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

public class MainViewModel extends ViewModel {
    private final AppStateRepository appStateRepo;
    private final MainRepository mainRepo;
    private final DetailRepository detailRepo;
    public final LiveData< String> orderLive;
    public final LiveData< String> apiKeyLive;
    public final LiveData< List< Main>> mainListByDatabaseLive;
    public final LiveData< Integer> movieCountLive;
    public final LiveData< Integer> detailedMovieCountLive;
//    private final MediatorLiveData< List< Main>> movieListLiveByMediator;
    private AppDatabase appDatabase;

    public MainViewModel( @NonNull final AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        this.appStateRepo =  new AppStateRepository( appDatabase);
        this.mainRepo     =  new MainRepository( appDatabase);
        this.detailRepo   =  new DetailRepository( appDatabase);

        this.orderLive              =  appStateRepo .getOrderLive();
        this.apiKeyLive             =  appStateRepo .getApiKeyLive();
        this.mainListByDatabaseLive =  mainRepo     .getListLive();
        this.movieCountLive         =  mainRepo     .getMovieCount();
        this.detailedMovieCountLive =  detailRepo   .getMovieCountLive();
//        final LiveData< List< Main>>  popularListLive  =  mainRepo .getPopularListLive();
//        final LiveData< List< Main>>  topRatedListLive =  mainRepo .getTopRatedListLive();
////        movieListLiveByMediator =  new MediatorLiveData<>() ;
//        movieListLiveByMediator .addSource( orderLive,          (value) -> movieListLiveByMediator .setValue(   combine( orderLive, popularListLive, topRatedListLive)));
//        movieListLiveByMediator .addSource( popularListLive,    (value) -> movieListLiveByMediator .setValue(   combine( orderLive, popularListLive, topRatedListLive)));
//        movieListLiveByMediator .addSource( topRatedListLive,   (value) -> movieListLiveByMediator .setValue(   combine( orderLive, popularListLive, topRatedListLive)));
    }
//
//    public List<Main> combine(
//            LiveData< String>     orderLive,
//            LiveData< List<Main>> popularMovieListLive,
//            LiveData< List<Main>> topRatedMovieListLive) {
//
//        if (orderLive.getValue() != null)
//            switch (orderLive.getValue()) {
//                case "popular":     return popularMovieListLive.getValue();
//                case "top_rated":   return topRatedMovieListLive.getValue();
//            }
//        return null;
//    }

    //public LiveData< String> getOrderLive() { return appDatabase .appStateDao() .loadOrder(); }
    //public LiveData< String> getApiKeyLive() { return appDatabase .appStateDao() .loadApiKey(); }



    public void start(    String apiKey) { mainRepo .integrate(apiKey);   Log.e(this.getClass().getSimpleName() + "::start()\t",     "\t<---\t" + apiKey); }
    public void setOrder( String order)  {
        appStateRepo.setOrder(order);
        Log.e(this.getClass().getSimpleName() + "::setOrder()\t",  "\t<---\t" + order);
    }
    public void setApiKey(String apiKey) {
        appStateRepo.setApiKey(apiKey);
        Log.e(this.getClass().getSimpleName() + "::setApiKey()\t", "\t<---\t" + apiKey); }


//    public final MediatorLiveData< List< Main>> getMovieListLiveByMediator()  { return  movieListLiveByMediator; }
}
