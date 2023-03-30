package com.stonefacesoft.ottaa.utils;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.stonefacesoft.ottaa.Interfaces.RemoteConfigListener;

/**
 * This method should be an singleton classes
 */

public class RemoteConfigUtils implements RemoteConfigListener.PriceListener,RemoteConfigListener.AvatarListener {
    private static RemoteConfigUtils remoteConfigUtils;
    private final FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseRemoteConfigSettings mfirebaseRemoteConfigSettings;


    private RemoteConfigUtils() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
    }

    public synchronized static RemoteConfigUtils getInstance() {
        if (remoteConfigUtils == null) remoteConfigUtils = new RemoteConfigUtils();
        return remoteConfigUtils;
    }

    public RemoteConfigUtils setDefaultParameter(int value) {
        mFirebaseRemoteConfig.setDefaultsAsync(value);
        return this;
    }

    public void activateFetched() {
        mFirebaseRemoteConfig.fetch().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mFirebaseRemoteConfig.fetchAndActivate();
            }
        }).addOnFailureListener(e -> {

        });
    }

    public void setActivateDeactivateConfig(Activity mActivity, OnCompleteListener onCompleteListener) {
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(mActivity, onCompleteListener);
    }


    @Override
    public boolean isRegionPriceEnabled() {
        return mFirebaseRemoteConfig.getBoolean("user_price_premium");
    }

    @Override
    public String changePrice(String text, String name) {
        if (text.contains("990 ARS"))
            return text.replace("990 ARS", mFirebaseRemoteConfig.getString(name));
        if (text.contains("9 USD"))
            return text.replace("9 USD", mFirebaseRemoteConfig.getString(name));
        if (text.contains("9 دولارات أمريكية"))
            return text.replace("9 دولارات أمريكية", mFirebaseRemoteConfig.getString(name));
        return text.replace("9 USD", mFirebaseRemoteConfig.getString(name));
    }


    @Override
    public String paymentUtri() {
        return mFirebaseRemoteConfig.getString("contactUs");
    }
    @Override
    public String paymentUtriPremium() {
        return mFirebaseRemoteConfig.getString("payment_uri_premium");
    }

    @Override
    public boolean enableAvatar() {
        return mFirebaseRemoteConfig.getBoolean("showAvatar");
    }

    @Override
    public String avatarMessages() {
        return mFirebaseRemoteConfig.getString("AvatarMessage");
    }

    public String getStringByName(String key){
        return mFirebaseRemoteConfig.getString(key);
    }

    public boolean getBooleanByName(String key){
        return mFirebaseRemoteConfig.getBoolean(key);
    }

    public FirebaseRemoteConfig getmFirebaseRemoteConfig() {
        return mFirebaseRemoteConfig;
    }
}
