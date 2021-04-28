package com.stonefacesoft.ottaa.test.Components;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class Preferences {
  private final SharedPreferences preferences;
    public Preferences(Context mContext){
        preferences= PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void editBoolean(String name,Boolean value){
        preferences.edit().putBoolean(name,value).apply();
    }

    public void editString(String name,String value){
        preferences.edit().putString(name,value).apply();
    }

    public void editInteger(String name,int value){
        preferences.edit().putInt(name,value).apply();
    }

    public void editFloat(String name,float value){
        preferences.edit().putFloat(name,value);
    }

    public void editLong(String name,long value){
        preferences.edit().putLong(name,value);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }
}
