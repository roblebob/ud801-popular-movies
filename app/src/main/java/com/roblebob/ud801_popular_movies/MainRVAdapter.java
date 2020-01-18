package com.roblebob.ud801_popular_movies;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainRVAdapter extends RecyclerView.Adapter<MainRVAdapter.MainRVViewHolder> {




    ////////////////////////////////////////////////////////////////////////////////////////////////
    @NonNull
    @Override
    public MainRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MainRVViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    public class MainRVViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MainRVViewHolder(@NonNull View itemView) {
            super( itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
