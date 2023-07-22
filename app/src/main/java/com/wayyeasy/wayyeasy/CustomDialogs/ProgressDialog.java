package com.wayyeasy.wayyeasy.CustomDialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wayyeasy.wayyeasy.R;

public class ProgressDialog {
    Dialog dialog;
    Context context;

    public ProgressDialog() {
    }

    public ProgressDialog(Context context) {
        this.context = context;
    }

    public void showDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_process_layout);
        ImageView progressIndicator = dialog.findViewById(R.id.progress_indicator);

        Glide.with(context).load(R.drawable.progress_indicator).into(progressIndicator);

        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
