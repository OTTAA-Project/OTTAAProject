package com.stonefacesoft.ottaa.utils.Firebase;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by GastonSaillen on 22/12/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseInstanceId";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "onNewToken: " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}
