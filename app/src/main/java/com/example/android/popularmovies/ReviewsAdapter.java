package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<com.example.android.popularmovies.ReviewsAdapter.ReviewsAdapterViewHolder> {

        public ReviewsAdapter(ArrayList<Reviews> reviews, Context context ) {
            this.reviews = reviews;
            this.context = context;
        }

        ArrayList<Reviews> reviews = new ArrayList<>();

        Context context;

        public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder {

            TextView reviewsContentTextView;
            TextView reviewsAuthorTextView;

            public ReviewsAdapterViewHolder(View view) {
                super(view);
                reviewsContentTextView = (TextView) view.findViewById(R.id.reviews_content);
                reviewsAuthorTextView = (TextView) view.findViewById(R.id.reviews_author);
            }
        }

        @Override
        public com.example.android.popularmovies.ReviewsAdapter.ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            Context context = viewGroup.getContext();
            int layoutIdForListItem = R.layout.reviews_list_item;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            return new com.example.android.popularmovies.ReviewsAdapter.ReviewsAdapterViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ReviewsAdapterViewHolder reviewsAdapterViewHolder, final int position) {
            Reviews review = reviews.get(position);
            reviewsAdapterViewHolder.reviewsContentTextView.setText(review.getMovieReviewsContent());
            reviewsAdapterViewHolder.reviewsAuthorTextView.setText(review.getMovieReviewsAuthor());
        }

        @Override
        public int getItemCount() {
            if (null == reviews) return 0;
            return reviews.size();
        }

        public void addAll(ArrayList<Reviews> reviews) {
            this.reviews = reviews;
            notifyDataSetChanged();
        }
    }

