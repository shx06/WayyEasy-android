package com.wayyeasy.wayyeasy.Activities;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wayyeasy.wayyeasy.databinding.ActivityContactUsBinding;

public class ContactUs extends AppCompatActivity {

    ActivityContactUsBinding contactUsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactUsBinding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(contactUsBinding.getRoot());

        contactUsBinding.finishActivity.setOnClickListener(v -> {
            finish();
        });

        contactUsBinding.contactHeading.setPaintFlags(contactUsBinding.contactHeading.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public void makeCall(View view) {
        Uri callUri = Uri.parse("tel:9748976922");
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(callUri);
        try {
            startActivity(callIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "emailSupport: " + e.getMessage());
        }
    }

    public void makeWhatsapp(View view) {
        Uri whatsappUri = Uri.parse("smsto:" + "9748976922");
        Intent whatsappIntent = new Intent(Intent.ACTION_SENDTO, whatsappUri);
        whatsappIntent.setPackage("com.whatsapp");
        try {
            startActivity(whatsappIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "connectWhatsapp: " + e.getMessage());
        }
    }

    public void makeEmail(View view) {
        Uri emailUri = Uri.parse("mailto:" + "wayy.easy.team@gmail.com");
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(emailUri);
        try {
            startActivity(emailIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "emailSupport: " + e.getMessage());
        }
    }
}