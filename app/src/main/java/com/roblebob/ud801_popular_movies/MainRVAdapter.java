package com.roblebob.ud801_popular_movies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private int whatPage(final int position) { return (int) (Math.floor( position / 20) + 1); }


    MainRVAdapter(Context context, ItemClickListener itemClickListener) {
        mAppDatabase = AppDatabase .getInstance( mContext);
        mContext = context;
        this.itemClickListener = itemClickListener;
        mMovieList = new ArrayList<>();
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
        return new MainRVViewHolder( view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRVViewHolder holder, int position) {
        final String posterKEY = mMovieList.get( position) .getPosterID();
        holder .textView.setText(String.valueOf( position + 1));

        if (mMovieList.get( position) .getTitle() == null) {
            NetworkUtils.integrateDetails(mAppDatabase, mMovieList.get(position).getMovieID());
            // NetworkUtils.integrateExtras(mAppDatabase, mMovieList.get( position) .getMovieID());
        }

        Log.e(TAG + "::onBindViewHolder() ", "----[POS:" + position + "]---[PAGE:" + whatPage(position) + "]---[SIZE:" + mMovieList.size() + "]--->  " + posterKEY);
        Picasso .get() .load( "http://image.tmdb.org/t/p/w185/" + posterKEY) .into( holder.imageView);
    }

    @Override public int getItemCount() { return  (mMovieList != null)  ?  mMovieList .size()  :  0; }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public interface ItemClickListener { void onItemClickListener(int id); }

    public class MainRVViewHolder extends RecyclerView .ViewHolder implements View .OnClickListener {

        ImageView imageView;
        TextView textView;

        public MainRVViewHolder( @NonNull View itemView) {
            super( itemView);
            imageView = itemView .findViewById( R.id.main_recycler_view_single_item_IMAGE_VIEW);
            textView = itemView .findViewById( R.id.main_recycler_view_single_item_TEXT_VIEW);
            itemView .setOnClickListener( this);
        }

        @Override
        public void onClick( View v) {
            int pos = getAdapterPosition();
            int id = mMovieList .get( pos).getMovieID();
            Log .d( this .getClass() .getSimpleName(), "POS:" + pos + "  " + "ID:" + id);
            itemClickListener.onItemClickListener( id);
        }
    }
}

