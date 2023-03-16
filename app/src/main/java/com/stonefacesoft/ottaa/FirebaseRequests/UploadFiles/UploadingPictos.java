package com.stonefacesoft.ottaa.FirebaseRequests.UploadFiles;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.utils.constants.Constants;

import java.io.FileNotFoundException;
import java.io.IOException;

public class UploadingPictos extends UploadFile{
    public UploadingPictos(Context mContext, DatabaseReference mDatabase, StorageReference mStorageReference) {
        super(mContext, mDatabase, mStorageReference);
    }

    @Override
    protected void openFile() {
        try {
            fis = mContext.openFileInput(Constants.ARCHIVO_PICTOS);
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

                Log.e("Subir Archivos Firebase", "subirPictosFirebase: " + fis.getChannel().size());
                mStorageReference.putStream(fis).continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return mStorageReference.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        String urlPictoUpload = downloadUri.toString();
                        mDatabase.setValue(urlPictoUpload, (databaseError, referenciaPictos1) -> {
                            if (referenciaPictos1 != null) {
                                Log.d("SAF_SPF_TAG", "Se guardo correctamente url Pictos");
                               closeFile();
                            } else {
                                Log.d("SAF_SPF_TAG", "Error al subir url Pictos");
                            }
                        });

                    } else {
                        Log.e("error_SAF_SPF_TAG", "Subida fallida" + task.getException());

                    }
                }).addOnFailureListener(e -> Log.e("No se subio el archivo", "onFailure: subir archivos"));
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
