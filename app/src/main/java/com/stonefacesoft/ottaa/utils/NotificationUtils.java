package com.stonefacesoft.ottaa.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import com.stonefacesoft.ottaa.R;

import androidx.annotation.RequiresApi;

public class NotificationUtils extends ContextWrapper {

    private NotificationManager mNotificationManager;


    public NotificationUtils(Context base) {

        super(base);
        createChannels();
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels() {
        int importance = NotificationManager.IMPORTANCE_HIGH;
        @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(getString(R.string.channel_notification_id), getString(R.string.channel_name), importance);
        channel.setShowBadge(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getAndroidChannelNotification(String title, String body) {
        return new Notification.Builder(getApplicationContext(), getString(R.string.channel_notification_id))
                .setContentTitle(title)
                .setContentText(body).setChannelId(getString(R.string.channel_notification_id))
                .setSmallIcon(R.drawable.icono_blanco2)
                .setAutoCancel(true);

    }

    //use them to delete notifications in android
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteNotificationChannel(String channelId) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.deleteNotificationChannel(channelId);
    }

}
