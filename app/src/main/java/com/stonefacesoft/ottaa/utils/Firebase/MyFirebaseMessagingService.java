package com.stonefacesoft.ottaa.utils.Firebase;


import android.annotation.TargetApi;
import android.app.Notification;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.stonefacesoft.ottaa.utils.NotificationUtils;

/**
 * Created by GastonSaillen on 22/12/2017.
 */

    //TODO chequear el Warning sobre el metodo NewToken
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    //MFMS : MyFirebaseMessagingService
    private NotificationUtils mNotificationUtils;

    private static final String TAG = "MyFirebaseMessaging";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());
                //sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
            } else {
                Log.d(TAG, "Enviado por :" + remoteMessage.getFrom());
            }
        }

    }


    @TargetApi(Build.VERSION_CODES.O)
    private void sendNotification(String title, String messageBody) {
        mNotificationUtils = new NotificationUtils(getApplicationContext());
        // mNotificationUtils.createChannels();
        Notification.Builder nb = mNotificationUtils.getAndroidChannelNotification(title, messageBody);
        mNotificationUtils.getManager().notify(101, nb.build());

    }
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }


}