package com.roblebob.ud801_popular_movies;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    private TabLayout tabLayout;
    private TabLayout .OnTabSelectedListener onTabSelectedListener;


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

        tabLayout = findViewById( R.id.activity_main_TABLAYOUT);
        onTabSelectedListener = new TabLayout .OnTabSelectedListener() {
            @Override public void onTabSelected(   TabLayout.Tab tab) { mainViewModel .setOrder( AppUtilities.ORDER.get( tab.getPosition())); }
            @Override public void onTabUnselected( TabLayout.Tab tab) {}
            @Override public void onTabReselected( TabLayout.Tab tab) { mainViewModel .setOrder( AppUtilities.ORDER.get( tab.getPosition())); }
        };




        new AppConnectivity( getApplicationContext()) .observe( this, new Observer<Boolean>() {
            @Override public void onChanged(Boolean aBoolean) {
                if (aBoolean) /* available */ {
                    ((ImageView)   findViewById( R.id.activity_main_TOOLBAR_state_disp_IMAGE_VIEW))   .setColorFilter(     getApplicationContext() .getColor( R.color.colorPrimary));
                    ((ProgressBar) findViewById( R.id.activity_main_TOOLBAR_state_disp_PROGRESS_BAR)) .setVisibility( View.VISIBLE);
                    ((CardView)    findViewById( R.id.activitity_main_MAIN_COUNT))                    .setBackgroundColor( getApplicationContext() .getColor( R.color.colorPrimaryLight));
                    ((CardView)    findViewById( R.id.activitity_main_DETAILED_COUNT))                .setBackgroundColor( getApplicationContext() .getColor( R.color.colorPrimaryLight));
                    ((TextView)    findViewById( R.id.activitity_main_MAIN_COUNT_label))              .setTextColor(       getApplicationContext() .getColor( R.color.colorPrimaryLight));
                    ((TextView)    findViewById( R.id.activitity_main_DETAILED_COUNT_label))          .setTextColor(       getApplicationContext() .getColor( R.color.colorPrimaryLight));
                } else /* lost */ {
                    ((ImageView)   findViewById( R.id.activity_main_TOOLBAR_state_disp_IMAGE_VIEW))   .setColorFilter( getApplicationContext() .getColor( R.color.colorGray));
                    ((ProgressBar) findViewById( R.id.activity_main_TOOLBAR_state_disp_PROGRESS_BAR)) .setVisibility( View.GONE);
                    ((CardView)    findViewById( R.id.activitity_main_MAIN_COUNT))                    .setBackgroundColor( getApplicationContext() .getColor( R.color.colorGray));
                    ((CardView)    findViewById( R.id.activitity_main_DETAILED_COUNT))                .setBackgroundColor( getApplicationContext() .getColor( R.color.colorGray));
                    ((TextView)    findViewById( R.id.activitity_main_MAIN_COUNT_label))              .setTextColor( getApplicationContext() .getColor( R.color.colorGray));
                    ((TextView)    findViewById( R.id.activitity_main_DETAILED_COUNT_label))          .setTextColor( getApplicationContext() .getColor( R.color.colorGray));
                }
            }
        });


        mainViewModel .apiKeyLive .observe(this, new Observer< String>() {
            @Override public void onChanged( @Nullable String apiKey) {
                //mainViewModel .apiKeyLive .removeObserver( this); 
                if (apiKey != null)  {
                    mainViewModel .apiKeyLive .removeObserver( this);
                    ((ConstraintLayout)  findViewById( R.id.activity_main_INITIAL_SETUP)         ) .setVisibility( View.GONE);
                    ((TabLayout)         findViewById( R.id.activity_main_TABLAYOUT)             ) .setVisibility( View.VISIBLE);
                    ((ConstraintLayout)  findViewById( R.id.activity_main_TOOLBAR_state_disp)    ) .setVisibility( View.VISIBLE);
                    ((CardView)          findViewById( R.id.activitity_main_MAIN_COUNT)          ) .setVisibility( View.VISIBLE);
                    ((TextView)          findViewById( R.id.activitity_main_MAIN_COUNT_label)    ) .setVisibility( View.VISIBLE);
                    ((CardView)          findViewById( R.id.activitity_main_DETAILED_COUNT)      ) .setVisibility( View.VISIBLE);
                    ((TextView)          findViewById( R.id.activitity_main_DETAILED_COUNT_label)) .setVisibility( View.VISIBLE);
                    ((RecyclerView)      findViewById( R.id.activity_main_RV)                    ) .setVisibility( View.VISIBLE);
                    mainViewModel .start( apiKey);
                } else {
                    ((ConstraintLayout)  findViewById( R.id.activity_main_INITIAL_SETUP)         ) .setVisibility( View.VISIBLE);
                    ((TabLayout)         findViewById( R.id.activity_main_TABLAYOUT)             ) .setVisibility( View.GONE);
                    ((ConstraintLayout)  findViewById( R.id.activity_main_TOOLBAR_state_disp)    ) .setVisibility( View.VISIBLE);
                    ((CardView)          findViewById( R.id.activitity_main_MAIN_COUNT)          ) .setVisibility( View.GONE);
                    ((TextView)          findViewById( R.id.activitity_main_MAIN_COUNT_label)    ) .setVisibility( View.GONE);
                    ((CardView)          findViewById( R.id.activitity_main_DETAILED_COUNT)      ) .setVisibility( View.GONE);
                    ((TextView)          findViewById( R.id.activitity_main_DETAILED_COUNT_label)) .setVisibility( View.GONE);
                    ((RecyclerView)      findViewById( R.id.activity_main_RV)                    ) .setVisibility( View.GONE);
                    ((TextInputEditText) findViewById( R.id.activity_main_INITIAL_SETUP_textInputLayout_tv)) .setOnEditorActionListener(   (v, actionId, event) -> {
                        mainViewModel.setApiKey(v.getText().toString());
                        return false;   //  FALSE -> keyboard display goes into hiding
                    });
                    if ( ((ProgressBar) findViewById(R.id.activity_main_TOOLBAR_state_disp_PROGRESS_BAR)) .getVisibility() == View.VISIBLE)  // isConnected : BOOLEAN
                        Toast .makeText( getApplicationContext(), "apiKey rejected!", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(  getApplicationContext(), "NO connectivity!!!", Toast.LENGTH_SHORT).show();

                }
            }
        });


        mainViewModel .orderLive .observe(this, new Observer< String>() {
            @Override public void onChanged( @Nullable  String order) {
                //mainViewModel .orderLive .removeObserver( this);
                if (order != null) {
                    mMainRVAdapter .submitOrder(  order);
                    populateTabLayout( order);
                } else
                    mainViewModel.setOrder("popular");  // ... the DEFAULT
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



        mainViewModel .lastPositionLive .observe(this, new Observer< String>() {
            @Override public void onChanged(String s) {
                if (s != null) {
                    int position = Integer.parseInt(s);
                    int offset = getApplicationContext().getResources().getInteger(R.integer.MainRV_offset_when_return);
                    mMainRV.scrollToPosition( position + offset);
                    Log.e(TAG, "---LastPosition--->\t" + position );
                }
            }
        });
    }


    private void populateTabLayout(@NonNull String order) {
        Log.e(TAG, "----order--->\t" + order + "\t\t" + order.equals("popular") + "\t\t" + order.equals("top_rated"));
        final MainViewModel mainViewModel = new ViewModelProvider(this, this.mainViewModelFactory) .get( MainViewModel.class);

        tabLayout .removeOnTabSelectedListener(onTabSelectedListener);
        tabLayout .removeAllTabs();
        tabLayout .addTab( tabLayout.newTab().setText( R.string.TAB_popular_label  ), 0, order.equals(getString( R.string.TAB_popular_label)));
        tabLayout .addTab( tabLayout.newTab().setText( R.string.TAB_top_rated_label), 1, order.equals(getString( R.string.TAB_top_rated_label)));
        tabLayout .addOnTabSelectedListener( onTabSelectedListener);
    }



    @Override public void onItemClickListener( int position, int movieID) {

        final MainViewModel mainViewModel = new ViewModelProvider(this, this.mainViewModelFactory) .get( MainViewModel.class);
        mainViewModel.setLastPosition(String.valueOf(position));
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent .putExtra( DetailActivity.INTENT_EXTRA_movieID, movieID);
        startActivity( intent);
    }

}
