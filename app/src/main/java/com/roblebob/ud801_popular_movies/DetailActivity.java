package com.roblebob.ud801_popular_movies;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import static java.lang.String.*;

public class DetailActivity extends AppCompatActivity  implements DetailRVAdapter.ItemClickListener  {
    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final String INTENT_EXTRA_movieID = "INTENT_EXTRA_movieID";
    private int movieID;
    private DetailRVAdapter mDetailRVAdapter;

    @Override protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_detail);

        Intent intent = getIntent();
        if ( intent != null && intent.hasExtra(INTENT_EXTRA_movieID))
            movieID = intent .getIntExtra(INTENT_EXTRA_movieID, -1);
        if (movieID > 0) {

            DetailViewModelFactory detailViewModelFactory = new DetailViewModelFactory(AppDatabase.getInstance(getApplicationContext()), movieID);
            final DetailViewModel detailViewModel = new ViewModelProvider(this, detailViewModelFactory).get( DetailViewModel.class);

            ImageView favoriteStar = (ImageView) findViewById(R.id.activity_details_TOOLBAR_favorite_star);
            favoriteStar.setOnClickListener( (v) -> detailViewModel.inverseFavorite());

            RecyclerView mDetailRV = (RecyclerView) this.findViewById(R.id.activity_details_RECYCLER_VIEW);
            RecyclerView.LayoutManager mDetailsRVLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            mDetailRV.setLayoutManager(mDetailsRVLayoutManager);
            mDetailRVAdapter = new DetailRVAdapter(this);
            mDetailRV.setAdapter(mDetailRVAdapter);
            mDetailRV.setHasFixedSize(false);


            detailViewModel.getApiKeyLive() .observe(this,
                    new Observer< String>() { @Override public void onChanged( String apiKey) {
                        //detailViewModel .getApiKeyLive() .removeObserver( this);
                        detailViewModel.integrate(apiKey);
                    }});

            detailViewModel .getMainLive() .observe(this,
                    new Observer<Main>() { @Override public void onChanged( Main main) {
                        //detailViewModel .getMainLive() .removeObserver( this);
                        populateToolbar( main);
                    }});

            detailViewModel .getListLive() .observe(this,
                    new Observer< List<Detail>>() { @Override public void onChanged( List<Detail> detailList) {
                       // detailViewModel .getListLive() .removeObserver( this);
                        /* if (detailList.size() > 0) */  mDetailRVAdapter .submitList( detailList);
                    }});

        } else Log .e(this.getClass().getSimpleName(), "ERROR, invalid movieID");
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////

    @Override public void onItemClickListener(String type, String url) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( (new URL(url).toString())));
            startActivity(browserIntent);
        } catch (MalformedURLException e) { e.printStackTrace(); }
    }


    private void populateToolbar(Main main) {
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.activity_detail_TOOLBAR);

            setSupportActionBar(toolbar);
            toolbar.setFitsSystemWindows(true);

            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            toolbar.setNavigationIcon(R.drawable.arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            });
        } catch (NullPointerException e) { e.printStackTrace(); }

        ((TextView) findViewById( R.id.activity_detail_TOOLBAR_movieID_tv))            .setText(  valueOf( main .getMovieID()));
        ((TextView) findViewById( R.id.activity_detail_TOOLBAR_popularity_value_tv))   .setText(  valueOf( main .getPopularVAL()));
        ((TextView) findViewById( R.id.activity_detail_TOOLBAR_vote_average_value_tv)) .setText(  valueOf( main .getVoteAVG()));
        ((TextView) findViewById( R.id.activity_detail_TOOLBAR_vote_count_value_tv))   .setText(  valueOf( main .getVoteCNT()));

        ImageView imageView = ((ImageView) findViewById( R.id.activity_detail_TOOLBAR_iv));
        imageView.getViewTreeObserver() .addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                Picasso .get()
                        .load( main.getPosterURL())
                        .placeholder( R.drawable.placeholder)
                        .error( R.drawable.error)
                        //.resize(screenWidth, imageHeight)
                        .fit()
                        .centerInside()
                        .into( imageView);
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        ((ImageView) findViewById( R.id.activity_details_TOOLBAR_favorite_star)) .setColorFilter( this.getApplicationContext() .getColor(
                (main.isFavorite())   ?   R.color.colorYellow   :  R.color.colorWhite               ));
    }
}
