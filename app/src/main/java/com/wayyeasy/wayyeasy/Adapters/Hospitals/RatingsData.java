package com.wayyeasy.wayyeasy.Adapters.Hospitals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wayyeasy.wayyeasy.Models.Hospitals.hospitals_ratings_response_model;
import com.wayyeasy.wayyeasy.R;

import java.util.List;

public class RatingsData extends RecyclerView.Adapter<RatingsData.holder> {

    private static final String TAG = "Ratings Data";
    List<hospitals_ratings_response_model> data;
    Context context;

    public RatingsData(List<hospitals_ratings_response_model> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_hospital_review_details, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.ratingBar.setRating(data.get(position).getAverage());
        holder.name.setText(data.get(position).getUserName());
        holder.review.setText(data.get(position).getReview());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class holder extends RecyclerView.ViewHolder {

        TextView name, review;
        RatingBar ratingBar;
        public holder(@NonNull View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.user_rating);
            name = itemView.findViewById(R.id.user_name);
            review = itemView.findViewById(R.id.user_review);
        }
    }
}
