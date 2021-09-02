package com.stonefacesoft.ottaa.utils;

import android.os.StrictMode;

public class StrictModePolicyClass {
    private static boolean enabled=false;

    public static void enableStrictMode(){
        enabled=true;
        StrictMode.ThreadPolicy.Builder threadPolicyBuilder=new StrictMode.ThreadPolicy.Builder().detectNetwork()
                .detectDiskReads()
                .detectDiskWrites()
                .penaltyLog()
                .penaltyDialog();
        StrictMode.VmPolicy.Builder threadVmPolicyBuilder= new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .detectActivityLeaks();
        StrictMode.setThreadPolicy(threadPolicyBuilder.build());
        StrictMode.setVmPolicy(threadVmPolicyBuilder.build());
    }
    public static void allowDiskReadAndWrite(Runnable runnable){
        StrictMode.ThreadPolicy oldThreadPolicy=null;
        if(enabled){
            oldThreadPolicy=StrictMode.getThreadPolicy();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(oldThreadPolicy).permitDiskReads().permitDiskWrites().build());
        }
        runnable.run();
        if (oldThreadPolicy != null) StrictMode.setThreadPolicy(oldThreadPolicy);

    }
    public static void allowDiskWrite(Runnable runnable){
        StrictMode.ThreadPolicy oldThreadPolicy=null;
        if(enabled){
            oldThreadPolicy=StrictMode.getThreadPolicy();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(oldThreadPolicy).permitDiskWrites().build());
        }
        runnable.run();
        if (oldThreadPolicy != null) StrictMode.setThreadPolicy(oldThreadPolicy);

    }
}
