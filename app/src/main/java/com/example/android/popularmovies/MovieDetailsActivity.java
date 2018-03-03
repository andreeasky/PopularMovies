package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie selectedMovie;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_movie_details );

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        Intent intent = getIntent();
        selectedMovie = intent.getParcelableExtra( "movie" );

        if (selectedMovie != null) {
            ImageView moviePoster = (ImageView) findViewById( R.id.movie_poster);
            Picasso.with(context).load(selectedMovie.getMoviePoster()).into(moviePoster);
            TextView movieTitle = (TextView) findViewById(R.id.movie_title);
            movieTitle.setText(selectedMovie.getMovieTitle());
            TextView releaseDateText = (TextView)findViewById(R.id.movie_release_date);
            releaseDateText.setText(R.string.release_date);
            TextView releaseDate = (TextView) findViewById(R.id.movie_release_date_content);
            releaseDate.setText(selectedMovie.getReleaseDate());
            TextView voteAverageText = (TextView) findViewById(R.id.movie_vote_average);
            voteAverageText.setText(R.string.vote_average);
            TextView voteAverage = (TextView) findViewById(R.id.movie_vote_average_content);
            voteAverage.setText(Double.toString(selectedMovie.getVoteAverage()));
            TextView plotSynopsis = (TextView) findViewById(R.id.movie_plot_synopsis);
            plotSynopsis.setText(selectedMovie.getPlotSynopsis());
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}







