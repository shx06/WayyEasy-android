package com.wayyeasy.wayyeasy.Activities.Physicians;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wayyeasy.wayyeasy.Adapters.Physicians.BookedPhysiciansData;
import com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.SharedPreferenceManager;
import com.wayyeasy.wayyeasy.CustomDialogs.ProgressDialog;
import com.wayyeasy.wayyeasy.CustomDialogs.ResponseDialog;
import com.wayyeasy.wayyeasy.Models.Physicians.physician_response_model;
import com.wayyeasy.wayyeasy.Models.user.fetched_physician_list_response_mode;
import com.wayyeasy.wayyeasy.R;
import com.wayyeasy.wayyeasy.databinding.ActivityBookedPhysiciansBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookedPhysicians extends AppCompatActivity {

    ActivityBookedPhysiciansBinding bookedPhysiciansBinding;
    ProgressDialog progressDialog;
    SharedPreferenceManager preferenceManager;
    ResponseDialog dialog;
    BookedPhysiciansData bookedPhysiciansData;

    private String dataType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookedPhysiciansBinding = ActivityBookedPhysiciansBinding.inflate(getLayoutInflater());
        setContentView(bookedPhysiciansBinding.getRoot());

        preferenceManager = new SharedPreferenceManager(getApplicationContext());
        progressDialog = new ProgressDialog(BookedPhysicians.this);
        dialog = new ResponseDialog();

        bookedPhysiciansBinding.finishActivity.setOnClickListener(v -> {
            finish();
        });

        dataType = getIntent().getStringExtra(Constants.fetchPhysicianConsultationType);

        fetchPhysiciansPaidForConsult();
    }

    private void fetchPhysiciansPaidForConsult() {
        progressDialog.showDialog();
        Call<fetched_physician_list_response_mode> bookedPhysicians = ApiControllers.getInstance()
                .getApi()
                .getBookedPhysicians("Bearer " + preferenceManager.getString(Constants.KEY_MONGO_TOKEN), dataType);

        bookedPhysicians.enqueue(new Callback<fetched_physician_list_response_mode>() {
            @Override
            public void onResponse(Call<fetched_physician_list_response_mode> call, Response<fetched_physician_list_response_mode> response) {
                progressDialog.dismissDialog();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getMessage() != null && response.body().getMessage().length() > 0) {
                        bookedPhysiciansBinding.emptyListSection.setVisibility(View.VISIBLE);
                    } else {
                        bookedPhysiciansBinding.recyclerView.setVisibility(View.VISIBLE);
                        bookedPhysiciansBinding.recyclerView.setLayoutManager(new LinearLayoutManager(BookedPhysicians.this));
                        List<physician_response_model> usersList = response.body().getData();
                        bookedPhysiciansData = new BookedPhysiciansData(usersList, preferenceManager.getString(Constants.KEY_MONGO_TOKEN));
                        bookedPhysiciansBinding.recyclerView.setAdapter(bookedPhysiciansData);
                    }
                }
            }

            @Override
            public void onFailure(Call<fetched_physician_list_response_mode> call, Throwable t) {
                progressDialog.dismissDialog();
                dialog.showDialog(BookedPhysicians.this, getResources().getDrawable(R.drawable.error), "Error", getResources().getColor(R.color.theme_color_orange_20), getResources().getColor(R.color.theme_color_orange), t.getMessage());
            }
        });
    }
}