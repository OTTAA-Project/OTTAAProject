package com.stonefacesoft.ottaa.FirebaseRequests.DownloadFile;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.ObservableInteger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class DownloadFile implements OnFailureListener {
    protected ObservableInteger observableInteger;
    protected File file;
    protected DatabaseReference mDatabase;
    protected SharedPreferences sharedPrefsDefault;
    protected StorageReference mStorageReference;
    protected String TAG="DownloadError";
    protected Context mContext;
    protected Json json;
    protected String locale;
    protected String email;
    protected String uid;
    public DownloadFile(Context mContext,DatabaseReference mDatabase,StorageReference mStorageReference,SharedPreferences sharedPreferences,ObservableInteger observableInteger){
        this.mContext = mContext;
        this.mDatabase = mDatabase;
        this.mStorageReference = mStorageReference;
        this.sharedPrefsDefault = sharedPreferences;
        this.observableInteger = observableInteger;
        this.json = Json.getInstance();
        this.locale = sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), Locale.getDefault().getLanguage());
    }


    protected static String getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }
    protected static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        int errorCode = ((StorageException) e).getErrorCode();
        String errorMessage = e.getMessage();
        Log.e(TAG, "onFailure: "+errorCode +" :"+errorMessage );
    }
}
