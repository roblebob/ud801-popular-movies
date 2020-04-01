package com.roblebob.ud801_popular_movies;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainRVAdapter.ItemClickListener  {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MainRVAdapter mMainRVAdapter;
    public Context context;
    private AppDatabase appDatabase;
    private MainViewModelFactory mainViewModelFactory;


    @Override protected void onCreate( Bundle savedInstanceState) {
        super .onCreate( savedInstanceState);
        setContentView( R.layout.activity_main);
        context = MainActivity.this;
        appDatabase = AppDatabase.getInstance( this.context);
        mainViewModelFactory = new MainViewModelFactory( this.appDatabase);
        final MainViewModel mainViewModel = new ViewModelProvider(this, this.mainViewModelFactory) .get( MainViewModel.class);

        RecyclerView mMainRV = (RecyclerView) this.findViewById( R.id.activity_main_RV);
        RecyclerView.LayoutManager mRVLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        ((LinearLayoutManager) mRVLayoutManager) .setInitialPrefetchItemCount( 100);
        mMainRV .setLayoutManager( mRVLayoutManager);
        mMainRVAdapter = new MainRVAdapter(this);
        mMainRV .setAdapter( mMainRVAdapter);
        mMainRV .setHasFixedSize( true);

        Toolbar toolbar = findViewById( R.id.activity_main_TOOLBAR);
        if (toolbar != null)  setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar() .setDisplayShowTitleEnabled( false);
        }
        toolbar.setFitsSystemWindows( true);



        mainViewModel .apiKeyLive .observe(this, new Observer< String>() {
            @Override public void onChanged( @Nullable String apiKey) {
                //mainViewModel .apiKeyLive .removeObserver( this);
                populateApiKeyUI( apiKey);
            }
        });

        mainViewModel .orderLive .observe(this, new Observer< String>() {
            @Override public void onChanged( @Nullable  String order) {
                //mainViewModel .orderLive .removeObserver( this);
                if (order != null) {
                    mMainRVAdapter .submitOrder(  order);

                    TabLayout tabLayout = findViewById( R.id.activity_main_TABLAYOUT);
                    //tabLayout .setSelected( true);
                    tabLayout .addOnTabSelectedListener( new TabLayout .OnTabSelectedListener() {
                        @Override public void onTabSelected( TabLayout.Tab tab) { mainViewModel .setOrder( AppUtilities.ORDER.get( tab.getPosition())); }
                        @Override public void onTabUnselected( TabLayout.Tab tab) {}
                        @Override public void onTabReselected( TabLayout.Tab tab) { mainViewModel .setOrder( AppUtilities.ORDER.get( tab.getPosition())); }
                    });

                    switch (order) {
                        case "popular":
                            if ( ((TabItem) findViewById( R.id.activity_main_TAB_popular)) != null)
                                ((TabItem) findViewById( R.id.activity_main_TAB_popular)) .setSelected(true);
                            if ( ((TabItem) findViewById(R.id.activity_main_TAB_top_rated)) != null)
                                ((TabItem) findViewById( R.id.activity_main_TAB_top_rated)) .setSelected(false);
                            break;

                        case "top_rated":
                            if ( ((TabItem) findViewById( R.id.activity_main_TAB_popular)) != null)
                                ((TabItem) findViewById( R.id.activity_main_TAB_popular)) .setSelected(false);
                            if ( ((TabItem) findViewById(R.id.activity_main_TAB_top_rated)) != null)
                                ((TabItem) findViewById( R.id.activity_main_TAB_top_rated)) .setSelected(true);
                            break;
                        default:
                            Log.e(TAG + ":::mainViewModel .orderLive\t\t", "\t\tthe new order \"" + order + "\"  could not be applied !");
                    }
                } else mainViewModel.setOrder("popular");
            }
        });

        mainViewModel .mainListByDatabaseLive .observe(this, new Observer< List< Main>>() {
            @Override public void onChanged( @Nullable  List< Main> mainList) {
                //mainViewModel .mainListByDatabaseLive .removeObserver( this);
                if (mainList != null) mMainRVAdapter .submitList( mainList);
            }
        });

        mainViewModel .movieCountLive .observe(this, new Observer< Integer>() {
            @Override public void onChanged( @Nullable Integer movieCount) {
                //mainViewModel .movieCountLive .removeObserver( this);
                ((TextView) findViewById( R.id.activitity_main_MAIN_COUNT_tv))  .setText(  String.valueOf(  movieCount));
            }
        });

        mainViewModel .detailedMovieCountLive .observe(this, new Observer< Integer>() {
            @Override public void onChanged( @Nullable Integer detailedMovieCount) {
                //mainViewModel .detailedMovieCountLive .removeObserver( this);
                ((TextView) findViewById( R.id.activitity_main_DETAILED_COUNT_tv))  .setText(  String.valueOf(  detailedMovieCount));
            }
        });
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null)  &&  (activeNetwork.isConnectedOrConnecting());
        return isConnected;
    }



    public void populateApiKeyUI( String apiKey) {

        final MainViewModel mainViewModel = new ViewModelProvider(this, this.mainViewModelFactory) .get( MainViewModel.class);

        if (apiKey != null)  {   Toast.makeText(  this.context, "apiKey accepted", Toast.LENGTH_SHORT).show();
            ((ConstraintLayout)  findViewById( R.id.activity_main_INITIAL_SETUP)     )   .setVisibility( View.GONE);
            ((TabLayout)         findViewById( R.id.activity_main_TABLAYOUT)         )   .setVisibility( View.VISIBLE);
            ((ConstraintLayout)  findViewById( R.id.activity_main_TOOLBAR_state_disp))   .setVisibility( View.VISIBLE);
            ((CardView)          findViewById( R.id.activitity_main_MAIN_COUNT)     )   .setVisibility( View.VISIBLE);
            ((TextView)          findViewById( R.id.activitity_main_MAIN_COUNT_label))  .setVisibility( View.VISIBLE);
            ((CardView)          findViewById( R.id.activitity_main_DETAILED_COUNT)     )   .setVisibility( View.VISIBLE);
            ((TextView)          findViewById( R.id.activitity_main_DETAILED_COUNT_label))  .setVisibility( View.VISIBLE);
            ((RecyclerView)      findViewById( R.id.activity_main_RV)                )   .setVisibility( View.VISIBLE);
            mainViewModel .start( apiKey);
            mainViewModel.apiKeyLive.removeObservers(this);

        } else {                 Toast .makeText( this.context, "apiKey rejected", Toast.LENGTH_SHORT).show();
            ((ConstraintLayout)  findViewById( R.id.activity_main_INITIAL_SETUP)     )   .setVisibility( View.VISIBLE);
            ((TabLayout)         findViewById( R.id.activity_main_TABLAYOUT)         )   .setVisibility( View.GONE);
            ((ConstraintLayout)  findViewById( R.id.activity_main_TOOLBAR_state_disp))   .setVisibility( View.VISIBLE);
            ((CardView)          findViewById( R.id.activitity_main_MAIN_COUNT)     )   .setVisibility( View.GONE);
            ((TextView)          findViewById( R.id.activitity_main_MAIN_COUNT_label))  .setVisibility( View.GONE);
            ((CardView)          findViewById( R.id.activitity_main_DETAILED_COUNT)     )   .setVisibility( View.GONE);
            ((TextView)          findViewById( R.id.activitity_main_DETAILED_COUNT_label))  .setVisibility( View.GONE);
            ((RecyclerView)      findViewById( R.id.activity_main_RV)                )   .setVisibility( View.GONE);
            ((TextInputEditText) findViewById( R.id.activity_main_INITIAL_SETUP_textInputLayout_tv)) .setOnEditorActionListener(   (v, actionId, event) -> {
                mainViewModel.setApiKey(v.getText().toString());
                return false;   //  FALSE -> keyboard display goes into hiding
            });
        }
    }



    @Override public void onItemClickListener( int movieID) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent .putExtra( DetailActivity.INTENT_EXTRA_movieID, movieID);
        startActivity( intent);
    }
}
