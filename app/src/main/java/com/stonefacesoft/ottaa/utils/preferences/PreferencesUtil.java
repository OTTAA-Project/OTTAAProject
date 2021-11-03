package com.stonefacesoft.ottaa.utils.preferences;

import android.content.SharedPreferences;

import java.util.Set;

public class PreferencesUtil {
    private final SharedPreferences preferences;
    public PreferencesUtil(SharedPreferences preferences){
        this.preferences=preferences;
    }
    public void applyBooleanKey(String key,boolean value){
        preferences.edit().putBoolean(key,value).apply();
    }
    public boolean getBooleanKey(String key,boolean value){
        return preferences.getBoolean(key,value);
    }
    public void applyStringValue(String key,String value){
        preferences.edit().putString(key,value).apply();
    }

    public String getStringValue(String key,String defaultValue){
        return preferences.getString(key,defaultValue);
    }

    public void applyStringValues(String key, Set<String> values){
        preferences.edit().putStringSet(key,values).apply();
    }

    public Set<String> getStringValues(String key,Set<String> values){
        return preferences.getStringSet(key,values);
    }

    public void applyLongValue(String key,long value){
        preferences.edit().putLong(key, value).apply();
    }

    public long getLongValue(String key,long value){
        return preferences.getLong(key,value);
    }

    public void applyFloatValue(String key,float value){
        preferences.edit().putFloat(key,value).apply();
    }

    public float getFloatValue(String key,float value){
        return preferences.getFloat(key, value);
    }

    public void applyIntValue(String key,int value){
        preferences.edit().putInt(key, value).apply();
    }

    public int getIntegerValue(String key,int value){
        return preferences.getInt(key, value);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }
}
