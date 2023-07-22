package com.wayyeasy.wayyeasy.CustomDialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.wayyeasy.wayyeasy.R;

public class ResponseDialog {
    public void showDialog(Context context, Drawable drawable, String header, int backGroundColor, int color, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_response_layout);

        ImageView responseImage = (ImageView) dialog.findViewById(R.id.dialog_image);
        TextView dialogHeader = (TextView) dialog.findViewById(R.id.dialog_header);
        TextView dialogMessage = (TextView) dialog.findViewById(R.id.dialog_message);
        LinearLayout layout = dialog.findViewById(R.id.dialog_layout);

        layout.setBackgroundColor(backGroundColor);
        Glide.with(context).load(drawable).into(new DrawableImageViewTarget(responseImage));
        dialogHeader.setText(header);
        dialogHeader.setTextColor(color);
        dialogMessage.setText(message);

        CardView btnOK = (CardView) dialog.findViewById(R.id.dialog_btn_ok);
        btnOK.setCardBackgroundColor(color);

        btnOK.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
