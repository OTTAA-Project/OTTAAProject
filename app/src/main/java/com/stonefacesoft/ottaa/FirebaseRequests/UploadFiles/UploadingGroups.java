package com.stonefacesoft.ottaa.FirebaseRequests.UploadFiles;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.utils.constants.Constants;

import java.io.FileNotFoundException;
import java.io.IOException;

public class UploadingGroups extends UploadFile{
    public UploadingGroups(Context mContext, DatabaseReference mDatabase, StorageReference mStorageReference) {
        super(mContext, mDatabase, mStorageReference);
    }

    @Override
    protected void openFile() {
        try {
            fis = mContext.openFileInput(Constants.ARCHIVO_GRUPOS);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void uploadFile() {
        try {
            openFile();
            if (fis.available() > 3) {
                Log.e("subirArchivosLog", "subirGruposFirebase: " + fis.available());
                mStorageReference.putStream(fis).continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return mStorageReference.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String urlGrupoUpload = downloadUri.toString();
                        Log.e("SAF_SGF__down", "" + urlGrupoUpload);
                        mDatabase.setValue(urlGrupoUpload, (databaseError, referenciaGrupos1) -> {
                            if (referenciaGrupos1 != null) {
                                Log.d("SAF_SGF_TAG", "Se guardo correctamente url Grupos");
                                closeFile();
                            } else {
                                Log.d("SAF_SGF_TAG", "Error al subir url Grupos");
                            }

                        });

                    } else {
                        Log.e("SAF_SGF_TAG", "Subida fallida " + task.getException());
                    }
                });

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void closeFile() {
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
