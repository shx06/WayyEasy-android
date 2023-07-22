package com.wayyeasy.wayyeasy.Activities.Hospitals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wayyeasy.wayyeasy.Adapters.Hospitals.BedNumberData;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.HospitalBooking;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Images;
import com.wayyeasy.wayyeasy.Models.Hospitals.beds.beds_response_model;
import com.wayyeasy.wayyeasy.R;
import com.wayyeasy.wayyeasy.databinding.ActivityRoomDetailsBinding;

public class RoomDetails extends AppCompatActivity {

    ActivityRoomDetailsBinding roomDetailsBinding;

    private static final String TAG = "Rooms Detail";
    beds_response_model roomDetails;
    Dialog dialog;
    private String userId, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomDetailsBinding = ActivityRoomDetailsBinding.inflate(getLayoutInflater());
        setContentView(roomDetailsBinding.getRoot());

        Intent intent = getIntent();
        roomDetails = intent.getParcelableExtra("bedDetails");
        HospitalBooking.setRoomType(roomDetails.getRooms().getRoomType());

        SharedPreferences profile = getSharedPreferences("profile credentials", MODE_PRIVATE);
        if (profile.contains("name") || profile.contains("age") || profile.contains("email") ||
                profile.contains("gender") || profile.contains("address") || profile.contains("profile")
                || profile.contains("city")) {
            userId = profile.getString("id", "");;
            userName = profile.getString("name", "");
        }

        for (int i = 0; i < Images.getRoomImages().size(); i++) {

            byte[] roomImageByte;
            Bitmap roomImageBitmap;

            roomImageByte = Base64.decode(Images.getRoomImages().get(i).split(",")[1], Base64.DEFAULT);
            roomImageBitmap = BitmapFactory.decodeByteArray(roomImageByte, 0, roomImageByte.length);

            showImage(roomImageBitmap);
        }

        roomDetailsBinding.roomsType.setText(roomDetails.getRooms().getRoomType());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        roomDetailsBinding.roomsList.setLayoutManager(gridLayoutManager);

        BedNumberData bedNumberData = new BedNumberData(roomDetails.getRooms().getRoomDetails(), getApplicationContext(), userId, userName);
        roomDetailsBinding.roomsList.setAdapter(bedNumberData);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.non_medical_details_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_rounded_white_round));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView closeDialog, dialogHeader;
        dialogHeader = dialog.findViewById(R.id.non_medical_header);
        dialogHeader.setText("Non-medical expenses");
        dialogHeader.setPaintFlags(dialogHeader.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        closeDialog = dialog.findViewById(R.id.btn_ok);

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        roomDetailsBinding.nonMedicalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }

    private void showImage(Bitmap roomBitmap) {
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(roomBitmap);
        roomDetailsBinding.roomImages.addView(imageView);

        roomDetailsBinding.roomImages.setFlipInterval(3000);
        roomDetailsBinding.roomImages.setAutoStart(true);
        roomDetailsBinding.roomImages.setInAnimation(this, android.R.anim.slide_in_left);
        roomDetailsBinding.roomImages.setOutAnimation(this, android.R.anim.slide_out_right);
    }
}