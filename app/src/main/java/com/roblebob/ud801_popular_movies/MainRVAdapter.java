package com.roblebob.ud801_popular_movies;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainRVAdapter extends RecyclerView.Adapter<MainRVAdapter.MainRVViewHolder> {
    private final static String TAG = MainRVAdapter.class.getSimpleName();
    private Context mContext;
    private ItemClickListener itemClickListener;
    private AppDatabase mAppDatabase;
    private int _POS = 0;
    private int whatPage(final int position) { return (int) (Math.floor( position / 20) + 1); }


    MainRVAdapter(Context context, ItemClickListener itemClickListener) {
        mAppDatabase = AppDatabase .getInstance( mContext);
        mContext = context;
        this.itemClickListener = itemClickListener;
        mMovieList = new ArrayList<>();
        NetworkUtils. integratePageOfMovies( mAppDatabase, "popular", 1);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    private List< Movie> mMovieList;

    public void setMovieList( List< Movie> movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @NonNull
    @Override
    public MainRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from( parent .getContext())
                .inflate( R.layout.main_recycler_view_single_item, parent, false);
        MainRVViewHolder mainRVViewHolder = new MainRVViewHolder( view);

        return mainRVViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainRVViewHolder holder, int position) {
        _POS = position;
        final String path = NetworkUtils.buildUrlForMoviePosterImage( mMovieList.get( position) .getPosterPath() , "w185").toString();
        Log.e(TAG, "----[POS:" + position + "]---[PAGE:" + whatPage(position) + "]---[SIZE:" + mMovieList.size() + "]--->  " + path);
        Picasso .get().load( path).into( holder.imageView);



    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mMovieList != null)
            count = mMovieList .size();
        return count;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    public interface ItemClickListener { void onItemClickListener(int id); }

    public class MainRVViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;

        public MainRVViewHolder(@NonNull View itemView) {
            super( itemView);
            imageView = itemView.findViewById(R.id.main_recycler_view_single_item_IMAGE_VIEW);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            int id = mMovieList .get(pos). getId();
            Log.e(this.getClass().getSimpleName(), "POS:" + pos + "  " + "ID:" + id);
            itemClickListener.onItemClickListener(id);
        }
    }
}

