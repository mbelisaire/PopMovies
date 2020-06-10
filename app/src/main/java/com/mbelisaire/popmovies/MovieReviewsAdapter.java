package com.mbelisaire.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewsHolder> {

    private Context ctx;
    private JSONArray movieReviews;

    public MovieReviewsAdapter(Context ctx, JSONArray movieReviews) {
        this.ctx = ctx;
        this.movieReviews = movieReviews;
    }

    @Override
    public MovieReviewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater movieInflater = LayoutInflater.from(ctx);
        View movieReviewView = movieInflater.inflate(R.layout.movie_review_row, parent, false);
        return new MovieReviewsAdapter.MovieReviewsHolder(movieReviewView);
    }

    @Override
    public void onBindViewHolder(MovieReviewsAdapter.MovieReviewsHolder holder, int position) {
        JSONObject movieReviewJson = movieReviews.optJSONObject(position);
        holder.textViewAuthor.setText(movieReviewJson.optString(Constants.JSON_MOVIE_REVIEW_AUTHOR));
        holder.textViewReviewContent.setText(movieReviewJson.optString(Constants.JSON_MOVIE_REVIEW_CONTENT));
        if(position == movieReviews.length()-1){
            holder.dividerView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return movieReviews.length();
    }

    public class MovieReviewsHolder extends RecyclerView.ViewHolder {
        TextView textViewAuthor, textViewReviewContent;
        View dividerView;
        public MovieReviewsHolder(View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.movieReviewAuthor);
            textViewReviewContent = itemView.findViewById(R.id.movieReviewContent);
            dividerView = itemView.findViewById(R.id.reviewDivider);
        }
    }
}
