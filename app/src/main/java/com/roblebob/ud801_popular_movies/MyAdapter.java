package com.roblebob.ud801_popular_movies;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter< MyAdapter.MyViewHolder> {



    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder( @NonNull View itemView) {
            super( itemView);

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
