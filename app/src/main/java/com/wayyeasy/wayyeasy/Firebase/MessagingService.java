package com.wayyeasy.wayyeasy.Firebase;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.wayyeasy.wayyeasy.Activities.Physicians.IncomingInvitation;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.CallingPreferenceManager;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.SharedPreferenceManager;
import com.wayyeasy.wayyeasy.Services.IncomingCallService;

import java.util.List;

public class MessagingService extends FirebaseMessagingService {
    public static final String TAG = "Firebase CloudMessage";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "onNewToken: 21 " + token);
        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(this);
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
    }

    public boolean isForeground(String myPackage) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        return componentInfo.getPackageName().equals(myPackage);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String type = remoteMessage.getData().get(Constants.REMOTE_MSG_TYPE);

        Log.d(TAG, "onMessageReceived: type " + type);

        if (type != null) {
            if (type.equals(Constants.REMOTE_MSG_INVITATION)) {
                if (isForeground(getApplicationContext().getPackageName())) {

                    Intent intent = new Intent(getApplicationContext(), IncomingInvitation.class);
                    intent.putExtra(Constants.REMOTE_MESSAGE_MEETING_TYPE, remoteMessage.getData().get(Constants.REMOTE_MESSAGE_MEETING_TYPE));
                    intent.putExtra(Constants.name, remoteMessage.getData().get(Constants.name));
                    intent.putExtra(Constants.REMOTE_MESSAGE_INVITER_TOKEN, remoteMessage.getData().get(Constants.REMOTE_MESSAGE_INVITER_TOKEN));
                    intent.putExtra(Constants.REMOTE_MSG_MEETING_ROOM, remoteMessage.getData().get(Constants.REMOTE_MSG_MEETING_ROOM));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    wakeApp();

                    CallingPreferenceManager preferenceManager = new CallingPreferenceManager(this);

                    Intent serviceIntent = new Intent(this, IncomingCallService.class);
                    serviceIntent.setAction(Constants.PLAY_RINGTONE);

                    preferenceManager.putString(Constants.CALLING_PERSON_MONGO_ID, remoteMessage.getData().get(Constants.KEY_MONGO_ID));
                    preferenceManager.putString(Constants.CALLING_PERSON, remoteMessage.getData().get(Constants.name));
                    preferenceManager.putString(Constants.CALLING_PERSON_IMAGE, remoteMessage.getData().get("image"));
                    preferenceManager.putString(Constants.CALLING_PERSON_ROLE, remoteMessage.getData().get(Constants.role));

                    preferenceManager.putString(Constants.REMOTE_MSG_MEETING_ROOM, remoteMessage.getData().get(Constants.REMOTE_MSG_MEETING_ROOM));
                    preferenceManager.putString(Constants.REMOTE_MESSAGE_INVITER_TOKEN, remoteMessage.getData().get(Constants.REMOTE_MESSAGE_INVITER_TOKEN));
                    preferenceManager.putString(Constants.REMOTE_MESSAGE_MEETING_TYPE, remoteMessage.getData().get(Constants.REMOTE_MESSAGE_MEETING_TYPE));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    } else {
                        startService(serviceIntent);
                    }
                }

            } else if (type.equals(Constants.REMOTE_MSG_INVITATION_RESPONSE)) {

                if (isForeground(getApplicationContext().getPackageName())) {

                    Intent intent = new Intent(Constants.REMOTE_MSG_INVITATION_RESPONSE);
                    intent.putExtra(Constants.REMOTE_MSG_INVITATION_RESPONSE, remoteMessage.getData().get(Constants.REMOTE_MSG_INVITATION_RESPONSE));
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                } else {

                    wakeApp();

                    Intent serviceIntent = new Intent(this, IncomingCallService.class);
                    serviceIntent.setAction(Constants.CALL_MISSED);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    } else {
                        startService(serviceIntent);
                    }
                }
            }
        }
    }

    // if screen is off when receive call
    private void wakeApp() {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        boolean screenIsOn = pm.isInteractive(); // check if screen is on
        if (!screenIsOn) {
            final String wakeLockTag = getPackageName() + "WAKELOCK";

            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE, wakeLockTag);

            //acquire will turn on the display
            wakeLock.acquire();

            //release will release the lock from CPU, in case of that, screen will go back to sleep mode in defined time bt device settings
            wakeLock.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
