package com.wayyeasy.wayyeasy.Activities.Hospitals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wayyeasy.wayyeasy.databinding.ActivityUserProfileBinding;
import com.wayyeasy.wayyeasy.databinding.ActivityVisitedHospitalsBinding;

public class VisitedHospitals extends AppCompatActivity {

    ActivityVisitedHospitalsBinding visitedHospitals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visitedHospitals = ActivityVisitedHospitalsBinding.inflate(getLayoutInflater());
        setContentView(visitedHospitals.getRoot());
    }
}