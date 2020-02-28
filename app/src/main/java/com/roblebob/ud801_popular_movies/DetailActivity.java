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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class DetailActivity extends AppCompatActivity  implements DetailRVAdapter.ItemClickListener  {
    private static final String TAG = DetailActivity.class.getSimpleName();
    // Extra for the task ID to be received in the intent
    public static final String INTENT_EXTRA_movieID = "INTENT_EXTRA_movieID";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_movieID = "INSTANCE_ID";

    private int movieID;

    private AppDatabase mAppDatabase;

    private RecyclerView mDetailRV;
    private DetailRVAdapter mDetailRVAdapter;
    private RecyclerView.LayoutManager mDetailsRVLayoutManager;
    private Button favoriteButton;
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_detail);

        Intent intent = getIntent();
        if ( intent != null && intent.hasExtra(INTENT_EXTRA_movieID))
            movieID = intent .getIntExtra(INTENT_EXTRA_movieID, -1);
        // if (savedInstanceState != null && savedInstanceState.containsKey( INSTANCE_ID))  ID = savedInstanceState .getInt( INSTANCE_ID, -1);
        if (movieID > 0) {

            DetailViewModelFactory detailViewModelFactory = new DetailViewModelFactory(this.getApplication(), movieID);
            final DetailViewModel detailViewModel = ViewModelProviders.of(this, detailViewModelFactory) .get( DetailViewModel.class);



            /* *************************************************************************************
             *
             */
            mDetailRV = (RecyclerView) this.findViewById( R.id.activity_details_RECYCLER_VIEW);
            mDetailsRVLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            mDetailRV.setLayoutManager( mDetailsRVLayoutManager);
            mDetailRVAdapter = new DetailRVAdapter(this);
            mDetailRV.setAdapter(mDetailRVAdapter);
            mDetailRV.setHasFixedSize(false);




            /* *************************************************************************************
             *
             */
            favoriteButton = (Button) findViewById( R.id.activity_details_BUTTON_favorite);
            favoriteButton.setOnClickListener(v -> detailViewModel .getMainLive() .observe(this,
                    (movie) -> { AppExecutors.getInstance().diskIO().execute( () -> mAppDatabase .mainDao() .inverseFavorite(movieID));
                                    Log.e(TAG, "CLICKED!!" + movie.isFavorite());           }));




            /* *************************************************************************************
             *
             */
            detailViewModel.getApiKeyLive() .observe(this,
                    new Observer< String>() { @Override public void onChanged(String apiKey) {
                        detailViewModel .getApiKeyLive() .removeObserver( this);
                        Log.d( TAG, ">+>+>+>+>+>>+>+>+>" + "Receiving database (api_key) update from LiveData  " + apiKey);
                        detailViewModel.integrate(apiKey);
                    }});


            detailViewModel .getMainLive() .observe(this,
                    new Observer<Main>() { @Override public void onChanged(Main main) {
                        detailViewModel .getMainLive() .removeObserver( this);
                        includeParent( main);
                    }});

            detailViewModel .getListLive() .observe(this,
                    new Observer< List<Detail>>() { @Override public void onChanged(List<Detail> detailList) {
                        detailViewModel .getListLive() .removeObserver( this);
                        Log.d( TAG, ">+>+>+>+>+>>+>+>+>" + "Receiving database (detailList) update from LiveData  " + detailList.toString());
                        if (detailList.size() > 0)  mDetailRVAdapter .setDetailList( detailList);
                    }});


        } else Log .e(this.getClass().getSimpleName(), "ERROR, invalid movieID");
    }





    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////





    /* *********************************************************************************************
     *
     * @param main
     */
    private void includeParent(Main main) {
        if (main.getPosterKey() != null)
            Picasso .get() .load( main.getPosterURL())
                .into( (ImageView) findViewById( R.id.activity_detail_TOOLBAR_iv));

        ((TextView) findViewById( R.id.activity_detail_TOOLBAR_popularity_value_tv))   .setText( String.valueOf( main .getPopularVAL()));
        ((TextView) findViewById( R.id.activity_detail_TOOLBAR_vote_average_value_tv)) .setText( String.valueOf( main .getVoteAVG()));
        ((TextView) findViewById( R.id.activity_detail_TOOLBAR_vote_count_value_tv))   .setText( String.valueOf( main .getVoteCNT()));

        favoriteButton.setCompoundDrawableTintList( ColorStateList.valueOf( getResources().getColor(
                main.isFavorite()  ?  R.color.colorYellow  :  R.color.colorGray
        )));
    }







    /* *********************************************************************************************
     *
     */
    private void populateRVDetails(  List<Detail> detailList) {
        mDetailRVAdapter.setDetailList(detailList);
    }


    /* *********************************************************************************************
     *
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
