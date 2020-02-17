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

public class DetailActivity extends AppCompatActivity  implements DetailRVAdapter.ItemClickListener  {
    private static final String TAG = DetailActivity.class.getSimpleName();
    // Extra for the task ID to be received in the intent
    public static final String INTENT_EXTRA_movieID = "INTENT_EXTRA_movieID";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_movieID = "INSTANCE_ID";

    private int movieID;

    private AppDatabase mAppDatabase;

    private RecyclerView mDetailsRV;
    private DetailRVAdapter mDetailRVAdapter;
    private RecyclerView.LayoutManager mDetailsRVLayoutManager;
    private Button favoriteFLAGbutton;
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if ( intent != null && intent.hasExtra(INTENT_EXTRA_movieID))
            movieID = intent .getIntExtra(INTENT_EXTRA_movieID, -1);
        // if (savedInstanceState != null && savedInstanceState.containsKey( INSTANCE_ID))  ID = savedInstanceState .getInt( INSTANCE_ID, -1);
        if (movieID > 0) {


            DetailViewModelFactory detailsViewModelFactory = new DetailViewModelFactory(this.getApplication(), movieID);
            final DetailViewModel detailsViewModel = ViewModelProviders.of(this, detailsViewModelFactory).get(DetailViewModel.class);




            /* *************************************************************************************
             *
             */
            mDetailsRV = (RecyclerView) this.findViewById( R.id.activity_details_RECYCLER_VIEW);
            mDetailsRVLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            mDetailsRV .setLayoutManager( mDetailsRVLayoutManager);
            mDetailRVAdapter = new DetailRVAdapter(this);
            mDetailsRV .setAdapter(mDetailRVAdapter);
            mDetailsRV .setHasFixedSize(false);




            /* *************************************************************************************
             *
             */
            favoriteFLAGbutton = (Button) findViewById( R.id.activity_details_BUTTON_favorite);
            favoriteFLAGbutton.setOnClickListener(v -> detailsViewModel .getMainLive( movieID) .observe(this,
                    (movie) -> { AppExecutors.getInstance().diskIO().execute( () -> mAppDatabase .movieDao() .inverseFavorite(movieID));
                                    Log.e(TAG, "CLICKED!!" + movie.isFavorite());           }));




            /* *************************************************************************************
             *
             */
            detailsViewModel .getMainLive(  movieID) .observe(this,
                    new Observer<Main>() { @Override public void onChanged(Main main) {
                        detailsViewModel .getMainLive( movieID) .removeObserver( this);
                        includeParent(main);
                    }});

            detailsViewModel .getListLive(  movieID) .observe(this,
                    new Observer< List<Detail>>() { @Override public void onChanged(List<Detail> detailList) {
                        detailsViewModel .getListLive(movieID) .removeObserver( this);
                        Log.d( TAG, ">+>+>+>+>+>>+>+>+>" + "Receiving database (xtraNonlinks)update from LiveData  " + detailList.toString());
                        populateNonlinksFromXtraListIntoUI(detailList);
                    }});

        } else Log .e(this.getClass().getSimpleName(), "ERROR, invalid movieID= " + movieID);
    }





    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////





    /* *********************************************************************************************
     *
     * @param main
     */
    private void includeParent(Main main) {
        if (main.getPosterKey() != null)    Picasso .get() .load( main.getPosterURL()) .into( (ImageView) findViewById( R.id.imageView));

        favoriteFLAGbutton .setCompoundDrawableTintList( ColorStateList.valueOf( getResources().getColor(
                main.isFavorite()  ?  R.color.colorYellow  :  R.color.colorGray
        )));

        ((TextView) findViewById( R.id.activity_details_RATING_tv)) .setText(  String.valueOf(main.getVoteAVG()));
    }







    /* *********************************************************************************************
     *
     */
    private void populateNonlinksFromXtraListIntoUI(List<Detail> detailList) {                                  Log.d(TAG, "change has occured -->  populateNonlinksFromXtraListIntoUI()");
        detailList.forEach(  (Detail detail) -> {

            switch(Detail.ORDER.get(detail.getOrder())) {

                case "title":
                    ((TextView) findViewById(R.id.activity_details_TITLE_tv)).setText(detail.getOrder());
                    break;
                case "original_title":
                    ((TextView) findViewById(R.id.activity_details_ORIGINAL_TITLE)).setText(detail.getOrder());
                    ((TextView) findViewById(R.id.activity_details_ORIGINAL_TITLE)).setVisibility(View.VISIBLE);
                    break;
                case "original_language":
                    ((TextView) findViewById(R.id.activity_details_ORIGINAL_LANGUAGE_tv)) .setText( detail.getContent());
                    ((TextView) findViewById(R.id.activity_details_ORIGINAL_LANGUAGE_tv))         .setVisibility( View.VISIBLE);
                    ((TextView) findViewById(R.id.activity_details_ORIGINAL_TITLE_left_bracket))  .setVisibility( View.VISIBLE);
                    ((TextView) findViewById(R.id.activity_details_ORIGINAL_TITLE_right_bracket)) .setVisibility( View.VISIBLE);
                    break;
                case "release_date":
                    ((TextView) findViewById(R.id.activity_details_RELEASE_DATE_tv)).setText(detail.getContent().split("-")[0]);  // take only the year
                    ((TextView) findViewById(R.id.activity_details_RELEASE_DATE_tv)) .setVisibility( View.VISIBLE);
                    break;
                case "runtime":
                    ((TextView) findViewById( R.id.activity_details_RUNTIME_tv)).setText(detail.getContent());
                    ((TextView) findViewById( R.id.activity_details_RUNTIME_tv))              .setVisibility( View.VISIBLE);
                    ((TextView) findViewById( R.id.activity_details_RUNTIME_unit_minutes_tv)) .setVisibility( View.VISIBLE);
                    break;
                case "tagline":
                    ((TextView) findViewById( R.id.activity_details_TAGLINE_tv)) .setText( detail.getContent());
                    ((TextView) findViewById( R.id.activity_details_TAGLINE_tv)) .setVisibility(View.VISIBLE);
                    break;
                case "overview":
                    ((TextView) findViewById(R.id.activity_details_OVERVIEW_tv)).setText(detail.getContent());
                    ((TextView) findViewById(R.id.activity_details_OVERVIEW_tv)).setVisibility(View.VISIBLE);
                    break;
                case "genres":
                    ((TextView) findViewById(R.id.activity_details_GENRES_tv)).setText(detail.getContent());
                    ((TextView) findViewById(R.id.activity_details_GENRES_tv)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.activity_details_GENRES_LABEL_tv)).setVisibility(View.VISIBLE);
                    break;
                case "budget":
                    ((TextView) findViewById(R.id.activity_Details_BUDGET_tv)).setText(detail.getContent());
                    ((TextView) findViewById(R.id.activity_Details_BUDGET_tv)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.activity_details_BUDGET_LABEL_tv)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.activity_details_BUDGET_UNIT_dollarsign_tv)).setVisibility(View.VISIBLE);
                case "revenue":
                    ((TextView) findViewById(R.id.activity_Details_REVENUE_tv)).setText(detail.getContent());
                    ((TextView) findViewById(R.id.activity_Details_REVENUE_tv)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.activity_details_REVENUE_LABEL_tv)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.activity_details_REVENUE_UNIT_dollarsign_tv)).setVisibility(View.VISIBLE);
                    break;
            }
        });
    }




    /* *********************************************************************************************
     *
     */
    private void populateLinksXtraIntoUI(  List<Detail> detailList) {
        mDetailRVAdapter.setLinksDetailList(detailList);
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
