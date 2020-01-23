package com.roblebob.ud801_popular_movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MainRVAdapter.ItemClickListener  {
    private static final String TAG = MainActivity.class.getSimpleName();
    private AppDatabase mAppDatabase;
    private RecyclerView mRecyclerView;
    private MainRVAdapter mMainRVAdapter;
    private RecyclerView.LayoutManager mRVLayoutManager;
    private Bundle mPresentState;
    private int whatPage(final int position) { return (int) ( Math .floor( position / 20) + 1); }


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        mPresentState = (savedInstanceState == null) ? new Bundle() : savedInstanceState;
        if (mPresentState.getString("orderType") == null) mPresentState.putString("orderType", "popular");
        setContentView( R.layout.activity_main);

        ////////////////////////////////////////////////////////////////////////////////////////////
        mAppDatabase = AppDatabase .getInstance( getApplicationContext());
        MainViewModelFactory mainViewModelFactory = new MainViewModelFactory( mAppDatabase );
        MainViewModel mainViewModel = ViewModelProviders.of(this, mainViewModelFactory) .get( MainViewModel.class);

        mainViewModel .getPopularMovieListLive() .observe(this, new Observer< List< Movie>>() {
            @Override
            public void onChanged(@NonNull List< Movie> movieList) {
                mainViewModel.getPopularMovieListLive() .removeObserver( this);
                Log.e( TAG, "-------->   " + "Receiving database update for Movies from LiveData:  " + movieList.toString());
                mMainRVAdapter .setMovieList( new ArrayList< Movie>( movieList));
            }
        }) ;
        ////////////////////////////////////////////////////////////////////////////////////////////
        mRecyclerView = (RecyclerView) this.findViewById( R.id.activity_main_RECYCLER_VIEW);
        mRVLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        mRecyclerView .setLayoutManager( mRVLayoutManager);
        mMainRVAdapter = new MainRVAdapter( this, this);
        mRecyclerView .setAdapter( mMainRVAdapter);
        mRecyclerView .setHasFixedSize( false);

        ////////////////////////////////////////////////////////////////////////////////////////////
        mRecyclerView .addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (( ! recyclerView .canScrollVertically(1) && dy != 0)  )
                {
                    int position = mRVLayoutManager.getItemCount() - 1;
                    int page = whatPage(position);
                    NetworkUtils.integratePageOfMovies(mAppDatabase, "popular", page + 1);
                }
            }
        });

    }

    @Override
    public void onItemClickListener( int id) {
        Log .e( this.getClass().getSimpleName(),  "ID:" + id);
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent .putExtra( DetailsActivity.EXTRA_ID, id);
        startActivity( intent);
    }

    // TODO : save mPresentState before closing
}
