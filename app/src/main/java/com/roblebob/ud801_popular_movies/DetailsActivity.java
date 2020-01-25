package com.roblebob.ud801_popular_movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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

public class DetailsActivity extends AppCompatActivity  implements DetailsRVAdapter.ItemClickListener  {
    private static final String TAG = DetailsActivity.class.getSimpleName();
    // Extra for the task ID to be received in the intent
    public static final String EXTRA_ID = "EXTRA_ID";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_ID = "INSTANCE_ID";

    private int ID;

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
        if ( intent != null && intent.hasExtra(EXTRA_ID))
            ID = intent .getIntExtra( EXTRA_ID, -1);


        if (ID > 0) {
            NetworkUtils.integrateDetails( mAppDatabase, ID);

            DetailsViewModelFactory detailsViewModelFactory = new DetailsViewModelFactory( mAppDatabase, ID);
            final DetailsViewModel detailsViewModel = ViewModelProviders.of(this, detailsViewModelFactory) .get( DetailsViewModel.class);

            detailsViewModel .getMovieLive() .observe(this, new Observer< Movie>() {
                @Override
                public void onChanged( Movie movie) {

                    detailsViewModel .getMovieLive() .removeObserver( this);
                    Log.d( TAG, ">+>+>+>+>+>>+>+>+>" + "Receiving database update from LiveData  " + movie.toString());
                    populateUI( new Movie( movie));
                }
            });
        } else Log .e(this.getClass().getSimpleName(), "ERROR");
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void  populateUI( Movie movie) {

        Log .e(TAG + "::populateUI() \t", movie.toString() );

        mDetailsRVAdapter .setTrailerNames(  movie .getTrailerNames());
        mDetailsRVAdapter .setTrailerUrls(   movie .getTrailerUrls());
        mDetailsRVAdapter .setReviewAuthors( movie .getReviewAuthors());
        mDetailsRVAdapter .setReviewUrls(    movie .getReviewUrls());


        if (movie.getTitle() != null)                   ((TextView) findViewById( R.id.activity_details_HEADING_TITLE_textview))    .setText( movie .getTitle());
        if (movie.getOriginalTitle() != null &&
                movie.getOriginalLanguage() != null)
            if (movie.getOriginalTitle()
                    .equals(movie.getTitle()))          ((View)     findViewById( R.id.activity_details_HEADING_ORG_TITLE_group))   .setVisibility( View.GONE);
        else {
                                                        ((View)     findViewById( R.id.activity_details_HEADING_ORG_TITLE_group))   .setVisibility( View.VISIBLE);
                                                        ((TextView) findViewById( R.id.activity_details_HEADING_ORG_TITLE))         .setText( movie .getOriginalTitle());
            if (movie.getOriginalLanguage() != null)    ((TextView) findViewById( R.id.activity_details_HEADING_ORG_LANG))          .setText( movie .getOriginalLanguage());
        }
        if (movie.getPosterUrl() != null)               Picasso .get() .load( movie .getPosterUrl()) .into( (ImageView) findViewById( R.id.imageView));
        if (movie.getReleaseDate() != null)             ((TextView) findViewById( R.id.release_date))                               .setText( movie .getReleaseDate().split("-")[0]);

        if (movie.getRuntime() != null)                 ((TextView) findViewById( R.id.activity_details_RUNTIME))                   .setText( movie .getRuntime());
        if (movie.getVoteAverage() > 0)                 ((TextView) findViewById( R.id.rating))                                     .setText( String.valueOf(movie.getVoteAverage()));
        if (movie.getTagline() != null)                 ((TextView) findViewById( R.id.activity_details_TAGLINE))                   .setText( movie .getTagline());
        if (movie.getOverview() != null)                ((TextView) findViewById( R.id.activity_details_OVERVIEW))                  .setText( movie .getOverview());
        if (movie.getGenres() != null)                  ((TextView) findViewById( R.id.activity_details_GENRES))                    .setText( movie .getGenres() .substring(1, movie .getGenres().length()-1) .replace(",", "\n"));
        if (movie .getBudget() != null) if (Integer.parseInt( movie.getBudget()) > 0)    ((TextView) findViewById( R.id.activity_Details_BUDGET))                    .setText( movie .getBudget());
        if (movie .getRevenue() != null) if (Integer.parseInt( movie.getRevenue()) > 0)   ((TextView) findViewById( R.id.activity_Details_REVENUE))                   .setText( movie .getRevenue());



    }


    @Override
    public void onItemClickListener(int pos) {




    }
}
