package com.roblebob.ud801_popular_movies;

import android.text.Html;
import android.text.method.ScrollingMovementMethod;
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
    private List< MovieExtra> mMovieExtraList;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void setHomepageUrl( String homeUrl) { mHomepageUrl = homeUrl; notifyDataSetChanged(); }
    public void setImdbUrl( String imdbUrl)     { mImdbUrl = imdbUrl; notifyDataSetChanged(); }

    public void setExtras( List< MovieExtra> extraList) {
        mMovieExtraList = extraList;
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

        } else if ( isExtra( position)) {
            int index = position - getHomePageItemCount() - getImdbUrlItemCount();
            holder.cardviewTextView.setText(mMovieExtraList.get( index).getType());
            holder.nameTextView.setText( mMovieExtraList.get( index).getName());
            holder.itemView.setOnClickListener((View v) -> { /* start trailer on youtube */ });
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean isHomePage( int position)   {
        return      position >= 0   &&
                    position < getHomePageItemCount(); }
    private boolean isImdbPage(int position)  {
        return      position >= getHomePageItemCount()   &&
                    position < getHomePageItemCount() + getImdbUrlItemCount(); }
    private boolean isExtra(int position) {
        return      position >= getHomePageItemCount() + getImdbUrlItemCount()   &&
                    position < getItemCount(); }

    //----------------------------------------------------------------------------------------------

    private int getHomePageItemCount()  { return ( mHomepageUrl != null) ? 1 : 0; }
    private int getImdbUrlItemCount()   { return ( mImdbUrl     != null) ? 1 : 0; }
    //----------------------------------------------------------------------------------------------

    @Override public int getItemCount() {
        return getHomePageItemCount() + getImdbUrlItemCount() + mMovieExtraList.size(); }

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
            nameTextView.setHorizontallyScrolling(true);
            nameTextView.setMovementMethod(new ScrollingMovementMethod());
            itemView .setOnClickListener( this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
