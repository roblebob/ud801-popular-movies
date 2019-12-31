package com.roblebob.ud801_popular_movies;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter< MyAdapter.MyViewHolder> {

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder( @NonNull View itemView) {
            super( itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }


    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder( @NonNull MyAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
