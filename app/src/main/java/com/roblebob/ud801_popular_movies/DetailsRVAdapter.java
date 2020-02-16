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

public class DetailsRVAdapter extends RecyclerView .Adapter<DetailsRVAdapter.DetailsRVViewholder> {
    private static final String TAG = DetailsRVAdapter.class.getSimpleName();
    private static final String ARROW_right = Html.fromHtml("&blacktriangleright;").toString();

    private String mHomepageUrl;
    private String mImdbUrl;
    private List<Xtra> linksXtraList;
    private DetailsRVAdapter.ItemClickListener itemClickListener;

    DetailsRVAdapter(DetailsRVAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void setLinksXtraList( List<Xtra>  linksXtraList) {
        this.linksXtraList = linksXtraList;
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

        Xtra xtra = this.linksXtraList.get(position);
        int subID = xtra.getExtraID() % 100;
        String value = xtra.getValue();

        if /* homepage */ (subID == 18) {

            holder .cardviewTextView    .setText( "home");
            holder .nameTextView        .setText( "Official Homepage");
            holder .urlString           = value;

        } else if ( /* imdbpage */ (subID == 19)) {
            holder .cardviewTextView    .setText( "imdb");
            holder .nameTextView        .setText( "IMDB's page");
            holder .urlString =  value;

        } else if ( /* trailer */ 20 <= subID && subID < 40) {
            String[] S = value.split(",", 2);
            holder.cardviewTextView.setText("trailer");
            holder.nameTextView.setText( S[1]);
            holder.urlString = S[0];
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    //----------------------------------------------------------------------------------------------

    @Override public int getItemCount() {
        return (this.linksXtraList != null)  ?  this.linksXtraList.size()   : 0; }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public interface ItemClickListener { void onItemClickListener( String type, String url); }

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

            Log .e(TAG, "---------_>  "  + urlString);
            itemClickListener.onItemClickListener(cardviewTextView.getText().toString(), urlString);
        }
    }
}
