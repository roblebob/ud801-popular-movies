package com.roblebob.ud801_popular_movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mAppDatabase = AppDatabase .getInstance( getApplicationContext());

        // if (savedInstanceState != null && savedInstanceState.containsKey( INSTANCE_ID))  ID = savedInstanceState .getInt( INSTANCE_ID, -1);

        mDetailsRV = (RecyclerView) this.findViewById( R.id.activity_details_RECYCLER_VIEW);
        mDetailsRVLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mDetailsRV.setLayoutManager(mDetailsRVLayoutManager);
        mDetailsRVAdapter = new DetailsRVAdapter();
        mDetailsRV .setAdapter(mDetailsRVAdapter);
        mDetailsRV .setHasFixedSize( false);
        ////////////////////////////////////////////////////////////////////////////////////////////

        Intent intent = getIntent();
        if ( intent != null && intent.hasExtra( EXTRA_MID))
            MID = intent .getIntExtra( EXTRA_MID, -1);


        if (MID > 0) {

            DetailsViewModelFactory detailsViewModelFactory = new DetailsViewModelFactory( mAppDatabase, MID);
            final DetailsViewModel detailsViewModel = ViewModelProviders.of(this, detailsViewModelFactory) .get( DetailsViewModel.class);

            detailsViewModel .getMovieLive() .observe(this, new Observer< Movie>() {
                @Override
                public void onChanged( Movie movie) {

                    detailsViewModel .getMovieLive() .removeObserver( this);
                    Log.d( TAG, ">+>+>+>+>+>>+>+>+>" + "Receiving database update from LiveData  " + movie.toString());
                    populateUI( new Movie( movie));
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
    private void  populateUI( Movie movie) {

        if (movie.getTitle() != null)                       ((TextView) findViewById( R.id.activity_details_HEADING_TITLE_textview))            .setText( movie .getTitle());
        if (movie.getTitleORIG() != null &&
                movie.getLangORIG() != null)
            if (movie.getTitleORIG()
                    .equals(movie.getTitle())) {
                                                            ((TextView) findViewById( R.id.activity_details_HEADING_ORG_TITLE))                 .setVisibility( View.GONE);
                                                            ((TextView) findViewById( R.id.activity_details_HEADING_ORG_LANG))                  .setVisibility( View.GONE);
                                                            ((TextView) findViewById( R.id.activity_details_HEADING_ORG_TITLE_left_bracket))    .setVisibility( View.GONE);
                                                            ((TextView) findViewById( R.id.activity_details_HEADING_ORG_TITLE_right_bracket))   .setVisibility( View.GONE);
            }
        else {
                                                            ((TextView) findViewById( R.id.activity_details_HEADING_ORG_TITLE))                 .setVisibility( View.VISIBLE);
                                                            ((TextView) findViewById( R.id.activity_details_HEADING_ORG_LANG))                  .setVisibility( View.VISIBLE);
                                                            ((TextView) findViewById( R.id.activity_details_HEADING_ORG_TITLE_left_bracket))    .setVisibility( View.VISIBLE);
                                                            ((TextView) findViewById( R.id.activity_details_HEADING_ORG_TITLE_right_bracket))   .setVisibility( View.VISIBLE);

                                                            ((TextView) findViewById( R.id.activity_details_HEADING_ORG_TITLE))                 .setText( movie .getTitleORIG());
            if (movie.getLangORIG() != null)        ((TextView) findViewById( R.id.activity_details_HEADING_ORG_LANG))                  .setText( movie .getLangORIG());
        }
        if (movie.getPosterID() != null)                   Picasso .get() .load( "http://image.tmdb.org/t/p/w185/" + movie .getPosterID()) .into( (ImageView) findViewById( R.id.imageView));
        if (movie.getReleasePIT() != null)                 ((TextView) findViewById( R.id.release_date))                                       .setText( movie .getReleasePIT().split("-")[0]);

        if (movie.getRuntimeVAL() != null)                     ((TextView) findViewById( R.id.activity_details_RUNTIME))                           .setText( movie .getRuntimeVAL());
        if (movie.getVoteAVG() > 0)                     ((TextView) findViewById( R.id.rating))                                             .setText( String.valueOf(movie.getVoteAVG()));
        if (movie.getTagline() != null)                     ((TextView) findViewById( R.id.activity_details_TAGLINE))                           .setText( movie .getTagline());
        if (movie.getOverview() != null)                    ((TextView) findViewById( R.id.activity_details_OVERVIEW))                          .setText( movie .getOverview());
        if (movie.getGenres() != null)                      ((TextView) findViewById( R.id.activity_details_GENRES))                            .setText( movie .getGenres() .substring(1, movie .getGenres().length()-1) .replace(",", "\n"));
        if (movie .getBudgetVAL() != null)
            if (Integer.parseInt( movie.getBudgetVAL()) > 0)   ((TextView) findViewById( R.id.activity_Details_BUDGET))                            .setText( movie .getBudgetVAL());
        if (movie .getRevenueVAL() != null)
            if (Integer.parseInt( movie.getRevenueVAL()) > 0)  ((TextView) findViewById( R.id.activity_Details_REVENUE))                           .setText( movie .getRevenueVAL());

        if (movie.getHomepageURL() != null)
            if (movie.getHomepageURL().length() != 0)       mDetailsRVAdapter .setHomepageUrl(  movie .getImdbURL());

        if (movie.getImdbURL() != null)
            if (movie.getImdbURL().length() != 0)           mDetailsRVAdapter .setImdbUrl(      movie .getImdbURL());

        Log .e(TAG + "::populateUI() \t", movie.toString() );
    }





    @Override
    public void onItemClickListener(int pos) {




    }
}
