package com.stonefacesoft.ottaa.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.stonefacesoft.ottaa.utils.constants.Constants;

public class RequestPersmissionClass {



    public boolean requestImagePermission(Context mActivity){
        boolean result = false;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            result = isGranted(mActivity,Manifest.permission.READ_MEDIA_IMAGES);
        }
        else{
            result =isGranted(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)&&!isGranted(mActivity,Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        return result;
    }
    public boolean requestLocationPermission(Context mActivity){
        boolean result = false;
            result = isGranted(mActivity,Manifest.permission.ACCESS_FINE_LOCATION)&& isGranted(mActivity,Manifest.permission.ACCESS_COARSE_LOCATION);
        return result;
    }

    public void makeRequestImagePermission(boolean request, AppCompatActivity mActivity){
        if(!request){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
                if(!isGranted(mActivity,Manifest.permission.READ_MEDIA_IMAGES))
                    ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.READ_MEDIA_IMAGES}, Constants.EXTERNAL_STORAGE);
            }else{
                mActivity.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.EXTERNAL_STORAGE);
            }
        }
    }


    private int requestPermision(Context appCompatActivity,String name){
        return ContextCompat.checkSelfPermission(appCompatActivity,name);
    }

    private boolean isGranted(Context mActivity,String name){
        return requestPermision(mActivity,name)==PackageManager.PERMISSION_GRANTED;
    }


}
