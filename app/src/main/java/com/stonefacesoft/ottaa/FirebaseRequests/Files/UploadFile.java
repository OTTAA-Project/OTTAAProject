package com.stonefacesoft.ottaa.FirebaseRequests.Files;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;

public class UploadFile {
    protected String filename;
    protected File file;
    protected FileInputStream fis;
    protected DatabaseReference mDatabase;
    protected StorageReference mStorageReference;
    protected String TAG="";
    protected Context mContext;

    public UploadFile(Context mContext,DatabaseReference mDatabase,StorageReference mStorageReference){
        this.mContext=mContext;
        this.mDatabase=mDatabase;
        this.mStorageReference=mStorageReference;
    }
    public void openFile(){}
    public void closeFile(){}
    public void uploadFile(){}

    public UploadFile setTAG(String TAG) {
        this.TAG = TAG;
        return this;
    }
}
