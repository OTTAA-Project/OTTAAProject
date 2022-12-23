package com.stonefacesoft.ottaa.utils;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

public class ConnectionDetector {
 
   private static Context m_context;
   private static Activity mActivity;
 
   public ConnectionDetector(Context context){
       m_context = context;
   }

    public static boolean isNetworkAvailable(Context mContext) {
       m_context=mContext;
       ConnectivityManager connectivityManager=(ConnectivityManager) m_context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }

        return false;
    }

    public static boolean isNetworkAvailable(Activity mContext) {
        mActivity=mContext;
        ConnectivityManager connectivityManager=(ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        return false;
    }

   public boolean isConnectedToInternet() {
       ConnectivityManager manager = (ConnectivityManager) m_context.getSystemService(Context.CONNECTIVITY_SERVICE);

       boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
               .isConnectedOrConnecting();


//For 3G check


        if(manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!=null)
        {boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
               .isConnectedOrConnecting();
            return is3g || isWifi;
        }
        else if(manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)==null)
        {
            return isWifi;
        }
//For WiFi Check



       return true;
   }


    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) m_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


}