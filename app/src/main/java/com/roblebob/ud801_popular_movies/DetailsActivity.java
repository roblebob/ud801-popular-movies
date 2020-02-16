package com.roblebob.ud801_popular_movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class DetailsActivity extends AppCompatActivity  implements DetailsRVAdapter.ItemClickListener  {
    private static final String TAG = DetailsActivity.class.getSimpleName();
    // Extra for the task ID to be received in the intent
    public static final String EXTRA_movieID = "EXTRA_movieID";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_movieID = "INSTANCE_ID";

    private int movieID;

    private AppDatabase mAppDatabase;

    private RecyclerView mDetailsRV;
    private DetailsRVAdapter mDetailsRVAdapter;
    private RecyclerView.LayoutManager mDetailsRVLayoutManager;
    private Button favButton;
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        if ( intent != null && intent.hasExtra(EXTRA_movieID))
            movieID = intent .getIntExtra(EXTRA_movieID, -1);
        // if (savedInstanceState != null && savedInstanceState.containsKey( INSTANCE_ID))  ID = savedInstanceState .getInt( INSTANCE_ID, -1);
        if (movieID > 0) {


            DetailsViewModelFactory detailsViewModelFactory = new DetailsViewModelFactory(this.getApplication(), movieID);
            final DetailsViewModel detailsViewModel = ViewModelProviders.of(this, detailsViewModelFactory).get(DetailsViewModel.class);

            mDetailsRV = (RecyclerView) this.findViewById( R.id.activity_details_RECYCLER_VIEW);
            mDetailsRVLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            mDetailsRV .setLayoutManager( mDetailsRVLayoutManager);
            mDetailsRVAdapter = new DetailsRVAdapter(this);
            mDetailsRV .setAdapter( mDetailsRVAdapter);
            mDetailsRV .setHasFixedSize(false);

            favButton = (Button) findViewById( R.id.activity_details_BUTTON_fav);
            favButton .setOnClickListener( v -> detailsViewModel .getMovieLive( movieID) .observe(this,
                    (movie) -> { AppExecutors.getInstance().diskIO().execute(  () -> mAppDatabase .movieDao() .update((movie.inverseFav())));          Log.e(TAG, "CLICKED!!" + movie.isFav());
                                }));


            detailsViewModel .getMovieLive(  movieID) .observe(this,
                    new Observer<Movie>() { @Override public void onChanged( Movie movie) {
                        detailsViewModel .getMovieLive( movieID) .removeObserver( this);
                        populateMovieIntoUI(movie);
                    }});

            detailsViewModel .getNonlinksXtraListLive(  movieID) .observe(this,
                    new Observer< List< Xtra>>() { @Override public void onChanged( List<Xtra> xtraList) {
                        detailsViewModel .getNonlinksXtraListLive(movieID) .removeObserver( this);
                        Log.d( TAG, ">+>+>+>+>+>>+>+>+>" + "Receiving database (xtraNonlinks)update from LiveData  " + xtraList.toString());
                        populateNonlinksFromXtraListIntoUI(xtraList);
                    }});
            detailsViewModel .getLinksXtraListLive(  movieID) .observe(this,
                    new Observer< List< Xtra>>() { @Override public void onChanged( List<Xtra> xtraList) {
                        detailsViewModel .getLinksXtraListLive(movieID) .removeObserver( this);
                        Log.d( TAG, ">+>+>+>+>+>>+>+>+>" + "Receiving database (xtraLinks) update from LiveData  " + xtraList.toString());
                        populateLinksXtraIntoUI(xtraList);
                    }});
        } else Log .e(this.getClass().getSimpleName(), "ERROR, invalid movieID= " + movieID);
    }





    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////


    /* *********************************************************************************************
     *
     * @param movie
     */
    private void populateMovieIntoUI(Movie movie) {                                                 Log.d(TAG, "change has occured -->  populateMovieIntoUI()");
        if (movie.getKey() != null)
            Picasso .get() .load( movie.getPosterURL()) .into( (ImageView) findViewById( R.id.imageView));

        favButton .setCompoundDrawableTintList( ColorStateList.valueOf(getResources().getColor(
                movie.isFav()  ?  R.color.colorYellow  :  R.color.colorGray
        )));

        ((TextView) findViewById( R.id.activity_details_RATING_tv)) .setText(  String.valueOf(movie.getVoteAVG()));
    }


    /**
     *
     * @param xtraList
     */
    private void populateNonlinksFromXtraListIntoUI(List< Xtra> xtraList) {                                  Log.d(TAG, "change has occured -->  populateNonlinksFromXtraListIntoUI()");
        xtraList.forEach( (Xtra xtra) -> {

            int subID = xtra.getExtraID() % 100;
            if (0 <= subID && subID < 18) {
                String attribute = Xtra.ATTRIBUTE_list.get(subID);
                switch(attribute) {

                    case "title":
                        ((TextView) findViewById( R.id.activity_details_TITLE_tv)) .setText( xtra.getValue() );
                        break;
                    case "original_title":
                         ((TextView) findViewById( R.id.activity_details_ORIGINAL_TITLE)) .setText( xtra.getValue());
                         ((TextView) findViewById( R.id.activity_details_ORIGINAL_TITLE)) .setVisibility( View.VISIBLE);
                        break;
                    case "original_language":
                        ((TextView) findViewById( R.id.activity_details_ORIGINAL_LANGUAGE_tv))        .setText( xtra.getValue());
                        ((TextView) findViewById( R.id.activity_details_ORIGINAL_LANGUAGE_tv))        .setVisibility( View.VISIBLE);
                        ((TextView) findViewById( R.id.activity_details_ORIGINAL_TITLE_left_bracket)) .setVisibility( View.VISIBLE);
                        ((TextView) findViewById( R.id.activity_details_ORIGINAL_TITLE_right_bracket)).setVisibility( View.VISIBLE);
                        break;
                    case "release_date":
                        ((TextView) findViewById( R.id.activity_details_RELEASE_DATE_tv))  .setText( xtra.getValue().split("-")[0]);  // take only the year
                        ((TextView) findViewById( R.id.activity_details_RELEASE_DATE_tv))  .setVisibility(View.VISIBLE);
                        break;
                    case "runtime":
                        ((TextView) findViewById( R.id.activity_details_RUNTIME_tv))              .setText( xtra.getValue());
                        ((TextView) findViewById( R.id.activity_details_RUNTIME_tv))              .setVisibility( View.VISIBLE);
                        ((TextView) findViewById( R.id.activity_details_RUNTIME_unit_minutes_tv)) .setVisibility( View.VISIBLE);
                        break;
                    case "tagline":
                        ((TextView) findViewById(R.id.activity_details_TAGLINE_tv))   .setText( xtra.getValue());
                        ((TextView) findViewById(R.id.activity_details_TAGLINE_tv))   .setVisibility( View.VISIBLE);
                        break;
                    case "overview":
                        ((TextView) findViewById(R.id.activity_details_OVERVIEW_tv))  .setText( xtra.getValue());
                        ((TextView) findViewById(R.id.activity_details_OVERVIEW_tv))  .setVisibility( View.VISIBLE);
                        break;
                    case "genres":
                        ((TextView) findViewById(R.id.activity_details_GENRES_tv))       .setText( xtra.getValue());
                        ((TextView) findViewById(R.id.activity_details_GENRES_tv))       .setVisibility( View.VISIBLE);
                        ((TextView) findViewById(R.id.activity_details_GENRES_LABEL_tv)) .setVisibility( View.VISIBLE);
                        break;
                    case "budget":
                        ((TextView) findViewById(R.id.activity_Details_BUDGET_tv))                  .setText( xtra.getValue());
                        ((TextView) findViewById(R.id.activity_Details_BUDGET_tv))                  .setVisibility( View.VISIBLE);
                        ((TextView) findViewById(R.id.activity_details_BUDGET_LABEL_tv))            .setVisibility( View.VISIBLE);
                        ((TextView) findViewById(R.id.activity_details_BUDGET_UNIT_dollarsign_tv))  .setVisibility( View.VISIBLE);
                    case "revenue":
                        ((TextView) findViewById(R.id.activity_Details_REVENUE_tv))                 .setText(xtra.getValue());
                        ((TextView) findViewById(R.id.activity_Details_REVENUE_tv))                 .setVisibility( View.VISIBLE);
                        ((TextView) findViewById(R.id.activity_details_REVENUE_LABEL_tv))           .setVisibility( View.VISIBLE);
                        ((TextView) findViewById(R.id.activity_details_REVENUE_UNIT_dollarsign_tv)) .setVisibility( View.VISIBLE);
                        break;
                };
            }
        });
    }

    /**
     *
     * @param xtraList
     */
    private void populateLinksXtraIntoUI(  List< Xtra> xtraList) {
        mDetailsRVAdapter .setLinksXtraList( xtraList);
    }


    /**
     *
     * @param type
     * @param url
     */
    @Override
    public void onItemClickListener(String type, String url) {
        Log .e(TAG, "CLICKED !!!   [type]:" + type + "   [url]:" + url);
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( (new URL(url).toString())));
            startActivity(browserIntent);
        } catch (MalformedURLException e) { e.printStackTrace(); }


    }
}
