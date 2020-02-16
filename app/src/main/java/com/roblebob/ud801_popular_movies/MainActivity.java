package com.roblebob.ud801_popular_movies;
import androidx.annotation.MainThread;
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
         */
        mMainRV = (RecyclerView) this.findViewById(R.id.activity_main_RECYCLER_VIEW);
        mRVLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        ((LinearLayoutManager) mRVLayoutManager).setInitialPrefetchItemCount(100);
        mMainRV.setLayoutManager(mRVLayoutManager);
        mMainRVAdapter = new MainRVAdapter( this);
        mainViewModel.getMovieListLiveMediated().observe(this, (List< Movie> list) -> mMainRVAdapter.submitList(list));
        mMainRV.setAdapter(mMainRVAdapter);
        mMainRV.setHasFixedSize(true);


        /* * * * * * * * * * * * *
         *   B A R
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_TOOLBAR);
        //if (toolbar != null)  setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setFitsSystemWindows(true);






        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        ////
        Log .e(TAG, "----------->");
        mainViewModel.start("someInvalid");





        /* * * * * * * * * * * * *
         *  C O U N T :   M o v i e s
         */
        mainViewModel  .countMovies()  .observe(this, new Observer< Integer>() {
            @Override public void onChanged( @NonNull Integer movieCount) {
                mainViewModel.countMovies() .removeObserver(this);

                Log.e(TAG, "---->   " + "Receiving database update for MovieCount:  " + movieCount);
                mMovieBasicsCountTv .setText( String.valueOf( movieCount));
                boolean isFirstRun = true;

                // TODO: PRE RUN
                if ( movieCount == 0) {  /* setup invalid */
                    Log .e(TAG, "----------->    invalid case");
                    ((ConstraintLayout)  findViewById( R.id.activity_main_INITIAL_SETUP)     )  .setVisibility(View.VISIBLE);
                    ((TabLayout)         findViewById( R.id.activity_main_TAB)               )  .setVisibility(View.GONE);
                    ((ConstraintLayout)  findViewById( R.id.activity_main_TOOLBAR_state_disp))  .setVisibility(View.GONE);
                    ((RecyclerView)      findViewById( R.id.activity_main_RECYCLER_VIEW)     )  .setVisibility(View.GONE);
                    ((TextInputEditText) findViewById( R.id.activity_main_INITIAL_SETUP_textInputLayout_tv)) .setOnEditorActionListener(
                            (v, actionId, event) -> {

                                Log .e(TAG, "----------->    ACTION was called:   " + v.getText().toString() );
                                 mainViewModel.start( v.getText().toString());

                                 return false;   //  FALSE -> keyboard display goes into hiding
                            });
                } else if (isFirstRun) {
                    /* TODO first run after validation  */   //
                    // Log .e(TAG, "----------->    valid case");
                    ((ConstraintLayout) findViewById( R.id.activity_main_INITIAL_SETUP)     )   .setVisibility(View.GONE);
                    ((TabLayout)        findViewById( R.id.activity_main_TAB)               )   .setVisibility(View.VISIBLE);
                    ((ConstraintLayout) findViewById( R.id.activity_main_TOOLBAR_state_disp))   .setVisibility(View.VISIBLE);
                    ((RecyclerView)     findViewById( R.id.activity_main_RECYCLER_VIEW)     )   .setVisibility(View.VISIBLE);


                    isFirstRun = false;
                }

            }
        });





        /* * * * * * * * * * * * *
         *   C O U N T :   M o v i e s   h a v i n g   X t r a   i n f o r m a t i o n
         */
        mainViewModel   .countDetailedMovies() .observe(this, new Observer<Integer>() {
            @Override public void onChanged(Integer integer) {

                mainViewModel.countDetailedMovies().removeObserver(this);
                mMovieDetailedCountTv.setText(String.valueOf(integer));
            }
        });





        /* * * * * * * * * * * * *
         *   T A B
         */
        mTabLayout = (TabLayout) findViewById(R.id.activity_main_TAB);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override @MainThread public void onTabSelected(TabLayout.Tab tab)   {
                mainViewModel.setOrderedbyTabPosition( tab.getPosition());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });



    }

//    @MainThread
//    private void prepareOrderedby(int orderedbyTabPosition) {
//
//        MainViewModel mainViewModel = ViewModelProviders.of(this, mMainViewModelFactory) .get(MainViewModel.class);
//
//        mainViewModel.setOrderedbyTabPosition( orderedbyTabPosition);
//
//        mainViewModel.getPopularMovieListLive().removeObservers(this);
//        mainViewModel.getTopRatedMovieListLive().removeObservers(this);
//
//        switch (orderedbyTabPosition) {
//            case 0: mainViewModel.getPopularMovieListLive().observe(this, (List< Movie> list) -> mMainRVAdapter.submitList(list));   break;
//            case 1: mainViewModel.getTopRatedMovieListLive().observe(this, (List< Movie> list) -> mMainRVAdapter.submitList(list));  break;
//        }
//    }


    // TODO : save mPresentState before closing
}
