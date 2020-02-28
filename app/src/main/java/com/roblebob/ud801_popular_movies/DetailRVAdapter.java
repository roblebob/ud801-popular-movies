package com.roblebob.ud801_popular_movies;

import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DetailRVAdapter extends RecyclerView .Adapter<DetailRVAdapter.DetailsRVViewholder> {
    private static final String TAG = DetailRVAdapter.class.getSimpleName();
    private static final String ARROW_right = Html.fromHtml("&blacktriangleright;").toString();

    private String mHomepageUrl;
    private String mImdbUrl;
    private List<Detail> detailList;
    private DetailRVAdapter.ItemClickListener itemClickListener;

    DetailRVAdapter(DetailRVAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void setDetailList(List<Detail> detailList) {
        this.detailList = new ArrayList<>(detailList);
        Log.e(TAG, "--> " + detailList.toString());
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

        Detail detail = this.detailList.get( position);
        holder.bindTo( detail);
    }


    //----------------------------------------------------------------------------------------------

    @Override public int getItemCount() {
        return (this.detailList != null)  ?  this.detailList.size()   : 0; }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///

    public interface ItemClickListener { void onItemClickListener( String type, String url); }

    class DetailsRVViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView labelTv, contentTv;
        CardView labelCv, contentCv;
        ImageView labelIv;
        String urlString;

        public DetailsRVViewholder(View itemView) {
            super(itemView);

            labelCv = (CardView) itemView.findViewById(R.id.detail_rv_single_item_LABEL_cv);
            labelTv = (TextView) itemView.findViewById(R.id.detail_rv_single_item_LABEL_tv);
            labelIv = (ImageView) itemView.findViewById(R.id.detail_rv_single_item_LABEL_iv);

            contentCv = (CardView) itemView.findViewById(R.id.detail_rv_single_item_CONTENT_cv);
            contentTv = (TextView) itemView.findViewById(R.id.detail_rv_single_item_CONTENT_tv);


            //nameTv.setHorizontallyScrolling(true);
            //nameTv.setMovementMethod(new ScrollingMovementMethod());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClickListener(labelTv.getText().toString(), urlString);
        }


        /* *********************************************************************************************
         *
         */
        public void bindTo(Detail detail) {


            labelCv .setCardBackgroundColor(itemView.getResources().getColor(  R.color.colorBlack));
            labelTv .setTextColor(itemView.getResources().getColor(  R.color.colorWhite));
            labelTv .setTextSize(12);
            labelTv .setText(  detail.getOrder());
            if (detail.getLink() == null)  labelIv.setVisibility(View.GONE);
            else labelIv.setVisibility(View.VISIBLE);

            contentTv .setTextAlignment(View.  TEXT_ALIGNMENT_VIEW_END);
            final String emptySpacesIntro = "       ";


            switch (detail.getOrder()) {

                case "title":
                    contentTv.setTextSize(35);
                    contentCv.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorPrimary));
                    contentTv.setTypeface(contentTv.getTypeface(), Typeface.BOLD);
                    contentTv.setText( "   " + detail.getContent());
                    break;

                case "original_title":
                    contentTv.setTextSize(27);
                    contentCv.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorPrimary));
                    contentTv.setText( detail.getContent());
                    break;

                case "original_language":
                    contentTv.setTextSize(17);
                    contentCv.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorPrimary));
                    contentTv.setText("(" + detail.getContent() + ")");
                    break;

                case "release_date":
                    contentTv.setTextSize(24);
                    contentCv.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorPrimaryLight));
                    contentTv.setText(detail.getContent());
                    break;

                case "runtime":
                    contentTv.setTextSize(17);
                    contentCv.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorPrimaryLight));
                    contentTv.setText(detail.getContent() + " min.");
                    break;


                case "tagline":
                    contentTv.setTextSize(27);
                    contentCv.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorWhite));
                    contentTv.setText( "    " + detail.getContent());
                    break;

                case "overview":
                    contentTv.setTextSize(21);
                    contentCv.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorWhite));
                    contentTv.setText( "      " + detail.getContent());
                    break;


                case "genres":
                    contentTv.setTextSize(21);
                    contentCv.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorWhite));
                    contentTv.setText( detail.getContent());
                    break;


                case "budget":
                case "revenue":
                    contentTv.setTextSize(17);
                    contentCv.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorWhite));
                    contentTv.setText( prepareForMonetary(detail.getContent()));
                    contentTv.setTypeface(contentTv.getTypeface(), Typeface.BOLD);
                    break;


                case "homepage":
                    contentTv.setTextSize(17);
                    contentCv.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorWhite));
                    contentTv.setText( detail.getOrder());
                    urlString = detail.getContent();
                    break;

                case "imdbpage":
                    contentTv.setTextSize(17);
                    contentCv.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorWhite));
                    contentTv.setText("imdb page's version");
                    urlString = detail.getUrl();
                    break;


                case "videos":
                    contentTv .setTextSize(17);
                    contentCv .setCardBackgroundColor(itemView.getResources().getColor(  R.color.colorWhite));
                    contentTv .setText( detail.getContent());

                    break;


                case "reviews":
                    contentTv.setTextSize(17);
                    contentCv.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorWhite));
                    contentTv.setText( detail.getContent());
                    this.urlString = detail.getUrl();
                    break;

            }
        }


        public String prepareForMonetary(String s) {

            s = new StringBuilder(s).reverse().toString();
            char[] sArray = s.toCharArray();

            StringBuilder sBuilder = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                sBuilder.append( sArray[i]);
                if ((i+1) % 3 == 0 ) sBuilder.append( " ");
            }

            return sBuilder.reverse().toString()  + " $";
        }
    }
}
