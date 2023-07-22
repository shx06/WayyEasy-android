package com.wayyeasy.wayyeasy.Activities.Physicians;

import static com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers.BASE_URL;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.SharedPreferenceManager;
import com.wayyeasy.wayyeasy.CustomDialogs.ProgressDialog;
import com.wayyeasy.wayyeasy.CustomDialogs.RatingsDialog;
import com.wayyeasy.wayyeasy.CustomDialogs.ResponseDialog;
import com.wayyeasy.wayyeasy.Models.Physicians.physician_booking_response_response_model;
import com.wayyeasy.wayyeasy.Models.Physicians.physician_response_model;
import com.wayyeasy.wayyeasy.Models.user.physicians_response_model;
import com.wayyeasy.wayyeasy.R;
import com.wayyeasy.wayyeasy.databinding.ActivityPhysicianDetailsBinding;
import java.text.SimpleDateFormat;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhysicianDetails extends AppCompatActivity {

    ActivityPhysicianDetailsBinding physicianDetailsBinding;
    private physician_response_model physician;
    physicians_response_model prescriptionData;
    ProgressDialog progressDialog;
    ResponseDialog responseDialog;
    SharedPreferenceManager sharedPreferenceManager;

    public static final String TAG = "PhysicianDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        physicianDetailsBinding = ActivityPhysicianDetailsBinding.inflate(getLayoutInflater());
        setContentView(physicianDetailsBinding.getRoot());

        physicianDetailsBinding.tvAvailable.setPaintFlags(physicianDetailsBinding.tvAvailable.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        sharedPreferenceManager = new SharedPreferenceManager(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        responseDialog = new ResponseDialog();

        Bundle bundle = getIntent().getExtras();

        //from both adapters
        physician = bundle.getParcelable(Constants.physicianData);

        if (physician != null) {
            physicianDetailsBinding.name.setText(physician.getName());
            physicianDetailsBinding.docSpeciality.setText(physician.getSpecialityType());
            physicianDetailsBinding.docQualification.setText(physician.getQualification());
            physicianDetailsBinding.docDescription.setText(physician.getDescription());

            if (physician.isAvailable()) {
                physicianDetailsBinding.docAvailability.setTextColor(getResources().getColor(R.color.green));
                physicianDetailsBinding.docAvailability.setText("Available");
            } else {
                physicianDetailsBinding.docAvailability.setTextColor(getResources().getColor(R.color.red));
                physicianDetailsBinding.docAvailability.setText("Unavailable");
            }

            physicianDetailsBinding.docStatus.setText(physician.getStatus());
            physicianDetailsBinding.shiftStart.setText(physician.getShiftStart());
            physicianDetailsBinding.shiftEnds.setText(physician.getShiftEnd());
            physicianDetailsBinding.workingDays.setText(physician.getWorkingDays());
            physicianDetailsBinding.docId.setText(physician.get_id());

            String imgUrl = BASE_URL + physician.getImage();

            GlideUrl glideUrl = new GlideUrl(imgUrl,
                    new LazyHeaders.Builder()
                            .addHeader("Authorization", "Bearer " + sharedPreferenceManager.getString(Constants.profileImage))
                            .build());

            Glide.with(PhysicianDetails.this).load(glideUrl).into(physicianDetailsBinding.userProfile);

            checkIfBooked();
        } else
            responseDialog.showDialog(PhysicianDetails.this, getResources().getDrawable(R.drawable.error), "Error", getResources().getColor(R.color.theme_color_orange_20), getResources().getColor(R.color.theme_color_orange), "\n\nCode 40004\n\nNo data found.\nPlease contact our customer support for any help.");


        physicianDetailsBinding.bookingBtn.setOnClickListener(view -> {
            if (profileCompleted()) {
                bookDoctor();
            } else
                responseDialog.showDialog(PhysicianDetails.this, getResources().getDrawable(R.drawable.error), "Important", getResources().getColor(R.color.theme_color_orange_20), getResources().getColor(R.color.theme_color_orange), "Please set up your profile properly before booking.");
        });
    }

    private void checkIfBooked() {
        progressDialog.showDialog();
        Call<physician_booking_response_response_model> call = ApiControllers
                .getInstance()
                .getApi()
                .getConsultation("Bearer " + sharedPreferenceManager.getString(Constants.KEY_MONGO_TOKEN), physician.get_id());

        call.enqueue(new Callback<physician_booking_response_response_model>() {
            @Override
            public void onResponse(Call<physician_booking_response_response_model> call, Response<physician_booking_response_response_model> response) {
                progressDialog.dismissDialog();
                if (response.body() != null && response.body().getConsultation() != null && response.body().getConsultation().equals("pending")) {
                    physicianDetailsBinding.closeConsultation.setVisibility(View.VISIBLE);
                    physicianDetailsBinding.makeCall.setVisibility(View.VISIBLE);
                    physicianDetailsBinding.viewPrescriptiom.setVisibility(View.VISIBLE);
                    getPrescription(response.body().getPhysicianId());
                } else {
                    physicianDetailsBinding.bookingBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<physician_booking_response_response_model> call, Throwable t) {
                progressDialog.dismissDialog();
                responseDialog.showDialog(PhysicianDetails.this, getResources().getDrawable(R.drawable.error), "Error", getResources().getColor(R.color.theme_color_orange_20), getResources().getColor(R.color.theme_color_orange), t.getMessage());
            }
        });
    }

    private void getPrescription(String physicianId) {
        progressDialog.showDialog();
        Call<physicians_response_model> call = ApiControllers
                .getInstance()
                .getApi()
                .getPrescription("Bearer " + sharedPreferenceManager.getString(Constants.KEY_MONGO_TOKEN), physicianId);

        call.enqueue(new Callback<physicians_response_model>() {
            @Override
            public void onResponse(Call<physicians_response_model> call, Response<physicians_response_model> response) {
                progressDialog.dismissDialog();
                prescriptionData = response.body();
            }

            @Override
            public void onFailure(Call<physicians_response_model> call, Throwable t) {
                progressDialog.dismissDialog();
                Log.d(TAG, "onFailure: Fetching prescription: " + t.getMessage());
            }
        });
    }

    private boolean profileCompleted() {
        boolean profileUpdated = false;
        if ((sharedPreferenceManager.getString(Constants.name) != null && sharedPreferenceManager.getString(Constants.name).length() > 0)
                && (sharedPreferenceManager.getString(Constants.age) != null && sharedPreferenceManager.getString(Constants.age).length() > 0)
                && (sharedPreferenceManager.getString(Constants.sex) != null && sharedPreferenceManager.getString(Constants.sex).length() > 0)
                && (sharedPreferenceManager.getString(Constants.profileImage) != null && sharedPreferenceManager.getString(Constants.profileImage).length() > 0))
            profileUpdated = true;

        return profileUpdated;
    }

    public void bookDoctor() {
        progressDialog.showDialog();

        Call<physician_booking_response_response_model> book = ApiControllers
                .getInstance()
                .getApi()
                .bookPhysicianByUser("Bearer " + sharedPreferenceManager.getString(Constants.KEY_MONGO_TOKEN), physician.get_id());

        book.enqueue(new Callback<physician_booking_response_response_model>() {
            @Override
            public void onResponse(Call<physician_booking_response_response_model> call, Response<physician_booking_response_response_model> response) {
                progressDialog.dismissDialog();
                Log.d(TAG, "onResponse: " + response.body().getConsultation());
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getMessage() != null)
                        responseDialog.showDialog(PhysicianDetails.this, getResources().getDrawable(R.drawable.error), "Error", getResources().getColor(R.color.theme_color_orange_20), getResources().getColor(R.color.theme_color_orange), response.body().getMessage());
                    else
                        responseDialog.showDialog(PhysicianDetails.this, getResources().getDrawable(R.drawable.success), "Booking Confirmed", getResources().getColor(R.color.green_20), getResources().getColor(R.color.green), "You have book this doctor for consultation.");
                } else {
                    responseDialog.showDialog(PhysicianDetails.this, getResources().getDrawable(R.drawable.error), "Error", getResources().getColor(R.color.theme_color_orange_20), getResources().getColor(R.color.theme_color_orange), response.message());
                }
            }

            @Override
            public void onFailure(Call<physician_booking_response_response_model> call, Throwable t) {
                progressDialog.dismissDialog();
                responseDialog.showDialog(PhysicianDetails.this, getResources().getDrawable(R.drawable.error), "Error", getResources().getColor(R.color.theme_color_orange_20), getResources().getColor(R.color.theme_color_orange), t.getMessage());
            }
        });
    }

    public void viewPrescription(View view) {
        if (prescriptionData != null && prescriptionData.getPrescription() != null && prescriptionData.getPrescription().size() > 0) {
            Intent intent = new Intent(getApplicationContext(), ViewPrescriptionActivity.class);
            intent.putExtra(Constants.prescription, prescriptionData);
            intent.putExtra(Constants.speciality, physician.getSpecialityType());
            intent.putExtra(Constants.physicianName, physician.getName());
            startActivity(intent);
        } else
            Toast.makeText(this, "No prescription found!!!", Toast.LENGTH_SHORT).show();
    }

    public void makeAudioCall(View view) {
        try {
            Date mToday = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
            String curTime = sdf.format(mToday);
            Date currentTime = sdf.parse(curTime);
            Date shiftStart = sdf.parse(physician.getShiftStart());
            Date shiftEnd = sdf.parse(physician.getShiftEnd());

            if (currentTime.before(shiftStart)) {
                Toast.makeText(this, "Doctor is not available now. Please check shift time properly", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentTime.after(shiftEnd)) {
                Toast.makeText(this, "Doctor is not available now. Please check shift time properly", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(getApplicationContext(), OutgoingInvitation.class);
            intent.putExtra(Constants.physicianData, physician);
            intent.putExtra("type", "audio");
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void makeVideoCall(View view) {
        try {
            Date mToday = new Date();

//            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
//            String curTime = sdf.format(mToday);
//            Date currentTime = sdf.parse(curTime);
//            Date shiftStart = sdf.parse(physician.getShiftStart());
//            Date shiftEnd = sdf.parse(physician.getShiftEnd());
//
//            if (currentTime.before(shiftStart)) {
//                Toast.makeText(this, "Doctor is not available now. Please check shift time properly", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (currentTime.after(shiftEnd)) {
//                Toast.makeText(this, "Doctor is not available now. Please check shift time properly", Toast.LENGTH_SHORT).show();
//                return;
//            }

            Intent intent = new Intent(getApplicationContext(), OutgoingInvitation.class);
            intent.putExtra(Constants.physicianData, physician);
            intent.putExtra("type", "video");
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(this, "Error 294: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void clearActivity(View view) {
        finish();
    }

    public void handleDescription(View view) {
        if (physicianDetailsBinding.expandDesc.getText().toString().equals("Read More")) {
            physicianDetailsBinding.docDescription.setEllipsize(null);
            physicianDetailsBinding.docDescription.setMaxLines(Integer.MAX_VALUE);
            physicianDetailsBinding.expandDesc.setText("Read Less");
        } else {
            physicianDetailsBinding.docDescription.setEllipsize(TextUtils.TruncateAt.END);
            physicianDetailsBinding.docDescription.setMaxLines(3);
            physicianDetailsBinding.expandDesc.setText("Read More");
        }
    }

    public void closeConsultation(View view) {
        progressDialog.showDialog();
        Call<Void> finishConsultation = ApiControllers
                .getInstance()
                .getApi()
                .finishConsultation("Bearer " + sharedPreferenceManager.getString(Constants.KEY_MONGO_TOKEN), physician.get_id());

        finishConsultation.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismissDialog();
                if (response.code() == 200) {
                    RatingsDialog ratingsDialog = new RatingsDialog(PhysicianDetails.this, physician.get_id());
                    ratingsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                    ratingsDialog.setCancelable(false);
                    ratingsDialog.show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismissDialog();
                responseDialog.showDialog(PhysicianDetails.this, getResources().getDrawable(R.drawable.error), "Error", getResources().getColor(R.color.theme_color_orange_20), getResources().getColor(R.color.theme_color_orange), t.getMessage());
            }
        });
    }
}