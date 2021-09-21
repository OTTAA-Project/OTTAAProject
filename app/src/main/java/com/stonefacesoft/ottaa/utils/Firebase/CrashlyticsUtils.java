package com.stonefacesoft.ottaa.utils.Firebase;

import com.google.firebase.crashlytics.FirebaseCrashlytics;



public class CrashlyticsUtils {
    private final FirebaseCrashlytics crashlytics;
    private static CrashlyticsUtils myCrashliticsUtils;

    public static CrashlyticsUtils getInstance() {
        if(myCrashliticsUtils==null)
            myCrashliticsUtils=new CrashlyticsUtils();
        return myCrashliticsUtils;
    }

    private CrashlyticsUtils() {
        crashlytics=FirebaseCrashlytics.getInstance();
    }

    public FirebaseCrashlytics getCrashlytics() {
        return crashlytics;
    }
}
