package com.example.android.popularmovies;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by User on 11.03.2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<com.example.android.popularmovies.TrailersAdapter.TrailersAdapterViewHolder> {

    ArrayList<Trailers> trailers = new ArrayList<>();

    Context context;

    public TrailersAdapter(ArrayList<Trailers> trailers, Context context ) {
            this.trailers = trailers;
            this.context = context;
        }

        private TrailersAdapter.OnTrailerClicked onClick;

        public void setOnClick() {
        }

        public interface OnTrailerClicked {

            void onTrailerClick(Trailers trailer);
        }

        public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder {

            ImageView movieTrailersImage;
            TextView trailersNameTextView;
            Button movieTrailers;

            public TrailersAdapterViewHolder(View view) {
                super(view);
                movieTrailersImage = (ImageView) view.findViewById(R.id.trailers_image);
                trailersNameTextView = (TextView) view.findViewById(R.id.trailers_name);
                movieTrailers = (Button)view.findViewById(R.id.trailers_button);

            }
        }

        @Override
        public com.example.android.popularmovies.TrailersAdapter.TrailersAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            Context context = viewGroup.getContext();
            int layoutIdForListItem = R.layout.trailers_list_item;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            return new com.example.android.popularmovies.TrailersAdapter.TrailersAdapterViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TrailersAdapterViewHolder trailersAdapterViewHolder, final int position) {
            final Trailers trailer = trailers.get(position);
            String videoID = trailer.getMovieTrailersKey();
            String url = "http://img.youtube.com/vi/" + videoID + "/0.jpg";
            Picasso.with( context ).load(url).into( trailersAdapterViewHolder.movieTrailersImage );
            trailersAdapterViewHolder.movieTrailers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onTrailerClick(trailer);
                }
            });
            trailersAdapterViewHolder.trailersNameTextView.setText(trailer.getMovieTrailersName());

        }

        @Override
        public int getItemCount() {
            if (null == trailers) return 0;
            return trailers.size();
        }

        public void addAll(ArrayList<Trailers> trailers) {
            this.trailers = trailers;
            notifyDataSetChanged();
        }

        public void setOnClick(TrailersAdapter.OnTrailerClicked onClick)
        {
            this.onClick=onClick;
        }
    }


