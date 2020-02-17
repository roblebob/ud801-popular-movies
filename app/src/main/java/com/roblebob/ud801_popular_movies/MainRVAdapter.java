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
import java.util.Arrays;
import java.util.List;

public class MainRVAdapter extends RecyclerView.Adapter< MainRVAdapter.MainRVViewHolder> {
    public static final List< String> ORDEREDBY = new ArrayList< String>( Arrays.asList( "popular", "top_rated"));
    private final static String TAG =   MainRVAdapter.class .getSimpleName();
    private final AsyncListDiffer<Main> mDiffer = new AsyncListDiffer(this, DIFF_CALLBACK);
    private ItemClickListener mItemClickListener;

    public void submitList(List< Main> mainList) { mDiffer.submitList(mainList); }

    public static final DiffUtil.ItemCallback< Main> DIFF_CALLBACK =  new DiffUtil.ItemCallback< Main>() {
        @Override public boolean areItemsTheSame(@NonNull Main oldMain, @NonNull Main newMain) {
            return oldMain.getID() == newMain.getID();
        }
        @Override public boolean areContentsTheSame(@NonNull Main oldMain, @NonNull Main newMain) {
            return oldMain.equals(newMain);
        }
    };


    MainRVAdapter(ItemClickListener itemClickListener) { mItemClickListener = itemClickListener; }


    @NonNull @Override public MainRVViewHolder  onCreateViewHolder( @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater .from( parent .getContext()) .inflate( R.layout.main_rv_single_item, parent, false);
        return new MainRVViewHolder( view);
    }

    @Override public void  onBindViewHolder( @NonNull MainRVViewHolder holder, int position) {
        Main main = mDiffer .getCurrentList() .get( position);
        holder.bindTo( main);
        holder.rankingTv.setText( String .valueOf( position + 1));
        if (main.isDetailed())  holder.rankingTv.setBackgroundColor( holder.itemView.getContext().getColor( R.color.colorYellow));
        else                    holder.rankingTv.setBackgroundColor( holder.itemView.getContext().getColor( R.color.colorWhite));

        // TODO:
        // this.onAttachedToRecyclerView()
    }

    @Override public int  getItemCount() { return  mDiffer.getCurrentList().size(); }






    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////
    public interface  ItemClickListener  { void onItemClickListener( int id); }
    //
    public class  MainRVViewHolder  extends RecyclerView .ViewHolder  implements View .OnClickListener {

        ImageView posterIv;
        TextView  rankingTv;

        public MainRVViewHolder( @NonNull View itemView) {
            super( itemView);
            posterIv  = itemView .findViewById( R.id.main_rv_single_item_IV);
            rankingTv = itemView .findViewById( R.id.main_rv_single_item_TV);
            itemView .setOnClickListener( this);
        }

        @Override public void onClick( View v) {
            mItemClickListener .onItemClickListener(  mDiffer.getCurrentList().get( getAdapterPosition()).getID());
        }

        public void bindTo(Main main) {      Log.d(TAG + "::onBindViewHolder() ", "----[POS]---->  " + getAdapterPosition());

            final String posterID = main.getPosterKey();
            Picasso .get() .load( "http://image.tmdb.org/t/p/w185/" + posterID) .into(posterIv);

            //if (!main.isDetailed())  mainViewModel.integrateXtras( mDiffer.getCurrentList().get( getAdapterPosition()) .getParent());
        }
    }
}

