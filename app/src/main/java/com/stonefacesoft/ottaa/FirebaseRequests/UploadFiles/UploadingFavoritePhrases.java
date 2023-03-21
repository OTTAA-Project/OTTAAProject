package com.stonefacesoft.ottaa.FirebaseRequests.UploadFiles;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stonefacesoft.ottaa.utils.constants.Constants;

import java.io.FileNotFoundException;
import java.io.IOException;

public class UploadingFavoritePhrases extends UploadFile {
    public UploadingFavoritePhrases(Context mContext, DatabaseReference mDatabaseReference, StorageReference mStorageReference) {
        super(mContext,mDatabaseReference,mStorageReference);
    }

    @Override
    protected void openFile() {
        try {
            fis=mContext.openFileInput(Constants.ARCHIVO_FRASES_FAVORITAS);
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
                    mStorageReference.putStream(fis).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return mStorageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                String urlFrasesUpload = downloadUri.toString();

                                mDatabase.setValue(urlFrasesUpload, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference referenciaGrupos) {
                                        if (referenciaGrupos != null) {
                                            Log.d(TAG, "File succes");
                                            closeFile();
                                        } else {
                                            Log.d(TAG, databaseError.getMessage());
                                        }

                                    }
                                });

                            } else {
                                Log.d(TAG, task.getException().getMessage());
                                // Toast.makeText(MainActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
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
