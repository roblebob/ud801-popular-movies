package com.roblebob.ud801_popular_movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    // Extra for the task ID to be received in the intent
    public static final String EXTRA_ID = "EXTRA_ID";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_ID = "INSTANCE_ID";

    private int ID;

    private AppDatabase mAppDatabase;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mAppDatabase = AppDatabase .getInstance( getApplicationContext());

    //    if (savedInstanceState != null && savedInstanceState.containsKey( INSTANCE_ID))
      //      ID = savedInstanceState .getInt( INSTANCE_ID, -1);


        Intent intent = getIntent();
        if ( intent != null && intent.hasExtra(EXTRA_ID))
            ID = intent .getIntExtra( EXTRA_ID, -1);


        if (ID != -1) {
            DetailsViewModelFactory detailsViewModelFactory = new DetailsViewModelFactory( mAppDatabase, ID);
            final DetailsViewModel detailsViewModel = ViewModelProviders.of(this, detailsViewModelFactory) .get( DetailsViewModel.class);

            detailsViewModel .getMovieLive() .observe(this, new Observer< Movie>() {
                @Override
                public void onChanged( Movie movie) {

                    detailsViewModel .getMovieLive() .removeObserver( this);
                    Log.d( this.getClass().getSimpleName(), ">+>+>+>+>+>>+>+>+>" + "Receiving database update from LiveData");
                    populateUI( new Movie( movie));
                }
            });
        } else Log .e(this.getClass().getSimpleName(), "ERROR");
    }

    private void  populateUI( Movie movie) {
        ((TextView) findViewById( R.id.title))          .setText( movie .getTitle());
        ((TextView) findViewById( R.id.release_date))   .setText( movie .getReleaseDate().split("-")[0]);
        ((TextView) findViewById( R.id.overview))       .setText( movie .getOverview());
        ((TextView) findViewById( R.id.rating))         .setText( String.valueOf( movie .getVoteAverage()));
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        String path = NetworkUtils .buildUrlForMoviePosterImage( movie .getPosterPath(), NetworkUtils.SIZE_list.get(0)).toString();
        Picasso .get().load( path).into( imageView);



    }
}
