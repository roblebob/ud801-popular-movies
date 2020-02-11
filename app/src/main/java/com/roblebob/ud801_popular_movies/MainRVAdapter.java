package com.roblebob.ud801_popular_movies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainRVAdapter extends RecyclerView.Adapter< MainRVAdapter.MainRVViewHolder> {

    private final static String TAG =   MainRVAdapter.class .getSimpleName();
    private final AsyncListDiffer< Movie> mDiffer = new AsyncListDiffer(this, DIFF_CALLBACK);
    private ItemClickListener mItemClickListener;
    private MainViewModel mainViewModel;




    @Override public int getItemCount() { return  mDiffer.getCurrentList().size(); }
    public void submitList(List< Movie> movieList) { mDiffer.submitList( movieList); }

    @Override public void onBindViewHolder(@NonNull MainRVViewHolder holder, int position) {
        Movie movie = mDiffer .getCurrentList() .get( position);
        holder.bindTo( movie);
        holder.rankingTv.setText( String .valueOf( position + 1));
    }

    public static final DiffUtil.ItemCallback< Movie> DIFF_CALLBACK =  new DiffUtil.ItemCallback< Movie>() {
        @Override public boolean areItemsTheSame(@NonNull Movie oldMovie, @NonNull Movie newMovie) {
            return oldMovie.getMovieID() == newMovie.getMovieID();
        }
        @Override public boolean areContentsTheSame(@NonNull Movie oldMovie, @NonNull Movie newMovie) {
            return oldMovie.equals( newMovie);
        }
    };
    ////////////////////////////////////////////////////////////////////////////////////////////////

    MainRVAdapter( ItemClickListener itemClickListener) {

        mItemClickListener =  itemClickListener;
    }

    @NonNull @Override public MainRVViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater .from( parent .getContext()) .inflate( R.layout.main_recycler_view_single_item, parent, false);
        return new MainRVViewHolder( view);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public interface ItemClickListener { void onItemClickListener( int id); }

    public class MainRVViewHolder extends RecyclerView .ViewHolder implements View .OnClickListener {

        ImageView posterIv;
        TextView  rankingTv;

        public MainRVViewHolder( @NonNull View itemView) {
            super( itemView);
            posterIv  = itemView .findViewById( R.id.main_recycler_view_single_item_IMAGE_VIEW);
            rankingTv = itemView .findViewById( R.id.main_recycler_view_single_item_TEXT_VIEW);
            itemView .setOnClickListener( this);
        }

        @Override public void onClick( View v) {
            mItemClickListener .onItemClickListener(  mDiffer.getCurrentList().get( getAdapterPosition()).getMovieID());
        }

        public void bindTo(Movie movie) {

            final String posterID = movie .getPosterID();
            Picasso .get() .load( "http://image.tmdb.org/t/p/w185/" + posterID) .into(posterIv);

            if (!movie.isDetailed())  mainViewModel.integrateXtras( mDiffer.getCurrentList().get( getAdapterPosition()) .getMovieID());

            Log.d(TAG + "::onBindViewHolder() ", "----[POS:" + getAdapterPosition() + "]-----[SIZE:" + mDiffer.getCurrentList().size() + "]--->  " + posterID);

        }
    }
}

