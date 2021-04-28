package com.stonefacesoft.ottaa.utils.Pictures;

import android.content.Context;

import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;

public class Picture {
    private String name;
    private String StoragePath;
    private String texto;
    private String id;
    private final FirebaseUtils firebaseUtils;
    private final Context mContext;

    public Picture(String id, Context mContext){
        this.mContext=mContext;
        firebaseUtils=FirebaseUtils.getInstance();
        firebaseUtils.setmContext(this.mContext);

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStoragePath(String storagePath) {
        StoragePath = storagePath;
    }
}
