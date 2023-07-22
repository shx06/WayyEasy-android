package com.wayyeasy.wayyeasy.CustomDialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.SharedPreferenceManager;
import com.wayyeasy.wayyeasy.Models.Physicians.physicians_ratings_response_model;
import com.wayyeasy.wayyeasy.R;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingsDialog extends Dialog {

    private float userRate = 0;
    private String physicianId = null;
    physicians_ratings_response_model ratings;
    ProgressDialog progressDialog;
    ResponseDialog responseDialog;

    SharedPreferenceManager preferenceManager;
    Context context;

    public RatingsDialog(@NonNull Context context, String id) {
        super(context);
        physicianId = id;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(context);
        responseDialog = new ResponseDialog();

        ratings = new physicians_ratings_response_model();
        setContentView(R.layout.ratings_dialog);

        preferenceManager = new SharedPreferenceManager(context);

        final AppCompatButton submit = findViewById(R.id.submit_rating);
        final AppCompatButton cancel_rating = findViewById(R.id.close_rating);
        final AppCompatButton no_rating = findViewById(R.id.no_rating);
        final EditText review = findViewById(R.id.review_input);
        final RatingBar ratingBar = findViewById(R.id.rating_bar);
        final ImageView ratingState = findViewById(R.id.rating_state);

        no_rating.setOnClickListener(view -> {
            ratings.setStatus("failed");
            addRating(ratings);
        });

        submit.setOnClickListener(view -> {

            if (userRate < 1)
                userRate = 4;

            ratings.setStatus("success");
            ratings.setRating(userRate);
            ratings.setReview(review.getText().toString());
            ratings.setUsername(preferenceManager.getString(Constants.name));
            ratings.setUserImage(preferenceManager.getString(Constants.profileImage));
            ratings.setPhysicianId(physicianId);

            addRating(ratings);
        });

        cancel_rating.setOnClickListener(view -> dismiss());

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, b) -> {
            if (rating <= 1)
                ratingState.setImageResource(R.drawable.star_1);
            else if (rating <= 2)
                ratingState.setImageResource(R.drawable.star_2);
            else if (rating <= 3)
                ratingState.setImageResource(R.drawable.star_3);
            else if (rating <= 4)
                ratingState.setImageResource(R.drawable.star_4);
            else
                ratingState.setImageResource(R.drawable.star_5);

            animateImage(ratingState);

            userRate = rating;
        });
    }

    private void animateImage(ImageView imageView) {

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(500);
        imageView.setAnimation(scaleAnimation);
    }

    private void addRating(physicians_ratings_response_model ratings) {
        progressDialog.showDialog();
        HashMap<String, physicians_ratings_response_model> map = new HashMap<>();
        map.put(Constants.physiciansRatings, ratings);

        Call<Void> book = ApiControllers
                .getInstance()
                .getApi()
                .ratePhysician("Bearer " + preferenceManager.getString(Constants.KEY_MONGO_TOKEN), physicianId, map);

        book.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dismiss();
                progressDialog.dismissDialog();
                responseDialog.showDialog(context, getContext().getResources().getDrawable(R.drawable.success), "Thank You", getContext().getResources().getColor(R.color.green_20), getContext().getResources().getColor(R.color.green), "You have successfully rated to this doctor");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                dismiss();
                progressDialog.dismissDialog();
                responseDialog.showDialog(context, getContext().getResources().getDrawable(R.drawable.error), "Error", getContext().getResources().getColor(R.color.theme_color_orange_20), getContext().getResources().getColor(R.color.theme_color_orange), t.getMessage());
            }
        });
    }
}
