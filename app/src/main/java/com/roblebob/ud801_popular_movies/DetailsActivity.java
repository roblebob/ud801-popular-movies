package com.roblebob.ud801_popular_movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.CompoundButtonCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity  implements DetailsRVAdapter.ItemClickListener  {
    private static final String TAG = DetailsActivity.class.getSimpleName();
    // Extra for the task ID to be received in the intent
    public static final String EXTRA_MID = "EXTRA_ID";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_MID = "INSTANCE_ID";

    private int MID;

    private AppDatabase mAppDatabase;

    private RecyclerView mDetailsRV;
    private DetailsRVAdapter mDetailsRVAdapter;
    private RecyclerView.LayoutManager mDetailsRVLayoutManager;
    private Movie mMovie;
    private Button favButton;
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mAppDatabase = AppDatabase .getInstance( getApplicationContext());

        Intent intent = getIntent();
        if ( intent != null && intent.hasExtra( EXTRA_MID))
            MID = intent .getIntExtra( EXTRA_MID, -1);

        // if (savedInstanceState != null && savedInstanceState.containsKey( INSTANCE_ID))  ID = savedInstanceState .getInt( INSTANCE_ID, -1);

        mDetailsRV = (RecyclerView) this.findViewById( R.id.activity_details_RECYCLER_VIEW);
        mDetailsRVLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mDetailsRV.setLayoutManager(mDetailsRVLayoutManager);
        mDetailsRVAdapter = new DetailsRVAdapter();
        mDetailsRV .setAdapter(mDetailsRVAdapter);
        mDetailsRV .setHasFixedSize( false);

        favButton = (Button) findViewById(R.id.activity_details_BUTTON_fav);
        favButton.setOnClickListener(v -> {

            mMovie.inverseFav();
            AppExecutors.getInstance().diskIO().execute(
                    () -> mAppDatabase.movieDao().updateMovie( mMovie));
            Log .e(TAG, "CLICKED!!   " + mMovie.isFav());
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        if (MID > 0) {
            DetailsViewModelFactory detailsViewModelFactory = new DetailsViewModelFactory( mAppDatabase, MID);
            final DetailsViewModel detailsViewModel = ViewModelProviders.of(this, detailsViewModelFactory) .get( DetailsViewModel.class);

            detailsViewModel .getMovieLive() .observe(this, new Observer< Movie>() {
                @Override
                public void onChanged( Movie movie) {

                    detailsViewModel .getMovieLive() .removeObserver( this);
                    mMovie = new Movie( movie);
                    populateUI();
                }
            });

            detailsViewModel .getExtraListLive() .observe(this, new Observer< List< MovieExtra>>() {
                @Override
                public void onChanged( List< MovieExtra> movieExtraList) {

                    detailsViewModel .getExtraListLive() .removeObserver( this);
                    Log.d( TAG, ">+>+>+>+>+>>+>+>+>" + "Receiving database update from LiveData  " + movieExtraList.toString());
                    mDetailsRVAdapter .setExtras( new ArrayList<>( movieExtraList));
                }
            });

        } else Log .e(this.getClass().getSimpleName(), "ERROR");
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void  populateUI() {
        Log.e(TAG, "change has occured -->  populateUI()");
        // assigning values to the UI's equivalents
        if (mMovie.getPosterID() != null)   Picasso .get() .load( mMovie .getPosterURL()) .into( (ImageView) findViewById( R.id.imageView));



        if (mMovie .isFav())  {
            favButton .setCompoundDrawableTintList( ColorStateList.valueOf(getResources().getColor(R.color.colorYellow)));
        }
        else {
            favButton .setCompoundDrawableTintList( ColorStateList.valueOf(getResources().getColor(R.color.colorGray)));
        }

        ((TextView) findViewById( R.id.activity_details_HEADING_TITLE_textview)) .setText(  (mMovie .getTitle() != null)    ?    mMovie .getTitle()        : "");
        ((TextView) findViewById( R.id.activity_details_HEADING_ORG_TITLE)) .setText(   (mMovie.getTitleORIG()  != null)    ?    mMovie .getTitleORIG()    : "" );
        ((TextView) findViewById( R.id.activity_details_HEADING_ORG_LANG))  .setText(   (mMovie.getLangORIG()   != null)    ?    mMovie .getLangORIG()     : "" );
        ((TextView) findViewById( R.id.release_date)            )           .setText(   (mMovie.getReleasePIT() != null)    ?    mMovie .getReleasePIT() .split("-")[0]    : "" );
        ((TextView) findViewById( R.id.activity_details_RUNTIME))           .setText(   (mMovie.getRuntimeVAL() != null)    ?    mMovie .getRuntimeVAL()   : "" );
        ((TextView) findViewById( R.id.rating)                  )           .setText(   (mMovie.getVoteAVG()     >  0  )    ?    String.valueOf(mMovie.getVoteAVG())    : "" );
        ((TextView) findViewById( R.id.activity_details_TAGLINE))           .setText(   (mMovie.getTagline()    != null)    ?    mMovie .getTagline()      : "" );
        ((TextView) findViewById( R.id.activity_details_OVERVIEW))          .setText(   (mMovie.getOverview()   != null)    ?    mMovie .getOverview()     : "" );
        ((TextView) findViewById( R.id.activity_details_GENRES) )           .setText(   (mMovie.getGenres()     != null)    ?    mMovie .getGenres() .substring(1, mMovie .getGenres().length()-1) .replace(",", "\n")  : "");
        ((TextView) findViewById( R.id.activity_Details_BUDGET) )           .setText(   (mMovie.getBudgetVAL()  != null)    ?    (Integer.parseInt( mMovie .getBudgetVAL()) > 0)   ?   mMovie .getBudgetVAL()   : "" : "" );
        ((TextView) findViewById( R.id.activity_Details_REVENUE))           .setText(   (mMovie.getRevenueVAL() != null)    ?    (Integer.parseInt( mMovie .getRevenueVAL()) > 0)  ?   mMovie .getRevenueVAL()  : "" : "" );
        mDetailsRVAdapter   .setHomepageUrl(  (mMovie.getHomepageURL() != null)    ?    (mMovie.getHomepageURL().length() != 0)    ?    mMovie .getImdbURL()    : "" : "" );
        mDetailsRVAdapter   .setImdbUrl(      (mMovie.getImdbURL() != null)        ?    (mMovie.getImdbURL().length() != 0)        ?    mMovie .getImdbURL()    : "" : "" );


        // TODO: hide all empties
        if (    ((TextView) findViewById( R.id.activity_details_HEADING_ORG_TITLE)) .getText() ==
                ((TextView) findViewById( R.id.activity_details_HEADING_TITLE_textview)) .getText())   {

            ((TextView) findViewById(R.id.activity_details_HEADING_ORG_TITLE)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.activity_details_HEADING_ORG_LANG)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.activity_details_HEADING_ORG_TITLE_left_bracket)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.activity_details_HEADING_ORG_TITLE_right_bracket)).setVisibility(View.VISIBLE);
        }
    }





    @Override
    public void onItemClickListener(int pos) {
        Log .e(TAG, "CLICKED !!!");



    }
}
