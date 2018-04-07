package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnItemClicked {

    private ArrayList<Movie> moviesList = new ArrayList<>();
    private ArrayList<Reviews> reviewsList = new ArrayList<>();
    private ArrayList<Trailers> trailersList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private RecyclerView moviesRecyclerView;
    ArrayList<Movie> movies = new ArrayList<>();
    Context context;
    private String sortOrder = "popular";
    static final String SORT_ORDER_MOVIE = "sort_order_movie";
    static final String RECYCLER_VIEW_LAYOUT = "recycler_view_layout";
    private Parcelable layoutManagerSavedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        movieAdapter = new MovieAdapter(movies, this);

        moviesRecyclerView.setAdapter(movieAdapter);

        // set a GridLayoutManager with default vertical orientation and 2 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        moviesRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        movieAdapter.setOnClick(this); // Bind the listener

        if (savedInstanceState != null) {
            sortOrder = savedInstanceState.getString(SORT_ORDER_MOVIE);
        }
        new MoviesAsyncTask().execute(sortOrder);

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(SORT_ORDER_MOVIE, sortOrder);
        savedInstanceState.putParcelable(RECYCLER_VIEW_LAYOUT, moviesRecyclerView.getLayoutManager().onSaveInstanceState());
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        layoutManagerSavedState = savedInstanceState.getParcelable(RECYCLER_VIEW_LAYOUT);
        moviesRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState);
    }

    public void setOnClick(List movies) {
        movieAdapter.setOnClick( (MovieAdapter.OnItemClicked) movies );
        restoreLayoutManagerPosition();
    }

    private void restoreLayoutManagerPosition() {
        if (layoutManagerSavedState != null) {
            moviesRecyclerView.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
        }
    }

    @Override
    public void onItemClick(int position) {
        // The onClick implementation of the RecyclerView item click
        Movie selectedMovie = moviesList.get(position);
        Intent intent = new Intent(this,MovieDetailsActivity.class);
        intent.putExtra("movie", selectedMovie);
        startActivity(intent);

    }

    private class MoviesAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            ArrayList<Movie> result = Utils.fetchMoviesData(urls[0]);

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {

            if (movies != null && !movies.isEmpty()) {
                moviesList = movies;
                movieAdapter.addAll(movies);
                restoreLayoutManagerPosition();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our visualizer_menu layout to this menu */
        inflater.inflate(R.menu.main_menu, menu);
        /* Return true so that the visualizer_menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        item.setChecked(!item.isChecked());
        CharSequence message;
        switch(item.getItemId()){
            case R.id.menu_sort_by_most_popular:
                message = "Most Popular selected";
                sortOrder = "popular";
                new MoviesAsyncTask().execute(sortOrder);
                break;
            case R.id.menu_sort_by_top_rated:
                message = "Top Rated selected";
                sortOrder = "top_rated";
                new MoviesAsyncTask().execute(sortOrder);
                break;
            case R.id.menu_sort_by_favorites:
                message = "Favorites selected";
                sortOrder = "favorites";
                new MoviesAsyncTask().execute(sortOrder);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
        return true;
    }
}


