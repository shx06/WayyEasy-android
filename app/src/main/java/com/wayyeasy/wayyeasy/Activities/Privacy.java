package com.wayyeasy.wayyeasy.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.databinding.ActivityPrivacyBinding;

public class Privacy extends AppCompatActivity {

    ActivityPrivacyBinding privacyBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        privacyBinding = ActivityPrivacyBinding.inflate(getLayoutInflater());
        setContentView(privacyBinding.getRoot());

        String path = getIntent().getStringExtra(Constants.policiesLink);

        privacyBinding.privacyView.loadUrl(path);
    }

    public void clearActivity(View view) {
        finish();
    }
}