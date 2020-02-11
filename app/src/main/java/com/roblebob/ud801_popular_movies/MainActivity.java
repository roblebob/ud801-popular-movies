package com.roblebob.ud801_popular_movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;


public class MainActivity extends AppCompatActivity implements MainRVAdapter.ItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MainViewModelFactory mMainViewModelFactory;
    private RecyclerView  mMainRV;
    private MainRVAdapter mMainRVAdapter;
    private RecyclerView.LayoutManager mRVLayoutManager;
    private Bundle        mPresentState;
    private TextView      mMovieBasicsCountTv, mMovieDetailedCountTv;
    private TabLayout     mTabLayout;
    private String        mApiKey =     null;


    @Override public void onItemClickListener( int movieID) {

        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent .putExtra( DetailsActivity.EXTRA_movieID, movieID);
        startActivity( intent);
    }





    @Override protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresentState = (savedInstanceState == null) ? new Bundle() : savedInstanceState;
        if (mPresentState.getString("orderType") == null)
            mPresentState.putString("orderType", "popular");

        setContentView(R.layout.activity_main);

        mMovieBasicsCountTv   = findViewById( R.id.activitity_main_BASIC_COUNT_tv);
        mMovieDetailedCountTv = findViewById( R.id.activitity_main_DETAILED_COUNT_tv);

        mMainViewModelFactory = new MainViewModelFactory( this.getApplication());
        MainViewModel mainViewModel = ViewModelProviders.of(this, mMainViewModelFactory) .get(MainViewModel.class);



        /* * * * * * * * * * * * *
         *  R E C Y C L E R   V I E W
         *
         */
        mMainRV = (RecyclerView) this.findViewById(R.id.activity_main_RECYCLER_VIEW);
        mRVLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        ((LinearLayoutManager) mRVLayoutManager).setInitialPrefetchItemCount(100);
        mMainRV.setLayoutManager(mRVLayoutManager);
        mMainRVAdapter = new MainRVAdapter( this);
        mainViewModel.movieListLive.observe(this, list -> mMainRVAdapter.submitList(list));
        mMainRV.setAdapter(mMainRVAdapter);
        mMainRV.setHasFixedSize(true);


        /* * * * * * * * * * * * *
         *  B A R
         *
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_TOOLBAR);
        //if (toolbar != null)  setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setFitsSystemWindows(true);


        /* * * * * * * * * * * * *
         *  T A B
         *
         */
        mTabLayout = (TabLayout) findViewById(R.id.activity_main_TAB);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab)   {
                int position = tab.getPosition();
                String orderedby = AppUtilities.ORDEREDBY_list.get(position);
                mainViewModel .setOrderedbyLiveInput( orderedby);     }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });





        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        ////
        Log .e(TAG, "----------->");


        /* * * * * * * * * * * * *
         *  A P I   K E Y
         *
         */
        mainViewModel  .apiKeyLive  .observe(this, new Observer<String>() {
            @Override public void onChanged( @NonNull String apiKey) {
                mainViewModel.apiKeyLive .removeObserver(this);
                Log.e(TAG, "---->   " + "Receiving database update for API KEY from LiveData:  " + apiKey);

                if ( apiKey == null) {  /* invalid case: */
                    Log .e(TAG, "----------->    invalid case");
                    ((ConstraintLayout) findViewById( R.id.activity_main_INITIAL_SETUP)     ).setVisibility(View.VISIBLE);
                    ((TabLayout)        findViewById( R.id.activity_main_TAB)               ).setVisibility(View.GONE);
                    ((ConstraintLayout) findViewById( R.id.activity_main_TOOLBAR_state_disp)).setVisibility(View.GONE);
                    ((RecyclerView)     findViewById( R.id.activity_main_RECYCLER_VIEW)     ).setVisibility(View.GONE);

                    ((TextInputEditText) findViewById(R.id.activity_main_INITIAL_SETUP_textInputLayout_tv))
                            .setOnEditorActionListener(  (v, actionId, event) -> {
                                Log .e(TAG, "----------->    ACTION was called");
                                mainViewModel.start( v.getText().toString());
                                return true;
                            });

                } else {    /* valid case: */
                    Log .e(TAG, "----------->    valid case");
                    mainViewModel .integrateMovies();
                    ((ConstraintLayout) findViewById( R.id.activity_main_INITIAL_SETUP) )     .setVisibility(View.GONE);
                    ((TabLayout)        findViewById( R.id.activity_main_TAB))                .setVisibility(View.VISIBLE);
                    ((ConstraintLayout) findViewById( R.id.activity_main_TOOLBAR_state_disp)) .setVisibility(View.VISIBLE);
                    ((RecyclerView)     findViewById( R.id.activity_main_RECYCLER_VIEW))      .setVisibility(View.VISIBLE);

                }
            }
        });



        /* * * * * * * * * * * * *
         *  M O V I E    L I S T
         *
         */
        mainViewModel  .movieListLive  .observe(this, new Observer<List<Movie>>() {
            @Override public void onChanged(@NonNull List<Movie> movieList) {

                mainViewModel  .movieListLive  .removeObserver(this);
                Log.e( TAG, "-------->   " + "Receiving database update for Movies from LiveData:  " + movieList.toString());
                mMovieBasicsCountTv .setText( String.valueOf( movieList.size()));
            }
        });



        /* * * * * * * * * * * * *
         *  C O U N T :
         *
         *    ( M o v i e s   h a v i n g   X t r a   i n f o r m a t i o n )
         */
        mainViewModel   .countDetailedMovies() .observe(this, new Observer<Integer>() {
            @Override public void onChanged(Integer integer) {

                mainViewModel.countDetailedMovies().removeObserver(this);
                mMovieDetailedCountTv.setText(String.valueOf(integer));
            }
        });

    }














    // TODO : save mPresentState before closing
}
