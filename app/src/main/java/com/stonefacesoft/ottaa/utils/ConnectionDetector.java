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
       this.m_context = context;
   }

    public static boolean isNetworkAvailable(Context mContext) {
       m_context=mContext;
       ConnectivityManager connectivityManager=(ConnectivityManager) m_context.getSystemService(m_context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        }else {
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
        }
        return false;
    }

    public static boolean isNetworkAvailable(Activity mContext) {
        mActivity=mContext;
        ConnectivityManager connectivityManager=(ConnectivityManager) mActivity.getSystemService(m_context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        }else {
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
        }
        return false;
    }


    //TODO GASTON definir el metodo de deteccion de conteccion y borrar esto
   public boolean isConnectedToInternet() {
       ConnectivityManager manager = (ConnectivityManager) m_context.getSystemService(Context.CONNECTIVITY_SERVICE);

       boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
               .isConnectedOrConnecting();


//For 3G check


        if(manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!=null)
        {boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
               .isConnectedOrConnecting();
        if(!is3g&&!isWifi)

            return false;
        }
        else if(manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)==null)
        {
            if ( !isWifi)
            {
                return false;
            }
        }
//For WiFi Check



       return true;
   }



}