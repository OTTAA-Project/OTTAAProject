package com.stonefacesoft.ottaa.utils;

/*@Author: Ruhi Bhandari
* @Created Date: 15/4/2016
* @PreferencesManager
* */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PreferencesManager {

    public static final String SHARED_PREFERENCES_NAME = "ICALL4HELP_PREFERENCES";

    /**
     * Instance
     */
    private static PreferencesManager preferencesManager = null;

    /**
     * Shared Preferences
     */
    private static SharedPreferences sharedPreferences;

    /**
     * Preferences variables
     */


    private final String FIRST_NAME = "FIRST_NAME";
    private final String USER_ID="USER_ID";
    private final String ONE_TIME_LOGIN="onetime";
    private final String IS_ONLINE_OR_OFFLINE_PRESSED_STATUS="YES";
    private final String IS_ONLINE_ONLY="online only";
    private final String ISSUED="issued";
    private final String SCANNED="scanned";
    private final String SCANNED_BY_ME="scanned_by_me";
    private final String IS_TICKETS_DOWNLOADED="is_tickets_downloaded";
    private final String EVENT_ID="event_id";
    private final String KEY="key";
    private final String PROGRESS_COUNT="progress_count";
    private final String DB_ID="db_id";
    private final String TOTAL_TICKETS="total_tickets";


    private PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static PreferencesManager getInstance(Context context) {

        if (preferencesManager == null) {
            Log.v("Preference status", "new object of " + context);
            preferencesManager = new PreferencesManager(context);
        } else {
            Log.v("Preference status", "old object of " + context);
        }

        return preferencesManager;
    }

    public String getTOTAL_TICKETS() {
        return sharedPreferences.getString(TOTAL_TICKETS, "");
    }

    public void setTOTAL_TICKETS(String user_id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOTAL_TICKETS, user_id);
        editor.apply();
    }
    public String getDB_ID() {
        return sharedPreferences.getString(DB_ID, "");
    }

    public void setDB_ID(String user_id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DB_ID, user_id);
        editor.apply();
    }

    public String getPROGRESS_COUNT() {
        return sharedPreferences.getString(PROGRESS_COUNT, "");
    }

    public void setPROGRESS_COUNT(String user_id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PROGRESS_COUNT, user_id);
        editor.apply();
    }


    public String getKEY() {
        return sharedPreferences.getString(KEY, "");
    }

    public void setKEY(String user_id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY, user_id);
        editor.apply();
    }

    public String getEVENT_ID() {
        return sharedPreferences.getString(EVENT_ID, "");
    }

    public void setEVENT_ID(String user_id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EVENT_ID, user_id);
        editor.apply();
    }
    public String getIS_TICKETS_DOWNLOADED() {
        return sharedPreferences.getString(IS_TICKETS_DOWNLOADED, "");
    }

    public void setIS_TICKETS_DOWNLOADED(String user_id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IS_TICKETS_DOWNLOADED, user_id);
        editor.apply();
    }


    public String getISSUED() {
        return sharedPreferences.getString(ISSUED, "");
    }

    public void setISSUED(String user_id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ISSUED, user_id);
        editor.apply();
    }



    public String getSCANNED() {
        return sharedPreferences.getString(SCANNED, "");
    }

    public void setSCANNED(String user_id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SCANNED, user_id);
        editor.apply();
    }


    public String getSCANNED_BY_ME() {
        return sharedPreferences.getString(SCANNED_BY_ME, "");
    }

    public void setSCANNED_BY_ME(String user_id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SCANNED_BY_ME, user_id);
        editor.apply();
    }



    public String getIS_ONLINE_OR_OFFLINE_PRESSED_STATUS() {
        return sharedPreferences.getString(IS_ONLINE_OR_OFFLINE_PRESSED_STATUS, "");
    }

    public void setIS_ONLINE_OR_OFFLINE_PRESSED_STATUS(String user_id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IS_ONLINE_OR_OFFLINE_PRESSED_STATUS, user_id);
        editor.apply();
    }





    public String getIS_ONLINE_ONLY() {
        return sharedPreferences.getString(IS_ONLINE_ONLY, "");
    }

    public void setIS_ONLINE_ONLY(String user_id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IS_ONLINE_ONLY, user_id);
        editor.apply();
    }







    public String getONE_TIME_LOGIN() {
        return sharedPreferences.getString(ONE_TIME_LOGIN, "");
    }

    public void setONE_TIME_LOGIN(String user_id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ONE_TIME_LOGIN, user_id);
        editor.apply();
    }

    public String getUSER_ID() {
        return sharedPreferences.getString(USER_ID, "");
    }

    public void setUSER_ID(String user_id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, user_id);
        editor.apply();
    }

    public String getFIRST_NAME() {
        return sharedPreferences.getString(FIRST_NAME, "");
    }

    public void setFIRST_NAME(String first_name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIRST_NAME, first_name);
        editor.apply();
    }

    public String getLAST_NAME() {
        String LAST_NAME = "LAST_NAME";
        return sharedPreferences.getString(LAST_NAME, "");
    }

    public void setLAST_NAME(String LAST_NAME) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_NAME, LAST_NAME);
        editor.apply();
    }

    public String getEMAIL_ID() {
        String EMAIL_ID = "EMAIL_ID";
        return sharedPreferences.getString(EMAIL_ID, "");
    }

    public void setEMAIL_ID(String EMAIL_ID) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL_ID, EMAIL_ID);
        editor.apply();
    }

    public String getPHONE_NUMBER() {
        String PHONE_NUMBER = "PHONE_NUMBER";
        return sharedPreferences.getString(PHONE_NUMBER, "");
    }

    public void setPHONE_NUMBER(String PHONE_NUMBER) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PHONE_NUMBER, PHONE_NUMBER);
        editor.apply();
    }

    public String getPASSWORD() {
        String PASSWORD = "PASSWORD";
        return sharedPreferences.getString(PASSWORD, "");
    }

    public void setPASSWORD(String PASSWORD) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSWORD, PASSWORD);
        editor.apply();
    }


    public void clearPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ACCESS_TOKEN", "");
        editor.putString("ACCESS_TOKEN_SECRET", "");
        editor.clear();
        editor.apply();
    }


}
