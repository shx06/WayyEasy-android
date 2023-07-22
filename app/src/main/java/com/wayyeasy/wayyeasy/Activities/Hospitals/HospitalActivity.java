package com.wayyeasy.wayyeasy.Activities.Hospitals;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.wayyeasy.wayyeasy.Adapters.Hospitals.BedsData;
import com.wayyeasy.wayyeasy.Adapters.Hospitals.DoctorsData;
import com.wayyeasy.wayyeasy.Adapters.Hospitals.RatingsData;
import com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Images;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.SharedPreferenceManager;
import com.wayyeasy.wayyeasy.Fragments.Hospitals.HospitalContact;
import com.wayyeasy.wayyeasy.Models.Hospitals.beds.beds_response_model;
import com.wayyeasy.wayyeasy.Models.Hospitals.doctors_response_model;
import com.wayyeasy.wayyeasy.Models.Hospitals.hospitals_ratings_response_model;
import com.wayyeasy.wayyeasy.R;
import com.wayyeasy.wayyeasy.databinding.ActivityHospitalBinding;

import java.text.DecimalFormat;
import java.util.List;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalActivity extends AppCompatActivity {
    private static final String TAG = "Hospital";
    ActivityHospitalBinding hospital;
    String id, token;
    public static String hospitalId = null;
    SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hospital = ActivityHospitalBinding.inflate(getLayoutInflater());
        setContentView(hospital.getRoot());

        for (int i = 0; i < Images.getHospitalImages().size(); i++) {
            byte[] hospitalImageByte;
            Bitmap hospitalImageBitmap;

            hospitalImageByte = Base64.decode(Images.getHospitalImages().get(i).split(",")[1], Base64.DEFAULT);
            hospitalImageBitmap = BitmapFactory.decodeByteArray(hospitalImageByte, 0, hospitalImageByte.length);

            showImage(hospitalImageBitmap);
        }

        hospitalId = getIntent().getStringExtra("id");
        hospital.hospitalName.setText(getIntent().getStringExtra("name"));
        hospital.hospitalAddress.setText(getIntent().getStringExtra("address"));
        hospital.hospitalDescription.setText(getIntent().getStringExtra("description"));

        preferenceManager = new SharedPreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            token = preferenceManager.getString(Constants.KEY_MONGO_TOKEN);
            id = preferenceManager.getString(Constants.KEY_MONGO_ID);
        }

        hospital.home.setOnClickListener(view -> hospital.scrollView.smoothScrollTo(0, 450));
        hospital.details.setOnClickListener(view -> hospital.scrollView.smoothScrollTo(0, 1000));
        hospital.review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hospital.scrollView.smoothScrollTo(0, 2100);
            }
        });
        hospital.contact.setOnClickListener(view -> {
            String contactOptions[] = getIntent().getStringExtra("contact").split(",");

            HospitalContact hospitalContact = new HospitalContact(contactOptions);
            hospitalContact.show(getSupportFragmentManager(), hospitalContact.getTag());
        });

        hospital.ll2.setOnClickListener(view -> {
            try {
                Uri uri = Uri.parse("geo:0, 0?q=" + getIntent().getStringExtra("address"));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            } catch (Exception e) {
                Log.d(TAG, "onClick: " + e);
            }
        });
        fetchDetails(token);
    }

    private void showImage(Bitmap hospitalImageBitmap) {
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(hospitalImageBitmap);
        hospital.hospitalImg.addView(imageView);

        hospital.hospitalImg.setFlipInterval(3000);
        hospital.hospitalImg.setAutoStart(true);
        hospital.hospitalImg.setInAnimation(this, android.R.anim.slide_in_left);
        hospital.hospitalImg.setOutAnimation(this, android.R.anim.slide_out_right);
    }

    private Emitter.Listener onNewMessage = args -> runOnUiThread(() -> Toast.makeText(HospitalActivity.this, "Message", Toast.LENGTH_SHORT).show());

    private void fetchDetails(String token) {
        Call<List<beds_response_model>> bedsData = ApiControllers
                .getInstance()
                .getApi()
                .getRoomsByHospital("Bearer " + token, hospitalId);

        bedsData.enqueue(new Callback<List<beds_response_model>>() {

            @Override
            public void onResponse(Call<List<beds_response_model>> call, Response<List<beds_response_model>> response) {
                try {
                    List<beds_response_model> data = response.body();
                    hospital.bedsDetails.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    BedsData bedsData = new BedsData(data, getApplicationContext());
                    hospital.bedsDetails.setAdapter(bedsData);
                } catch (Exception ex) {
                    Log.d(TAG, "onResponse: " + ex);
                }
            }

            @Override
            public void onFailure(Call<List<beds_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailureBeds: " + t);
            }
        });

        Call<List<doctors_response_model>> doctorsData = ApiControllers
                .getInstance()
                .getApi()
                .getDoctorsByHospital("Bearer " + token, hospitalId);

        doctorsData.enqueue(new Callback<List<doctors_response_model>>() {
            @Override
            public void onResponse(Call<List<doctors_response_model>> call, Response<List<doctors_response_model>> response) {
                try {
                    List<doctors_response_model> data = response.body();
                    hospital.doctorsData.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    DoctorsData doctorsData = new DoctorsData(data, getApplicationContext());
                    hospital.doctorsData.setAdapter(doctorsData);
                } catch (Exception ex) {
                    Log.d(TAG, "onResponse: " + ex);
                }
            }

            @Override
            public void onFailure(Call<List<doctors_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailureDoctors: " + t);
            }
        });

        Call<List<hospitals_ratings_response_model>> ratingsData = ApiControllers
                .getInstance()
                .getApi()
                .getRatingsByHospital("Bearer " + token, hospitalId);

        ratingsData.enqueue(new Callback<List<hospitals_ratings_response_model>>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<List<hospitals_ratings_response_model>> call, Response<List<hospitals_ratings_response_model>> response) {
                try {
                    List<hospitals_ratings_response_model> data = response.body();
                    hospital.totalRatings.setText(Integer.toString(new Integer(data.size())));
                    float locationsRating = 0, treatmentsRating = 0, cleanRatings = 0, staffsRating = 0, valueForMoneyRatings = 0, averageRatings = 0;;
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getLocation() > 0)
                            locationsRating += data.get(i).getLocation();

                        if (data.get(i).getTreatement() > 0)
                            treatmentsRating += data.get(i).getTreatement();

                        if (data.get(i).getClean() > 0)
                            cleanRatings += data.get(i).getClean();

                        if (data.get(i).getValueForMoney() > 0)
                            valueForMoneyRatings += data.get(i).getValueForMoney();

                        if (data.get(i).getStaffs() > 0)
                            staffsRating += data.get(i).getStaffs();

                        if (data.get(i).getAverage() > 0) {
                            averageRatings += data.get(i).getAverage();
                        }
                    }
                    locationsRating /= data.size();
                    locationsRating = locationsRating * 20;
                    treatmentsRating /= data.size();
                    treatmentsRating = treatmentsRating * 20;
                    cleanRatings /= data.size();
                    cleanRatings = cleanRatings * 20;
                    staffsRating /= data.size();
                    staffsRating = staffsRating * 20;
                    valueForMoneyRatings /= data.size();
                    valueForMoneyRatings = valueForMoneyRatings * 20;
                    averageRatings /= data.size();

                    if (averageRatings >= 4) {
                        hospital.hospitalCondition.setText("Condition Very Good");
                        hospital.hospitalConditionBack.setCardBackgroundColor(getResources().getColor(R.color.green));
                    }
                    if (averageRatings >= 3 && averageRatings < 4) {
                        hospital.hospitalCondition.setText("Condition Good");
                        hospital.hospitalConditionBack.setCardBackgroundColor(getResources().getColor(R.color.light_orange));
                    }
                    if (averageRatings < 3 && averageRatings >= 2) {
                        hospital.hospitalConditionBack.setCardBackgroundColor(getResources().getColor(R.color.light_red));
                        hospital.hospitalCondition.setText("Condition Satisfactory");
                    }
                    if (averageRatings < 2 && averageRatings > 0) {
                        hospital.hospitalConditionBack.setCardBackgroundColor(getResources().getColor(R.color.red));
                        hospital.hospitalCondition.setText("Condition not Good");
                    }

                    DecimalFormat numFormat = new DecimalFormat();
                    numFormat.setMaximumFractionDigits(2);

                    hospital.hospitalRatings.setRating(averageRatings);
                    hospital.locationRatings.setProgress((int) locationsRating);
                    hospital.locationRatings.setMax(100);
                    hospital.locationCount.setText(Float.toString(Float.parseFloat(numFormat.format(locationsRating/20))));
                    hospital.treatmentRatings.setProgress((int) treatmentsRating);
                    hospital.treatmentRatings.setMax(100);
                    hospital.treatmentCount.setText(Float.toString(Float.parseFloat(numFormat.format(treatmentsRating/20))));
                    hospital.cleanRatings.setProgress((int) cleanRatings);
                    hospital.cleanRatings.setMax(100);
                    hospital.cleanCount.setText(Float.toString(Float.parseFloat(numFormat.format(cleanRatings/20))));
                    hospital.staffRatings.setProgress((int) staffsRating);
                    hospital.staffRatings.setMax(100);
                    hospital.staffCount.setText(Float.toString(Float.parseFloat(numFormat.format(staffsRating/20))));
                    hospital.valueForMoneyRatings.setProgress((int) valueForMoneyRatings);
                    hospital.valueForMoneyRatings.setMax(100);
                    hospital.valueForMoneyCount.setText(Float.toString(Float.parseFloat(numFormat.format(valueForMoneyRatings/20))));
                    hospital.reviewsData.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    RatingsData ratingsData = new RatingsData(data, getApplicationContext());
                    hospital.reviewsData.setAdapter(ratingsData);
                } catch (Exception ex) {
                    Log.d(TAG, "onResponse: " + ex);
                }
            }

            @Override
            public void onFailure(Call<List<hospitals_ratings_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailureRatings: " + t);
            }
        });
    }

    public void handleDescription(View view) {
        if (hospital.descriptionHandler.getText() == "Read more") {
            hospital.hospitalDescription.setEllipsize(null);
            hospital.hospitalDescription.setMaxLines(Integer.MAX_VALUE);
            hospital.descriptionHandler.setText("Read less");
        } else {
            hospital.hospitalDescription.setEllipsize(TextUtils.TruncateAt.END);
            hospital.hospitalDescription.setMaxLines(3);
            hospital.descriptionHandler.setText("Read more");
        }
    }
}