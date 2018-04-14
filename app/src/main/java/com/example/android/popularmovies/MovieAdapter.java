package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private final Context movieContext;

    final private MovieAdapter.MovieAdapterOnClickHandler movieClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface  MovieAdapterOnClickHandler {
        void onClick(String movieDetails);
    }

    private Cursor movieCursor;

    public MovieAdapter(@NonNull Context context, MovieAdapter.MovieAdapterOnClickHandler clickHandler) {
        movieContext = context;
        movieClickHandler = clickHandler;
    }


    @Override
    public MovieAdapter.MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater
                .from(movieContext)
                .inflate(R.layout.favorites_list_item, viewGroup, false);

        view.setFocusable(true);

        return new MovieAdapter.MovieAdapterViewHolder(view);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

        MovieAdapterViewHolder (View view) {
            super(view);
            ImageView favoriteMovieImage = (ImageView)view.findViewById( R.id.favorite_movie_image );

            TextView favoriteMovieTitle = (TextView)view.findViewById( R.id.favorite_movie_title );
        }

    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param movieAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */

    @Override
    public void onBindViewHolder (MovieAdapter.MovieAdapterViewHolder movieAdapterViewHolder, int position) {

//      Move the cursor to the appropriate position
        movieCursor.moveToPosition( position );

        int indexColumnMovieId = movieCursor.getColumnIndex( MoviesContract.MoviesEntry.COLUMN_MOVIE_ID );
        int indexColumnMovieImage = movieCursor.getColumnIndex( MoviesContract.MoviesEntry.COLUMN_MOVIE_IMAGE );
        int indexColumnMovieTitle = movieCursor.getColumnIndex( MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE );

        final String movieId = movieCursor.getString( indexColumnMovieId );
        final String movieImage = movieCursor.getString( indexColumnMovieImage );
        final String movieTitle = movieCursor.getString( indexColumnMovieTitle );

        String movieSummary = movieId + " - " + movieImage + " - " + movieTitle;

        // Display the summary created above
        movieAdapterViewHolder.moviesSummary.setText(movieSummary);
        movieAdapterViewHolder.favoriteMovieImage.setText(movieImage);
        movieAdapterViewHolder.favoriteMovieTitle.setText(movieTitle);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of movies available
     */

    @Override
    public int getItemCount() {
        // If mCursor is null, return 0. Otherwise, return the count of movieCursor
        if (null == movieCursor) return 0;
        return movieCursor.getCount();
    }

    //  Create a new method that allows you to swap Cursors.
    /**
     * Swaps the cursor used by the FavoriteMoviesAdapter for its movies data. This method is called by
     * MainActivity after a load has finished, as well as when the Loader responsible for loading
     * the movies data is reset. When this method is called, we assume we have a completely new
     * set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param newCursor the new cursor to use as FavoriteMoviesAdapter's data source
     */
    void swapCursor(Cursor newCursor) {
        movieCursor = newCursor;
        // After the new Cursor is set, call notifyDataSetChanged
        notifyDataSetChanged();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView moviesSummary;

        MovieAdapterViewHolder(View view) {
            super(view);

            moviesSummary = (TextView) view.findViewById(R.id.);

            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click. We fetch the movie that has been
         * selected, and then call the onClick handler registered with this adapter, passing that
         * movie.
         *
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {
            // Instead of passing the String from the data array, use the moviesSummary text!
            String movieDetails = moviesSummary.getText().toString();
            movieClickHandler.onClick(movieDetails);
        }
    }

    public MovieAdapter( ArrayList<Movie> movies, Context context ) {
        this.movies = movies;
        this.context = context;
    }

    private OnItemClicked onClick;

    public void setOnClick() {
    }

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