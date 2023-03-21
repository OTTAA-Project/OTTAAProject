package com.stonefacesoft.ottaa.FirebaseRequests.UploadFiles;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.utils.constants.Constants;

import java.io.FileNotFoundException;
import java.io.IOException;

public class UploadPhrase extends UploadFile{
    public UploadPhrase(Context mContext, DatabaseReference mDatabase, StorageReference mStorageReference) {
        super(mContext, mDatabase, mStorageReference);
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
                        String urlFrasesUpload = downloadUri.toString();

                        mDatabase.setValue(urlFrasesUpload, (databaseError, referenciaGrupos) -> {
                            if (referenciaGrupos != null) {
                                closeFile();
                                Log.d("SAF_SFF_TAG", "Se guardo correctamente url Frases");
                            } else {
                                Log.d("SAF_SFF_TAG", "Error al subir url Frases");
                            }

                        });

                    } else {
                        Log.d("SAF_SFF_TAG", "Error al subir url Frases");
                        // Toast.makeText(MainActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
    protected void openFile() {
        try {
            fis = mContext.openFileInput(Constants.ARCHIVO_FRASES);
        } catch (FileNotFoundException e) {
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
