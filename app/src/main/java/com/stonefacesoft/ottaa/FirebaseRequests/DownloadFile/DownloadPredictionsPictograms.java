package com.stonefacesoft.ottaa.FirebaseRequests.DownloadFile;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.utils.ObservableInteger;
import com.stonefacesoft.ottaa.utils.constants.Constants;

import java.io.File;

public class DownloadPredictionsPictograms extends DownloadFile{
    public DownloadPredictionsPictograms(Context mContext, DatabaseReference mDatabase, StorageReference mStorageReference, SharedPreferences sharedPreferences, ObservableInteger observableInteger,String locate) {
        super(mContext, mDatabase, mStorageReference, sharedPreferences, observableInteger,locate);
        TAG ="DownloadPredictionsPictograms";

    }

    public void downloadPictograms(FirebaseSuccessListener successListener){
        File pictosDatabaseFile = new File(rootPath, Constants.ARCHIVO_PICTOS_DATABASE);
        mStorageReference.getFile(pictosDatabaseFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                Log.e("md5Hash", "onSuccess: Se bajo pictosDatabase ");

                try {
                    if (!getStringFromFile(pictosDatabaseFile.getAbsolutePath()).equals("[]") && pictosDatabaseFile.length() > 0) {
                        Log.e("prueba", getStringFromFile(pictosDatabaseFile.getAbsolutePath()));
                        json.setmJSONArrayPictosSugeridos(json.readJSONArrayFromFile(pictosDatabaseFile.getAbsolutePath()));
                        if (!json.guardarJson(Constants.ARCHIVO_PICTOS_DATABASE))
                            Log.e(TAG, "Error al guardar Json");
                        successListener.onPictosSugeridosBajados(true);
                    }

                } catch (Exception e1) {
                    e1.printStackTrace();
                    successListener.onPictosSugeridosBajados(true);
                }

            }

        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                successListener.onPictosSugeridosBajados(true);
            }
        });
    }
}
