package com.wayyeasy.wayyeasy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.wayyeasy.wayyeasy.Adapters.NotificationsAdapter;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.CallingPreferenceManager;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.SharedPreferenceManager;
import com.wayyeasy.wayyeasy.R;
import com.wayyeasy.wayyeasy.databinding.ActivityCallHistoryBinding;
import com.wayyeasy.wayyeasy.databinding.ActivityContactUsBinding;

public class CallHistoryActivity extends AppCompatActivity {

    private String TAG = "CallHistoryActivity";

    ActivityCallHistoryBinding callHistoryBinding;
    private CallingPreferenceManager callingPreferenceManager;
    private SharedPreferenceManager preferenceManager;

    private NotificationsAdapter notificationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callHistoryBinding = ActivityCallHistoryBinding.inflate(getLayoutInflater());
        setContentView(callHistoryBinding.getRoot());

        preferenceManager = new SharedPreferenceManager(this);
        callingPreferenceManager = new CallingPreferenceManager(this);

        callHistoryBinding.finishActivity.setOnClickListener(v -> {
            finish();
        });

        callHistoryBinding.clearAllCalls.setPaintFlags(callHistoryBinding.clearAllCalls.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        callHistoryBinding.clearAllCalls.setOnClickListener(v -> {
            callingPreferenceManager.clearPreferences();
            notificationsAdapter.notifyDataSetChanged();
            notificationHandler();
        });

        notificationHandler();
    }

    private void notificationHandler() {
        if (callingPreferenceManager.getCallList() != null && callingPreferenceManager.getCallList().size() > 0) {

            notificationsAdapter = new NotificationsAdapter(getApplicationContext(), callingPreferenceManager.getCallList(), preferenceManager.getString(Constants.KEY_MONGO_TOKEN));
            callHistoryBinding.notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            callHistoryBinding.notificationRecyclerView.setAdapter(notificationsAdapter);
        } else {
            callHistoryBinding.noNotificationLayout.setVisibility(View.VISIBLE);
            callHistoryBinding.clearAllCalls.setVisibility(View.GONE);
        }
    }
}