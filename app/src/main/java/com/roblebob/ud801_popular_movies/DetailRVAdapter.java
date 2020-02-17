package com.roblebob.ud801_popular_movies;

import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DetailRVAdapter extends RecyclerView .Adapter<DetailRVAdapter.DetailsRVViewholder> {
    private static final String TAG = DetailRVAdapter.class.getSimpleName();
    private static final String ARROW_right = Html.fromHtml("&blacktriangleright;").toString();

    private String mHomepageUrl;
    private String mImdbUrl;
    private List<Detail> linksDetailList;
    private DetailRVAdapter.ItemClickListener itemClickListener;

    DetailRVAdapter(DetailRVAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void setLinksDetailList(List<Detail> linksDetailList) {
        this.linksDetailList = linksDetailList;
        notifyDataSetChanged();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    @NonNull
    @Override
    public DetailsRVViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from( parent .getContext())
                .inflate( R.layout.detail_rv_single_item, parent, false);
        DetailRVAdapter.DetailsRVViewholder detailsRVViewHolder = new DetailRVAdapter.DetailsRVViewholder( view);

        return detailsRVViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull DetailsRVViewholder holder, int position) {

        Detail detail = this.linksDetailList.get(position);
        int subID = detail.getOrder();
        String value = detail.getContent();

        if /* homepage */ (subID == 18) {

            holder .tagTv.setText( "home");
            holder .nameTv.setText( "Official Homepage");
            holder .urlString           = value;

        } else if ( /* imdbpage */ (subID == 19)) {
            holder .tagTv.setText( "imdb");
            holder .nameTv.setText( "IMDB's page");
            holder .urlString =  value;

        } else if ( /* trailer */ 20 <= subID && subID < 40) {
            String[] S = value.split(",", 2);
            holder.tagTv.setText("trailer");
            holder.nameTv.setText( S[1]);
            holder.urlString = S[0];
        }

    }


    //----------------------------------------------------------------------------------------------

    @Override public int getItemCount() {
        return (this.linksDetailList != null)  ?  this.linksDetailList.size()   : 0; }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///

    public interface ItemClickListener { void onItemClickListener( String type, String url); }

    class DetailsRVViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tagTv;
        TextView triangleTv;
        TextView nameTv;
        String urlString;

        public DetailsRVViewholder(View itemView) {
            super( itemView);
            tagTv = ( TextView) itemView .findViewById( R.id.detail_rv_single_item_tag_cv_tv);
            triangleTv = ( TextView) itemView .findViewById( R.id.detail_rv_single_item_triangle_tv);
            nameTv = ( TextView) itemView .findViewById( R.id.detail_rv_single_item_name_tv);
            nameTv.setHorizontallyScrolling(true);
            nameTv.setMovementMethod(new ScrollingMovementMethod());
            itemView .setOnClickListener( this);
        }

        @Override
        public void onClick(View v) {

            Log .e(TAG, "---------_>  "  + urlString);
            itemClickListener.onItemClickListener(tagTv.getText().toString(), urlString);
        }
    }
}
