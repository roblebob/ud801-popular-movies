package com.roblebob.ud801_popular_movies;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailsRVAdapter extends RecyclerView .Adapter<DetailsRVAdapter.DetailsRVViewholder> {

    private static final String ARROW_right = Html.fromHtml("&blacktriangleright;").toString();

    private String mHomepageUrl;
    private String mImdbUrl;
    private List< String> mTrailerNameList;
    private List< String> mTrailerUrlList;
    private List< String> mReviewNameList;
    private List< String> mReviewUrlList;
    private List< String> mReviewContentList;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void setHomepageUrl( String homeUrl) { mHomepageUrl = homeUrl; notifyDataSetChanged(); }
    public void setImdbUrl( String imdbUrl)     { mImdbUrl = imdbUrl; notifyDataSetChanged(); }

    public void setExtras( List< MovieExtra> extraList) {
        mTrailerNameList = new ArrayList< String>();
        mTrailerUrlList  = new ArrayList< String>();
        mReviewNameList  = new ArrayList< String>();
        mReviewUrlList   = new ArrayList< String>();
        mReviewContentList = new ArrayList< String>();

        extraList.forEach( (MovieExtra extra) -> {
            if ( extra .getType() .equals("trailer")) {
                mTrailerNameList .add( extra .getName());
                mTrailerUrlList .add( extra .getUrl());
            } else if ( extra .getType() .equals("review")) {
                mReviewNameList .add( extra .getName());
                mReviewNameList .add( extra .getUrl());
            }
        });
        notifyDataSetChanged();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @NonNull
    @Override
    public DetailsRVViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from( parent .getContext())
                .inflate( R.layout.details_recycler_view_single_item, parent, false);
        DetailsRVAdapter.DetailsRVViewholder detailsRVViewHolder = new DetailsRVAdapter.DetailsRVViewholder( view);

        return detailsRVViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull DetailsRVViewholder holder, int position) {

        if ( isHomePage( position)) {

            holder .cardviewTextView    .setText( "home");
            holder .nameTextView        .setText( "Official Homepage");
            holder .urlString           = mHomepageUrl;

        } else if ( isImdbPage(position)) {
            holder .cardviewTextView    .setText( "imdb");
            holder .nameTextView        .setText( "IMDB's page");
            holder.itemView.setOnClickListener( (View v) -> { /* start IMDB's page within a webview/browser */ });

        } else if ( isTrailer( position)) {
            holder .cardviewTextView    .setText( "trailer");
            holder .nameTextView        .setText( mTrailerNameList.get( position - getHomePageItemCount() - getImdbUrlItemCount()));
            holder .itemView .setOnClickListener( ( View v) -> { /* start trailer on youtube */ });

        } else if ( isReview( position)) {
            ((TextView) holder .itemView. findViewById( R.id.details_recycler_view_single_item_CARDVIEW_textview)) .setText("review");

        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean isHomePage( int position)   {
        return      position >= 0   &&
                    position < getHomePageItemCount(); }
    private boolean isImdbPage(int position)  {
        return      position >= getHomePageItemCount()   &&
                    position < getHomePageItemCount() + getImdbUrlItemCount(); }
    private boolean isTrailer(int position) {
        return      position >= getHomePageItemCount() + getImdbUrlItemCount()   &&
                    position < getHomePageItemCount() + getImdbUrlItemCount() + getMovieTrailerItemCount(); }
    private boolean isReview( int position) {
        return      position >= getHomePageItemCount() + getImdbUrlItemCount() + getMovieTrailerItemCount()   &&
                    position < getHomePageItemCount() + getImdbUrlItemCount() + getMovieTrailerItemCount() + getMovieReviewItemCount(); }

    //----------------------------------------------------------------------------------------------

    private int getHomePageItemCount()      { return ( mHomepageUrl     != null) ? 1                        : 0; }
    private int getImdbUrlItemCount()       { return ( mImdbUrl         != null) ? 1                        : 0; }
    private int getMovieTrailerItemCount()  { return ( mTrailerNameList != null) ? mTrailerNameList.size()  : 0; }
    private int getMovieReviewItemCount()   { return ( mReviewNameList  != null) ? mReviewNameList.size()   : 0; }
    //----------------------------------------------------------------------------------------------

    @Override public int getItemCount() {
        return getHomePageItemCount() + getImdbUrlItemCount() + getMovieTrailerItemCount() + getMovieReviewItemCount(); }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public interface ItemClickListener { void onItemClickListener( int pos); }

    class DetailsRVViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView cardviewTextView;
        TextView triangleTextView;
        TextView nameTextView;
        String urlString;

        public DetailsRVViewholder(View itemView) {
            super( itemView);
            cardviewTextView    = ( TextView) itemView .findViewById( R.id.details_recycler_view_single_item_CARDVIEW_textview);
            triangleTextView    = ( TextView) itemView .findViewById( R.id.details_recycler_view_single_item_TRIANGLE_textview);
            nameTextView        = ( TextView) itemView .findViewById( R.id.details_recycler_view_single_item_NAME_textview);
            itemView .setOnClickListener( this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
