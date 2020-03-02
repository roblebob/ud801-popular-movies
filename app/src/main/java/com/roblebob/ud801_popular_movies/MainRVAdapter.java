package com.roblebob.ud801_popular_movies;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainRVAdapter extends RecyclerView.Adapter< MainRVAdapter.MainRVViewHolder> {
    private final static String TAG =   MainRVAdapter.class .getSimpleName();
    private final AsyncListDiffer<Main> mDiffer = new AsyncListDiffer(this, DIFF_CALLBACK);
    private ItemClickListener mItemClickListener;
    private String order;

    public void submitOrder(@NonNull String order) {
        if (getItemCount() > 0) {
            this.order = order;
            List<Main> mainList = new ArrayList<Main>(mDiffer.getCurrentList());
            switch (order) {

                case "popular":
                    Collections.sort(mainList, ((Comparator<Main>) (Main m1, Main m2) ->
                            (Double.compare(m1.getFavorite(), m2.getFavorite()) != 0) ? Double.compare(m1.getFavorite(), m2.getFavorite()) :
                                    (Double.compare(m1.getPopularVAL(), m2.getPopularVAL()) != 0) ? Double.compare(m1.getPopularVAL(), m2.getPopularVAL()) :
                                            (Double.compare(m1.getVoteAVG(), m2.getVoteAVG()) != 0) ? Double.compare(m1.getVoteAVG(), m2.getVoteAVG()) :
                                                    Double.compare(m1.getVoteCNT(), m2.getVoteCNT())
                    ).reversed());
                    break;

                case "top_rated":
                    Collections.sort(mainList, ((Comparator<Main>) (Main m1, Main m2) ->
                            (Double.compare(m1.getFavorite(), m2.getFavorite()) != 0) ? Double.compare(m1.getFavorite(), m2.getFavorite()) :
                                    (Double.compare(m1.getVoteAVG(), m2.getVoteAVG()) != 0) ? Double.compare(m1.getVoteAVG(), m2.getVoteAVG()) :
                                            (Double.compare(m1.getPopularVAL(), m2.getPopularVAL()) != 0) ? Double.compare(m1.getPopularVAL(), m2.getPopularVAL()) :
                                                    Double.compare(m1.getVoteCNT(), m2.getVoteCNT())
                    ).reversed());
                    break;
            }
            submitList( mainList);
            Log.e(TAG + "::submitOrder()\t",  order +  "---> "  +   mainList .parallelStream() .map(  (movie) -> movie.getMovieID())  .collect(  Collectors.toList())  .toString());
        }
    }

    public void submitList( @NonNull  List< Main> mainList) { mDiffer.submitList( new ArrayList<>(mainList)); }

    public static final DiffUtil.ItemCallback< Main> DIFF_CALLBACK =  new DiffUtil.ItemCallback< Main>() {
        @Override public boolean areItemsTheSame    (@NonNull Main oldMain, @NonNull Main newMain) { return  oldMain.getMovieID()  ==  newMain.getMovieID(); }
        @Override public boolean areContentsTheSame (@NonNull Main oldMain, @NonNull Main newMain) { return  oldMain          .equals( newMain); }
    };

    MainRVAdapter(ItemClickListener itemClickListener) { mItemClickListener = itemClickListener; }

    @NonNull @Override public MainRVViewHolder  onCreateViewHolder( @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater .from( parent .getContext()) .inflate( R.layout.main_rv_single_item, parent, false);
        return new MainRVViewHolder( view);
    }

    @Override public void  onBindViewHolder( @NonNull MainRVViewHolder holder, int position) {
        Main main = mDiffer .getCurrentList() .get( position);
        holder.bindTo( main);
        Log.d(TAG + "::onBindViewHolder()\t", "\t---(POS:" + position + ")--->\t" + main.getMovieID());
    }

    @Override public int  getItemCount() { return  mDiffer.getCurrentList().size(); }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////
    public interface  ItemClickListener  { void onItemClickListener( int id); }
    //
    public class  MainRVViewHolder  extends RecyclerView .ViewHolder  implements View .OnClickListener  {

        ImageView posterIv;
        TextView  rankingTv;

        public MainRVViewHolder( @NonNull View itemView) {
            super( itemView);
            posterIv  = itemView .findViewById( R.id.main_rv_single_item_IV);
            rankingTv = itemView .findViewById( R.id.main_rv_single_item_TV);
            itemView .setOnClickListener( this);
        }

        @Override public void onClick( View v) {
            mItemClickListener .onItemClickListener(  mDiffer.getCurrentList().get( getAdapterPosition()).getMovieID());
        }

        public void bindTo( Main main) {

            Picasso .get() .load( main.getPosterURL()) .into(posterIv);

            rankingTv.setText( String .valueOf( getAdapterPosition() + 1));
            if (main.isDetailed())  rankingTv.setBackgroundColor( itemView.getContext().getColor( R.color.colorYellow));
            else                    rankingTv.setBackgroundColor( itemView.getContext().getColor( R.color.colorWhite));
        }
    }
}

