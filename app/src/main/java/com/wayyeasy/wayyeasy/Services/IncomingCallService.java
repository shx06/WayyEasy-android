package com.wayyeasy.wayyeasy.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.wayyeasy.wayyeasy.Activities.CallHistoryActivity;
import com.wayyeasy.wayyeasy.Activities.Physicians.IncomingInvitation;
import com.wayyeasy.wayyeasy.ComponentFiles.CallingApiHandlers.CallingApiControllers;
import com.wayyeasy.wayyeasy.ComponentFiles.CallingApiHandlers.CallingApiSet;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.CallingPreferenceManager;
import com.wayyeasy.wayyeasy.Models.OnlineCalls.call_response_model;
import com.wayyeasy.wayyeasy.R;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomingCallService extends Service {

    private final String TAG = "IncomingCallService";

    //foreground notification
    private final int SERVICE_NOTIFICATION_ID = 1005;
    public final String SERVICE_NOTIFICATION_CHANNEL_ID = "SERVICE_NOTIFICATION_CHANNEL_ID";
    public final String SERVICE_NOTIFICATION_NAME = "SERVICE_NOTIFICATION_CHANNEL_NAME";

    //custom notification
    private final String CUSTOM_NOTIFICATION_NAME = "CUSTOM_NOTIFICATION_CHANNEL_NAME";
    private final int CUSTOM_NOTIFICATION_ID = 1010;
    private MediaPlayer mediaPlayer;
    private CallingPreferenceManager callingPreferenceManager;
    private ArrayList<call_response_model> callList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        callingPreferenceManager = new CallingPreferenceManager(this);
        mediaPlayer = MediaPlayer.create(this, R.raw.incoming_call_ring);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            switch (intent.getAction()) {

                case Constants.PLAY_RINGTONE:
                    createNotification();
                    startRinging();
                    break;

                case Constants.CALL_MISSED:

                    addCallToList(callingPreferenceManager.getString(Constants.CALLING_PERSON_MONGO_ID),
                            callingPreferenceManager.getString(Constants.CALLING_PERSON),
                            callingPreferenceManager.getString(Constants.CALLING_PERSON_IMAGE),
                            callingPreferenceManager.getString(Constants.CALLING_PERSON_ROLE),
                            Constants.CALL_MISSED, new Date().toString());

                    stopRinging();

                    showMissedCallNotification();

                    stopForeground(true);
                    stopSelf();
                    break;

                case Constants.RECEIVE_CALL:

                    addCallToList(callingPreferenceManager.getString(Constants.CALLING_PERSON_MONGO_ID),
                            callingPreferenceManager.getString(Constants.CALLING_PERSON),
                            callingPreferenceManager.getString(Constants.CALLING_PERSON_IMAGE),
                            callingPreferenceManager.getString(Constants.CALLING_PERSON_ROLE),
                            Constants.RECEIVE_CALL, new Date().toString());

                    initiateMeeting();
                    stopRinging();
                    stopForeground(true);
                    stopSelf();
                    break;

                case Constants.CALL_CHECKED:

                    addCallToList(callingPreferenceManager.getString(Constants.CALLING_PERSON_MONGO_ID),
                            callingPreferenceManager.getString(Constants.CALLING_PERSON),
                            callingPreferenceManager.getString(Constants.CALLING_PERSON_IMAGE),
                            callingPreferenceManager.getString(Constants.CALLING_PERSON_ROLE),
                            Constants.CALL_CHECKED, new Date().toString());

                    stopRinging();
                    stopForeground(true);
                    stopSelf();
                    break;

                case Constants.REJECT_CALL:

                    addCallToList(callingPreferenceManager.getString(Constants.CALLING_PERSON_MONGO_ID),
                            callingPreferenceManager.getString(Constants.CALLING_PERSON),
                            callingPreferenceManager.getString(Constants.CALLING_PERSON_IMAGE),
                            callingPreferenceManager.getString(Constants.CALLING_PERSON_ROLE),
                            Constants.REJECT_CALL, new Date().toString());

                    stopRinging();

                    sendResponse(Constants.REMOTE_MSG_INVITATION_REJECTED,
                            callingPreferenceManager.getString(Constants.REMOTE_MESSAGE_INVITER_TOKEN));

                    stopForeground(true);
                    stopSelf();

                    break;

                default:
                    break;
            }
        }

        return START_STICKY;
    }

    private void createNotification() {

        Intent activityIntent = new Intent(getApplicationContext(), IncomingInvitation.class);
        activityIntent.putExtra(Constants.CALL_CHECKED, Constants.CALL_CHECKED);
        PendingIntent activityPIntent = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent startRingIntent = new Intent(this, IncomingCallService.class);
        startRingIntent.setAction(Constants.RECEIVE_CALL);
        PendingIntent startRingPendingIntent = PendingIntent.getService(this, 111, startRingIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent stopRingIntent = new Intent(this, IncomingCallService.class);
        stopRingIntent.setAction(Constants.REJECT_CALL);
        PendingIntent stopRingPendingIntent = PendingIntent.getService(this, 1231, stopRingIntent, PendingIntent.FLAG_IMMUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(SERVICE_NOTIFICATION_CHANNEL_ID,
                    SERVICE_NOTIFICATION_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Build a notification for the foreground service
        Notification notification = new NotificationCompat.Builder(this, SERVICE_NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Incoming Call")
                .setContentText(callingPreferenceManager.getString(Constants.CALLING_PERSON))
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(activityPIntent)
                .addAction(new NotificationCompat.Action(R.drawable.ic_call, "Receive", startRingPendingIntent))
                .addAction(new NotificationCompat.Action(R.drawable.ic_call, "Cancel", stopRingPendingIntent))
                .build();

        startForeground(SERVICE_NOTIFICATION_ID, notification);
    }

    private void addCallToList(String id, String name, String image, String role, String status, String date) {

        if (callingPreferenceManager.getCallList() != null)
            for (int i = 0; i < callingPreferenceManager.getCallList().size(); i++) {
                callList.add(new call_response_model(callingPreferenceManager.getCallList().get(i).getId(),
                        callingPreferenceManager.getCallList().get(i).getName(),
                        callingPreferenceManager.getCallList().get(i).getImage(),
                        callingPreferenceManager.getCallList().get(i).getRole(),
                        callingPreferenceManager.getCallList().get(i).getStatus(),
                        callingPreferenceManager.getCallList().get(i).getDate()));
            }

        callList.add(new call_response_model(id, name, image, role, status, date));

        Log.d(TAG, "addCallToList: " + id + name + image + role + status + date);
        Log.d(TAG, "addCallToList: " + new Gson().toJson(callList));

        callingPreferenceManager.addCallList(callList);
    }

    private void showMissedCallNotification() {

        Intent intent = new Intent(this, CallHistoryActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.icon_white) // notification icon
                    .setContentTitle("Missed Call")
                    .setColor(getResources().getColor(R.color.red))
                    .setContentText(callingPreferenceManager.getString(Constants.CALLING_PERSON))
                    .setAutoCancel(true)
                    .setChannelId(CUSTOM_NOTIFICATION_NAME)
                    .setContentIntent(pIntent).build();

            notificationManager.createNotificationChannel(new NotificationChannel(CUSTOM_NOTIFICATION_NAME, "Incoming Call Notification", NotificationManager.IMPORTANCE_HIGH));

        } else {
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.icon_white) // notification icon
                    .setContentTitle("Missed Call")
                    .setColor(getResources().getColor(R.color.red))
                    .setContentText(callingPreferenceManager.getString(Constants.CALLING_PERSON))
                    .setAutoCancel(true)
                    .setContentIntent(pIntent).build();
        }

        notificationManager.notify(CUSTOM_NOTIFICATION_ID, notification);

    }

    private void sendResponse(String type, String receiverToken) {
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION_RESPONSE);
            data.put(Constants.REMOTE_MSG_INVITATION_RESPONSE, type);

            body.put(Constants.REMOTE_MESSAGE_DATA, data);
            body.put(Constants.REMOTE_MESSAGE_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString());
        } catch (Exception e) {
            Log.d(TAG, "sendInvitationResponse: 65 " + e.getMessage());
        }
    }

    private void sendRemoteMessage(String remoteBodyMessage) {

        CallingApiControllers.getClient().create(CallingApiSet.class).sendRemoteMessage(
                        Constants.getRemoteMessageHeaders(), remoteBodyMessage)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.d(TAG, "onResponse: " + response);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Log.d(TAG, "Error: 105 " + t.getMessage());
                    }
                });
    }

    private void initiateMeeting() {
        try {

            URL serverURL = new URL(Constants.JITSI_MEET_SERVER_URL);

            String meetingRoom = callingPreferenceManager.getString(Constants.REMOTE_MSG_MEETING_ROOM) != null ? callingPreferenceManager.getString(Constants.REMOTE_MSG_MEETING_ROOM) : "default meet";

            JitsiMeetConferenceOptions.Builder builder = new JitsiMeetConferenceOptions.Builder();
            builder.setServerURL(serverURL);
            builder.setRoom(meetingRoom);

            if (callingPreferenceManager.getString(Constants.REMOTE_MESSAGE_MEETING_TYPE).equals("audio")) {
                builder.setVideoMuted(true);
            }

            JitsiMeetActivity.launch(IncomingCallService.this, builder.build());

        } catch (Exception e) {
            Log.d(TAG, "onResponse: 109 " + e.getMessage());
        }
    }

    public void startRinging() {
        mediaPlayer.start();
    }

    public void stopRinging() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}