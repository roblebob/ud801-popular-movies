package com.roblebob.ud801_popular_movies;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DetailsRVAdapter extends RecyclerView .Adapter<DetailsRVAdapter.DetailsRVViewholder> {

    private static final String ARROW_right = Html.fromHtml("&blacktriangleright;").toString();

    private List< String> mTrailerUrlStringList;
    private List< String> mTrailerNameStringList;
    private List< String> mReviewAuthorStringList;
    private List< String> mReviewContentStringList;
    public void setTrailerUrlStringList(    List< String> trailerUrlStringList)     { mTrailerUrlStringList     = trailerUrlStringList;     notifyDataSetChanged(); }
    public void setTrailerNameStringList(   List< String> trailerNameStringList)    { mTrailerNameStringList    = trailerNameStringList;    notifyDataSetChanged(); }
    public void setReviewAuthorStringList(  List< String> reviewAuthorStringList)   { mReviewAuthorStringList   = reviewAuthorStringList;   notifyDataSetChanged(); }
    public void setReviewContentStringList( List< String> reviewContentStringList)  { mReviewContentStringList  = reviewContentStringList;  notifyDataSetChanged(); }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @NonNull
    @Override
    public DetailsRVViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }





    private boolean isMovieTrailerPosition( int position) {
        return ( 0 <= position  &&  position < mTrailerUrlStringList.size()  &&
                mTrailerUrlStringList.size() == mTrailerNameStringList.size()); }
    private boolean isMovieReviewPosition( int position) {
        return ( mTrailerUrlStringList.size() <= position  &&  position < getItemCount()  &&
                mTrailerUrlStringList.size() == mTrailerNameStringList.size()); }

    @Override
    public void onBindViewHolder(@NonNull DetailsRVViewholder holder, int position) {


        if (isMovieTrailerPosition( position) ) { final int i = position;

            holder .textView .setText( mTrailerNameStringList .get( i));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start Youtube Trailer

                }
            });

        } else if (isMovieReviewPosition( position)) { final int i = position - getMovieTrailerItemCount();


        }
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    private int getMovieTrailerItemCount() {
        if ( mTrailerUrlStringList != null && mTrailerNameStringList != null)
            if ( mTrailerUrlStringList.size() == mTrailerNameStringList.size())
                return mTrailerUrlStringList.size();
        return 0;
    }
    private int getMovieReviewItemCount() {
        if ( mReviewContentStringList != null && mReviewAuthorStringList != null)
            if ( mReviewContentStringList.size() == mReviewAuthorStringList.size())
                return mReviewContentStringList.size();
        return 0;
    }

    @Override public int getItemCount() { return getMovieTrailerItemCount() + getMovieReviewItemCount(); }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    class DetailsRVViewholder extends RecyclerView.ViewHolder {

        TextView textView;

        public DetailsRVViewholder(View itemView) {
            super( itemView);
            textView = ( TextView) itemView.findViewById( R.id.details_trailers_recycler_view_single_item_NAME_text_view);
        }
    }
}