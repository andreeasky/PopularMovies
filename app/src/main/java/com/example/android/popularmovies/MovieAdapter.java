package com.example.android.popularmovies;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    public MovieAdapter( ArrayList<Movie> movies, Context context ) {
        this.movies = movies;
        this.context = context;
    }

    private OnItemClicked onClick;

    public interface OnItemClicked {

        void onItemClick(int position);
    }

    ArrayList<Movie> movies = new ArrayList<>();

    Context context;

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView movieImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            movieImageView = (ImageView) view.findViewById(R.id.movie_image);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, final int position) {
        Movie movie = movies.get(position);
        Picasso.with(context).load(movie.getMoviePoster()).into(movieAdapterViewHolder.movieImageView);
        movieAdapterViewHolder.movieImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == movies) return 0;
        return movies.size();
    }

    public void addAll(ArrayList<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}